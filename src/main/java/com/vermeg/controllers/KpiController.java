package com.vermeg.controllers;


import com.vermeg.configurations.RestTemplateClient;
import com.vermeg.entities.Fields;
import com.vermeg.payload.requests.Reporting;
import com.vermeg.payload.requests.SearchApi;
import com.vermeg.payload.responses.*;
import com.vermeg.service.KpiService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/kpi")
public class KpiController {


    private final KpiService kpiService ;
    private final MessageSource messageSource;



    public KpiController(KpiService kpiService, RestTemplateClient restTemplateClient, MessageSource messageSource) {
        this.kpiService = kpiService;
        this.messageSource = messageSource;
    }

    @GetMapping(value = "/projects")
    public ApiResponse<List<ProjectInfo>> getAllProject(){
        String messageResponse = messageSource.getMessage("jira.project",
                null, LocaleContextHolder.getLocale());
        return new ApiResponse(200,  messageResponse  ,kpiService.getProjectInfo());
    }

    @GetMapping(value = "/issueType/{issueType}/project/{projectKey}")
    public ApiResponse<Long> getUssueTypeByProjectKey(@PathVariable String issueType , @PathVariable String projectKey ){
        String messageResponse = messageSource.getMessage("jira.issueType",
                null, LocaleContextHolder.getLocale());
        return new ApiResponse(200,  messageResponse  ,kpiService.getcountIssueTypeByProject(issueType , projectKey));
    }

    @PostMapping(value = "/status/project")
    public ApiResponse<Map<String, List<Fields>>> getUssuesTypeStatusByCreatedDate(@RequestBody SearchApi searchApi){
        return new ApiResponse(200,  null ,
                kpiService.getIssueTypeStatusByCreatedDate(searchApi.getKey(),searchApi.getStartDate(),searchApi.getEndDate()));
    }

    @PostMapping(value = "/fields/project")
    public ApiResponse<List<KpiFields>> getUssuesTypeFieldsByCreatedDate(@RequestBody SearchApi searchApi){
        return new ApiResponse(200,  null ,
                kpiService.getIssueTypeByCreatedDate(searchApi.getKey(),searchApi.getStartDate(),searchApi.getEndDate()));
    }

    @GetMapping(value = "/all/project/{projectKey}")
    public ApiResponse<List<IssueDetails>> getAllListIssueDetails(@PathVariable String projectKey){
        return new ApiResponse(200,  null ,
                kpiService.getAllListIssueDetails(projectKey));
    }

    @GetMapping(value = "/project/{projectKey}/issueType/{issueType}/")
    public ApiResponse<List<IssueDetails>> getAllUssueTypeByProjectKey(@PathVariable String issueType , @PathVariable String projectKey ){
        String messageResponse = messageSource.getMessage("jira.issueType",
                null, LocaleContextHolder.getLocale());
        return new ApiResponse(200,  messageResponse  ,kpiService.getListIssueDetailsBySatatus(issueType , projectKey));
    }

    @PostMapping(value = "/reproting/all")
    public ApiResponse<List<IssueDetails>> getReportingData(@RequestBody Reporting reporting){
        String messageResponse = messageSource.getMessage("jira.issueType",
                null, LocaleContextHolder.getLocale());
        return new ApiResponse<>(200,  messageResponse  ,kpiService.reportingData(reporting));
    }

    @PostMapping("generated_excel_reporting")
    public ResponseEntity<Resource> generatedExcelComptaOpenig(@RequestBody Reporting filterDto ) throws IOException {
        InputStreamResource file = new InputStreamResource(kpiService.reportingExcel(filterDto) );
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);

    }
}
