package com.vermeg.service;

import com.vermeg.entities.Project;
import com.vermeg.payload.requests.Reporting;
import com.vermeg.entities.Fields;
import com.vermeg.payload.responses.KpiFields;
import com.vermeg.payload.responses.IssueDetails;
import com.vermeg.payload.responses.ProjectInfo;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

public interface KpiService {

    List<ProjectInfo> getProjectInfo();

    Long getcountIssueTypeByProject(String ussueType , String projectKey);
    Map<String, List<Fields>> getIssueTypeStatusByCreatedDate(String key, String startDate , String endDate ) ;
    List<KpiFields> getIssueTypeByCreatedDate(String key, String startDate , String endDate ) ;

    List<IssueDetails> getAllListIssueDetails(String projectKey);
    List<IssueDetails> getListIssueDetailsBySatatus(String ussueType , String projectKey);

    List<IssueDetails> reportingData(Reporting reporting);

    ByteArrayInputStream reportingExcel(Reporting reporting );
}
