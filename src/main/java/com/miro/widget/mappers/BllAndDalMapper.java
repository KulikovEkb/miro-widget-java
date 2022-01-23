package com.miro.widget.mappers;

import com.miro.widget.service.models.V1WidgetDto;
import com.miro.widget.service.repositories.models.V1InsertWidgetModel;
import com.miro.widget.service.repositories.models.V1WidgetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class BllAndDalMapper {
    @Mapping(source = "centerX", target = "coordinates.centerX")
    @Mapping(source = "centerY", target = "coordinates.centerY")
    @Mapping(source = "width", target = "size.width")
    @Mapping(source = "height", target = "size.height")
    public abstract V1WidgetDto v1EntityToDto(V1WidgetEntity entity);

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
