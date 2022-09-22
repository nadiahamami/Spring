package com.vermeg.configurations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vermeg.utils.Utilities;

import java.util.List;
import java.util.stream.Collectors;

public interface GenericMapper <D> {
    D mapToDTO(JsonObject jsonObject);


    default List<D> mapToDTOList(JsonArray jsonObject){
        return Utilities.arrayToStream(jsonObject)
                .map(JsonElement::getAsJsonObject)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    };


}
