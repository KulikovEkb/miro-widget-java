package com.miro.widget.mappers;

import com.miro.widget.controllers.models.requests.V1CreateWidgetRequest;
import com.miro.widget.controllers.models.requests.V1UpdateWidgetRequest;
import com.miro.widget.controllers.models.responses.V1Coordinates;
import com.miro.widget.controllers.models.responses.V1Size;
import com.miro.widget.controllers.models.responses.V1WidgetDto;
import com.miro.widget.service.models.Widget;
import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class WidgetsMapper {
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

    public V1WidgetDto bllModelToDto(Widget bllModel) {
        return new V1WidgetDto(
            bllModel.getId(),
            bllModel.getZ(),
            new V1Coordinates(
                bllModel.getCenterX(),
                bllModel.getCenterY()
            ),
            new V1Size(
                bllModel.getWidth(),
                bllModel.getHeight()
            ),
            bllModel.getUpdatedAt()
        );
    }

    public Widget creationParamsToBllModel(CreateWidgetParams params, int z) {
        return new Widget(
            UUID.randomUUID(),
            z,
            params.getCenterX(),
            params.getCenterY(),
            params.getWidth(),
            params.getHeight(),
            null
        );
    }

    public Widget updatingParamsToBllModel(UpdateWidgetParams params, Widget currentWidget) {
        return new Widget(
            currentWidget.getId(),
            params.getZ() == null ? currentWidget.getZ() : params.getZ(),
            params.getCenterX() == null ? currentWidget.getCenterX() : params.getCenterX(),
            params.getCenterY() == null ? currentWidget.getCenterY() : params.getCenterY(),
            params.getWidth() == null ? currentWidget.getWidth() : params.getWidth(),
            params.getHeight() == null ? currentWidget.getHeight() : params.getHeight(),
            null);
    }

    public Widget toWidgetWithNewZ(Widget currentWidget, int newZ) {
        return new Widget(
            currentWidget.getId(),
            newZ,
            currentWidget.getCenterX(),
            currentWidget.getCenterY(),
            currentWidget.getWidth(),
            currentWidget.getHeight(),
            null
        );
    }
}
