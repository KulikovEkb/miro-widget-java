package com.miro.widget.mappers;

import com.miro.widget.controllers.models.requests.*;
import com.miro.widget.controllers.models.responses.*;
import com.miro.widget.service.models.params.*;
import com.miro.widget.service.models.widget.Widget;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class WebAndBllMapper {
    public CreateWidgetParams v1CreateRequestToBllModel(V1CreateWidgetRequest request) {
        return new CreateWidgetParams(
            request.getCenterX(),
            request.getCenterY(),
            request.getZ(),
            request.getWidth(),
            request.getHeight()
        );
    }

    public UpdateWidgetParams v1UpdateRequestToBllModel(V1UpdateWidgetRequest request) {
        return new UpdateWidgetParams(
            request.getCenterX(),
            request.getCenterY(),
            request.getZ(),
            request.getWidth(),
            request.getHeight()
        );
    }

    public V1CreateWidgetResponse bllModelToV1CreationResponse(Widget bllModel) {
        return new V1CreateWidgetResponse(
            bllModel.getId(),
            bllModel.getZ(),
            new V1Coordinates(
                bllModel.getCoordinates().getCenterX(),
                bllModel.getCoordinates().getCenterY()
            ),
            new V1Size(
                bllModel.getSize().getWidth(),
                bllModel.getSize().getHeight()
            ),
            bllModel.getUpdatedAt()
        );
    }

    public V1UpdateWidgetResponse bllModelToV1UpdatingResponse(Widget bllModel) {
        return new V1UpdateWidgetResponse(
            bllModel.getId(),
            bllModel.getZ(),
            new V1Coordinates(
                bllModel.getCoordinates().getCenterX(),
                bllModel.getCoordinates().getCenterY()
            ),
            new V1Size(
                bllModel.getSize().getWidth(),
                bllModel.getSize().getHeight()
            ),
            bllModel.getUpdatedAt()
        );
    }

    public V1GetWidgetByIdResponse bllModelToV1GetByIdResponse(Widget bllModel) {
        return new V1GetWidgetByIdResponse(
            bllModel.getId(),
            bllModel.getZ(),
            new V1Coordinates(
                bllModel.getCoordinates().getCenterX(),
                bllModel.getCoordinates().getCenterY()
            ),
            new V1Size(
                bllModel.getSize().getWidth(),
                bllModel.getSize().getHeight()
            ),
            bllModel.getUpdatedAt()
        );
    }

    public V1GetRangeItem bllModelToV1GetRangeItem(Widget bllModel) {
        return new V1GetRangeItem(
            bllModel.getId(),
            bllModel.getZ(),
            new V1Coordinates(
                bllModel.getCoordinates().getCenterX(),
                bllModel.getCoordinates().getCenterY()
            ),
            new V1Size(
                bllModel.getSize().getWidth(),
                bllModel.getSize().getHeight()
            ),
            bllModel.getUpdatedAt()
        );
    }
}
