package com.vermeg.service.mapper;


import com.google.gson.JsonObject;
import com.vermeg.configurations.GenericMapper;
import com.vermeg.payload.responses.ProjectInfo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class ProjectInfoServiceMapper implements GenericMapper<ProjectInfo> {

    private final ModelMapper modelMapper ;

    public ProjectInfoServiceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public ProjectInfo mapToDTO(JsonObject jsonObject) {
        if(!jsonObject.isJsonNull()){

            ProjectInfo projectInfo = modelMapper.map(jsonObject.get("location") , ProjectInfo.class);
            projectInfo.setId(jsonObject.get("id").getAsLong());
            return projectInfo;
        }
        return null;
    }
}
