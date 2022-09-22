package com.vermeg.service.mapper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vermeg.configurations.GenericMapper;
import com.vermeg.entities.IssueType;
import com.vermeg.entities.Status;
import com.vermeg.payload.responses.*;
import com.vermeg.utils.Utilities;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class IssueDetailsMapper implements GenericMapper<IssueDetails> {

    private final ModelMapper modelMapper ;


    public IssueDetailsMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public IssueDetails mapToDTO(JsonObject jsonObject) {
        if(!jsonObject.isJsonNull()) {
            JsonElement jsonObject1 = jsonObject.get("fields");
            String key = jsonObject.getAsJsonObject().get("key").getAsString();
            String summary =  jsonObject1.getAsJsonObject().get("summary").getAsString();
            Status status = modelMapper.map(jsonObject1.getAsJsonObject().get("status"), Status.class);
            IssueType issueType = modelMapper.map(jsonObject1.getAsJsonObject().get("issuetype"), IssueType.class);
            ProjectInfo projectInfo = modelMapper.map(jsonObject1.getAsJsonObject().get("project"), ProjectInfo.class);

            Person creator = jsonObject1.getAsJsonObject().get("creator").isJsonNull() ?null:
                    modelMapper.map(jsonObject1.getAsJsonObject().get("creator"), Person.class);

            Person assignee = jsonObject1.getAsJsonObject().get("assignee").isJsonNull() ?
                 null  :   modelMapper.map(jsonObject1.getAsJsonObject().get("assignee"), Person.class) ;

            Person reporter =  jsonObject1.getAsJsonObject().get("reporter").isJsonNull() ?null :
                    modelMapper.map(jsonObject1.getAsJsonObject().get("reporter"), Person.class) ;

            LocalDate created = Utilities.convertStringToLocalDate(jsonObject1.getAsJsonObject().get("created").getAsString());
            LocalDate updated = Utilities.convertStringToLocalDate(jsonObject1.getAsJsonObject().get("updated").getAsString());

            return  IssueDetails.builder()
                    .key(key)
                    .issueType(issueType.getName())
                    .summary(summary)
                    .reporter(reporter == null ?"-" : reporter.getDisplayName())
                    .creator(creator == null ? "-" : creator.getDisplayName())
                    .assignee(assignee == null ? "-":assignee.getDisplayName())
                    .status(status.getName())
                    .created(created)
                    .updated(updated)
                    .build();
        }
        return  null ;
    }
}
