package com.miro.widget.mappers;

import com.miro.widget.controllers.models.requests.*;
import com.miro.widget.controllers.models.responses.*;
import com.miro.widget.service.models.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
// https://mapstruct.org/documentation/stable/reference/html/#adding-custom-methods
public interface WebAndBllMapper {
    V1CreateWidgetDto v1CreateRequestToDto(V1CreateWidgetRequest request);

    V1UpdateWidgetDto v1UpdateRequestToDto(V1UpdateWidgetRequest request);

    V1CreateWidgetResponse v1DtoToCreationResponse(V1WidgetDto dto);

    V1UpdateWidgetResponse v1DtoToUpdatingResponse(V1WidgetDto dto);

    V1GetWidgetByIdResponse v1DtoToGetByIdResponse(V1WidgetDto dto);

    V1GetAllWidgetsItem v1DtoToGetAllItem(V1WidgetDto dto);
}
