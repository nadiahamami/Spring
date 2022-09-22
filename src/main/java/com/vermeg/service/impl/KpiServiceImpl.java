package com.vermeg.service.impl;

import com.google.gson.JsonObject;
import com.vermeg.configurations.RestTemplateClient;
import com.vermeg.entities.Fields;
import com.vermeg.entities.User;
import com.vermeg.payload.requests.Reporting;
import com.vermeg.payload.responses.*;

import com.vermeg.repositories.*;
import com.vermeg.service.KpiService;
import com.vermeg.service.mapper.FieldsServiceMapper;
import com.vermeg.service.mapper.IssueDetailsMapper;
import com.vermeg.service.mapper.ProjectInfoServiceMapper;
import com.vermeg.utils.Uri;
import com.vermeg.utils.Utilities;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;


@Service
public class KpiServiceImpl implements KpiService {

    private final RestTemplateClient restTemplateClient;
    private final ProjectInfoServiceMapper projectInfoServiceMapper;
    private final FieldsServiceMapper fieldsServiceMapper;
    private final IssueDetailsMapper issueDetailsMapper;
    private final UserRepository userRepository;
    private final  IssuTypeRepository issuTypeRepository ;

    public KpiServiceImpl(RestTemplateClient restTemplateClient, ProjectInfoServiceMapper projectInfoServiceMapper, FieldsServiceMapper fieldsServiceMapper, IssueDetailsMapper issueDetailsMapper, UserRepository userRepository, IssuTypeRepository issuTypeRepository) {
        this.restTemplateClient = restTemplateClient;
        this.projectInfoServiceMapper = projectInfoServiceMapper;
        this.fieldsServiceMapper = fieldsServiceMapper;
        this.issueDetailsMapper = issueDetailsMapper;
        this.userRepository = userRepository;
        this.issuTypeRepository = issuTypeRepository;
    }

    @Override
    public List<ProjectInfo> getProjectInfo() {

        if(issuTypeRepository.findAll().size()>0){
            JsonObject jsonObject = restTemplateClient.getResponseRestTemplate(Uri.getProjectUri(), HttpMethod.GET);
            List<ProjectInfo> projectInfos = projectInfoServiceMapper
                    .mapToDTOList(jsonObject.getAsJsonArray("values"));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            User user = userRepository.findByEmail(authentication.getName());
            if(!user.getRole().getName().equals("Role_super_Admin"))
                projectInfos = projectInfos.stream().filter(projectInfo ->
                        user.getProjects().stream().anyMatch(project -> project.getProjectKey().equals(projectInfo.getProjectKey())))
                        .collect(Collectors.toList());

            projectInfos.stream().forEach(projectInfo -> {
                projectInfo.setTicktes(Ticktes.builder()
                        .story(getcountIssueTypeByProject("STORY", projectInfo.getProjectKey()))
                        .task(getcountIssueTypeByProject("TASK", projectInfo.getProjectKey()))
                        .epic(getcountIssueTypeByProject("EPIC", projectInfo.getProjectKey()))
                        .bugs(getcountIssueTypeByProject("BUG", projectInfo.getProjectKey())).build());


            });
            return projectInfos;
        }
       else{
           return  null ;
        }
    }

    @Override
    public Long getcountIssueTypeByProject(String ussueType, String projectKey) {
        String url = Uri.getJqlUri("issuetype= " + ussueType + " AND project= " + projectKey);
        JsonObject jsonObject = restTemplateClient.getResponseRestTemplate(url, HttpMethod.GET);
        return jsonObject.get("total").getAsLong();
    }

    @Override
    public Map<String, List<Fields>> getIssueTypeStatusByCreatedDate(String key, String startDate, String endDate) {
        String url = Uri.getJqlUri("project= " + key + " and created >= " + "'" + startDate + "'" + " and created <= " + "'" + endDate + "'");
        JsonObject jsonObject = restTemplateClient.getResponseRestTemplate(url, HttpMethod.GET);
        List<Fields> fieldsList = fieldsServiceMapper.mapToDTOList(jsonObject.getAsJsonArray("issues"));

        return fieldsList.stream().collect(groupingBy(fields -> fields.getStatus().getName()));
    }

    @Override
    public List<KpiFields> getIssueTypeByCreatedDate(String key, String startDate, String endDate) {
        String url = Uri.getJqlUri("project= " + key + " and created >= " + "'" + startDate + "'" + " and created <= " + "'" + endDate + "'");
        JsonObject jsonObject = restTemplateClient.getResponseRestTemplate(url, HttpMethod.GET);
        List<Fields> fieldsList = fieldsServiceMapper.mapToDTOList(jsonObject.getAsJsonArray("issues"));

        Map<Integer, List<Fields>> byIssueType = fieldsList.stream()
                .collect(groupingBy(fields -> fields.getCreated().getMonth().getValue()));

        List<KpiFields> kpiFields = new ArrayList<>();
        byIssueType.entrySet().stream().forEach(stringListEntry -> {
            kpiFields.add(KpiFields.builder()
                    .month(stringListEntry.getKey())
                    .fields(stringListEntry.getValue().stream().collect(groupingBy(fields -> fields.getIssuetype().getName())))
                    .build());
        });
        kpiFields.sort(Comparator.comparing(KpiFields::getMonth));
        return kpiFields;
    }

    @Override
    public List<IssueDetails> getAllListIssueDetails(String projectKey) {
        String url = Uri.getJqlUri(" project= " + projectKey);
        JsonObject jsonObject = restTemplateClient.getResponseRestTemplate(url, HttpMethod.GET);
        return issueDetailsMapper.mapToDTOList(jsonObject.getAsJsonArray("issues"));
    }

    @Override
    public List<IssueDetails> getListIssueDetailsBySatatus(String status, String projectKey) {
        List<IssueDetails> issueDetails = getAllListIssueDetails(projectKey);
        return issueDetails.stream()
                .filter(issueDetails1 -> issueDetails1.getStatus().toLowerCase().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueDetails> reportingData(Reporting reporting) {
        if(issuTypeRepository.findAll().size() > 0){
            JsonObject jsonObject = restTemplateClient.getResponseRestTemplate(Uri.jqlUri, HttpMethod.GET);
            List<IssueDetails> issueDetails = issueDetailsMapper
                    .mapToDTOList(jsonObject.getAsJsonArray("issues"));
            if (reporting.getStatus() != null && !reporting.getStatus().equals("All"))
                issueDetails = issueDetails
                        .stream()
                        .filter(issueDetails1 -> issueDetails1.getIssueType().equalsIgnoreCase(reporting.getStatus()))
                        .collect(Collectors.toList());

            if (reporting.getProjectkey() != null && !reporting.getProjectkey().equals("All"))
                issueDetails = issueDetails
                        .stream()
                        .filter(issueDetails1 -> issueDetails1.getKey().contains(reporting.getProjectkey()))
                        .collect(Collectors.toList());

            return issueDetails.stream()
                    .filter(issueDetails1 -> issueDetails1.getCreated().isAfter(Utilities.convertStringToLocalDate(reporting.getStartDate()))
                            && issueDetails1.getCreated().isBefore(Utilities.convertStringToLocalDate(reporting.getEndDate())))
                    .collect(Collectors.toList());
        }else{
            return  null ;
        }

    }

    @Override
    public ByteArrayInputStream reportingExcel(Reporting reporting) {
        List<IssueDetails> issueDetails = reportingData(reporting);
        try {
            return Utilities.getExcel(issueDetails, "Reporting Jira  ", "classpath:word/jirapi.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
