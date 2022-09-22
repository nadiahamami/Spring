package com.vermeg.service.mapper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vermeg.configurations.GenericMapper;

import com.vermeg.entities.Fields;
import com.vermeg.entities.IssueType;
import com.vermeg.entities.Project;
import com.vermeg.entities.Status;
import com.vermeg.utils.Utilities;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class FieldsServiceMapper implements GenericMapper<Fields> {

    private final ModelMapper modelMapper ;

    public FieldsServiceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public Fields mapToDTO(JsonObject jsonObject) {
        if(!jsonObject.isJsonNull()){
            JsonElement jsonObject1 = jsonObject.get("fields") ;
            Status status = modelMapper.map(jsonObject1.getAsJsonObject().get("status") , Status.class);
            String summary =  jsonObject1.getAsJsonObject().get("summary").getAsString();
            IssueType issueType = modelMapper.map(jsonObject1.getAsJsonObject().get("issuetype") , IssueType.class);
            Project projectInfo = modelMapper.map(jsonObject1.getAsJsonObject().get("project") , Project.class);
            LocalDate created = Utilities.convertStringToLocalDate(jsonObject1.getAsJsonObject().get("created").getAsString()) ;
            return Fields.builder()
                    .summary(summary)
                    .fiedlsKey(jsonObject.getAsJsonObject().get("key").getAsString())
                    .issuetype(issueType)
                    .project(projectInfo)
                    .status(status)
                    .created(created)
                    .build();
        }
        return null;
    }
}
