package com.vermeg.service.impl;

import com.google.gson.JsonObject;
import com.vermeg.configurations.RestTemplateClient;
import com.vermeg.entities.*;
import com.vermeg.payload.responses.ProjectInfo;
import com.vermeg.repositories.*;
import com.vermeg.service.DataService;
import com.vermeg.service.mapper.FieldsServiceMapper;
import com.vermeg.service.mapper.ProjectInfoServiceMapper;
import com.vermeg.utils.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    private final RestTemplateClient restTemplateClient;
    private final FieldsServiceMapper fieldsServiceMapper;
    private final FieldsRepository fieldsRepository ;
    private final IssuTypeRepository issuTypeRepository ;
    private final  ProjectRepository projectRepository ;
    private final StatusRepository statusRepository ;
    private final ProjectInfoServiceMapper projectInfoServiceMapper;
    @Autowired
    private UserRepository userRepository;


    public DataServiceImpl(RestTemplateClient restTemplateClient, FieldsServiceMapper fieldsServiceMapper, FieldsRepository fieldsRepository, IssuTypeRepository issuTypeRepository, ProjectRepository projectRepository, StatusRepository statusRepository, ProjectInfoServiceMapper projectInfoServiceMapper) {
        this.restTemplateClient = restTemplateClient;
        this.fieldsServiceMapper = fieldsServiceMapper;
        this.fieldsRepository = fieldsRepository;
        this.issuTypeRepository = issuTypeRepository;
        this.projectRepository = projectRepository;
        this.statusRepository = statusRepository;
        this.projectInfoServiceMapper = projectInfoServiceMapper;
    }

    @Override
    public void save() {
        JsonObject projectObject = restTemplateClient.getResponseRestTemplate(Uri.getProjectUri(), HttpMethod.GET);
        List<ProjectInfo> projectInfos = projectInfoServiceMapper
                .mapToDTOList(projectObject.getAsJsonArray("values"));
        projectInfos.stream().forEach(projectInfo -> {
            Project project = projectRepository.findByNameContaining(projectInfo.getProjectName());
            if(project == null)
                projectRepository.save(Project.builder()
                        .name(projectInfo.getName())
                        .projectKey(projectInfo.getProjectKey())
                        .projectName(projectInfo.getProjectName())
                        .displayName(projectInfo.getDisplayName())
                        .build());

        });

        JsonObject jsonObject = restTemplateClient.getResponseRestTemplate(Uri.jqlUri, HttpMethod.GET);

        List<Fields> fieldsList = fieldsServiceMapper.mapToDTOList(jsonObject.getAsJsonArray("issues"));
        fieldsList.stream().forEach(fields -> {
            IssueType issueType = issuTypeRepository.findByName(fields.getIssuetype().getName());
            // Add IssueType
            if(issueType == null)
                fields.setIssuetype(issuTypeRepository.save(fields.getIssuetype()));
            else
                fields.setIssuetype(issueType);

            //ADD Project in Not existe
            Project project = projectRepository.findByNameContaining(fields.getProject().getName());
            if(project == null)
                fields.setProject(projectRepository.save(fields.getProject()));
            else
                fields.setProject(project);

            Status status = statusRepository.findByName(fields.getStatus().getName());
            if(status == null)
                fields.setStatus(statusRepository.save(fields.getStatus()));
            else
                fields.setStatus(status);

            if(fieldsRepository.findByFiedlsKey(fields.getFiedlsKey()) == null)
                fieldsRepository.save(fields) ;

        });
    }

    @Override
    public List<Project> getAllProject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Project> projects =  projectRepository.findAll();
        User user = userRepository.findByEmail(authentication.getName());
        if(!user.getRole().getName().equals("Role_super_Admin"))
            projects = projects.stream().filter(projectInfo ->
                    user.getProjects().stream().anyMatch(project -> project.getProjectKey().equals(projectInfo.getProjectKey())))
                    .collect(Collectors.toList());
        return projects ;
    }
}
