package com.miro.widget.mappers;

import com.miro.widget.service.models.V1CoordinatesDto;
import com.miro.widget.service.models.V1SizeDto;
import com.miro.widget.service.models.V1WidgetDto;
import com.miro.widget.service.repositories.models.V1InsertWidgetModel;
import com.miro.widget.service.repositories.models.V1WidgetEntity;
import org.mapstruct.Mapper;

import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class BllAndDalMapper {
    public V1WidgetDto v1EntityToDto(V1WidgetEntity entity) {
        return new V1WidgetDto(
            entity.getId(),
            entity.getZIndex(),
            new V1CoordinatesDto(
                entity.getCenterX(),
                entity.getCenterY()
            ),
            new V1SizeDto(
                entity.getWidth(),
                entity.getHeight()
            ),
            entity.getUpdatedAt());
    }

    public V1WidgetEntity v1InsertModelToEntity(V1InsertWidgetModel model) {
        return new V1WidgetEntity(
            UUID.randomUUID(),
            model.getZIndex(),
            model.getCenterX(),
            model.getCenterY(),
            model.getWidth(),
            model.getHeight(),
            ZonedDateTime.now()
        );
    }
}
