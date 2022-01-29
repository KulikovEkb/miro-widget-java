package com.miro.widget.mappers;

import com.miro.widget.service.models.widget.*;
import com.miro.widget.service.repositories.models.params.InsertWidgetParams;
import com.miro.widget.service.repositories.models.WidgetEntity;
import org.mapstruct.Mapper;

import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class BllAndDalMapper {
    public Widget entityToBllModel(WidgetEntity entity) {
        return new Widget(
            entity.getId(),
            entity.getZ(),
            new Coordinates(
                entity.getCenterX(),
                entity.getCenterY()
            ),
            new Size(
                entity.getWidth(),
                entity.getHeight()
            ),
            entity.getUpdatedAt());
    }

    public WidgetEntity insertionParamsToEntity(InsertWidgetParams model) {
        return new WidgetEntity(
            UUID.randomUUID(),
            model.getZ(),
            model.getCenterX(),
            model.getCenterY(),
            model.getWidth(),
            model.getHeight(),
            ZonedDateTime.now()
        );
    }
}
