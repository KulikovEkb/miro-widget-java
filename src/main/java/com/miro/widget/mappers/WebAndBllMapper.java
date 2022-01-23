package com.miro.widget.mappers;

import com.miro.widget.controllers.models.requests.*;
import com.miro.widget.controllers.models.responses.*;
import com.miro.widget.service.models.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class WebAndBllMapper {
    public V1CreateWidgetDto v1CreateRequestToDto(V1CreateWidgetRequest request) {
        return new V1CreateWidgetDto(
            request.getCenterX(),
            request.getCenterY(),
            request.getZ(),
            request.getWidth(),
            request.getHeight()
        );
    }

    public V1UpdateWidgetDto v1UpdateRequestToDto(V1UpdateWidgetRequest request) {
        return new V1UpdateWidgetDto(
            request.getCenterX(),
            request.getCenterY(),
            request.getZ(),
            request.getWidth(),
            request.getHeight()
        );
    }

    public V1CreateWidgetResponse v1DtoToCreationResponse(V1WidgetDto dto) {
        return new V1CreateWidgetResponse(
            dto.getId(),
            dto.getZ(),
            new V1Coordinates(
                dto.getCoordinates().getCenterX(),
                dto.getCoordinates().getCenterY()
            ),
            new V1Size(
                dto.getSize().getWidth(),
                dto.getSize().getHeight()
            ),
            dto.getUpdatedAt()
        );
    }

    public V1UpdateWidgetResponse v1DtoToUpdatingResponse(V1WidgetDto dto) {
        return new V1UpdateWidgetResponse(
            dto.getId(),
            dto.getZ(),
            new V1Coordinates(
                dto.getCoordinates().getCenterX(),
                dto.getCoordinates().getCenterY()
            ),
            new V1Size(
                dto.getSize().getWidth(),
                dto.getSize().getHeight()
            ),
            dto.getUpdatedAt()
        );
    }

    public V1GetWidgetByIdResponse v1DtoToGetByIdResponse(V1WidgetDto dto) {
        return new V1GetWidgetByIdResponse(
            dto.getId(),
            dto.getZ(),
            new V1Coordinates(
                dto.getCoordinates().getCenterX(),
                dto.getCoordinates().getCenterY()
            ),
            new V1Size(
                dto.getSize().getWidth(),
                dto.getSize().getHeight()
            ),
            dto.getUpdatedAt()
        );
    }

    public V1GetAllWidgetsItem v1DtoToGetAllItem(V1WidgetDto dto) {
        return new V1GetAllWidgetsItem(
            dto.getId(),
            dto.getZ(),
            new V1Coordinates(
                dto.getCoordinates().getCenterX(),
                dto.getCoordinates().getCenterY()
            ),
            new V1Size(
                dto.getSize().getWidth(),
                dto.getSize().getHeight()
            ),
            dto.getUpdatedAt()
        );
    }
}
