package com.miro.widget.service.repositories;

import com.miro.widget.mappers.BllAndDalMapper;
import com.miro.widget.service.models.*;
import com.miro.widget.service.repositories.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import result.PlainResult;
import result.Result;
import result.errors.Error;
import result.errors.NotFoundError;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryRepositoryImpl implements WidgetRepository {
    private final ConcurrentMap<UUID, V1WidgetEntity> idToWidgetMap;
    private final ConcurrentMap<Integer, V1WidgetEntity> zIndexToWidgetMap;

    private final BllAndDalMapper mapper;

    @Autowired
    public InMemoryRepositoryImpl(BllAndDalMapper mapper) {
        idToWidgetMap = new ConcurrentHashMap<UUID, V1WidgetEntity>();
        zIndexToWidgetMap = new ConcurrentHashMap<Integer, V1WidgetEntity>();

        this.mapper = mapper;
    }

    public Result<V1WidgetDto> v1Insert(V1InsertWidgetModel model) {
        var widgetEntity = mapper.v1InsertModelToEntity(model);

        try {
            idToWidgetMap.put(widgetEntity.getId(), widgetEntity);
            zIndexToWidgetMap.put(widgetEntity.getZ(), widgetEntity);
        } catch (Exception exc) {
            var error = String.format("Failed to insert widget %s: %s", model, exc.getMessage());
            return Result.Fail(new Error(error));
        }

        return Result.Ok(mapper.v1EntityToDto(widgetEntity));
    }

    public Result<V1WidgetDto> v1GetById(UUID id) {
        var widgetEntity = idToWidgetMap.getOrDefault(id, null);

        if (widgetEntity == null)
            return Result.Fail(new NotFoundError(String.format("Widget with id '%s' not found", id)));

        return Result.Ok(mapper.v1EntityToDto(widgetEntity));
    }

    public Result<V1WidgetDto> v1GetByZIndex(int z) {
        var widgetEntity = zIndexToWidgetMap.getOrDefault(z, null);

        if (widgetEntity == null)
            return Result.Fail(new NotFoundError(String.format("widget with z index '%d' not found", z)));

        return Result.Ok(mapper.v1EntityToDto(widgetEntity));
    }

    public Result<List<V1WidgetDto>> v1GetAll() {
        try {
            return Result.Ok(idToWidgetMap
                .values()
                .stream()
                .map(mapper::v1EntityToDto)
                .sorted(Comparator.comparing(V1WidgetDto::getZ))
                .collect(Collectors.toList()));
        } catch (Exception exc) {
            var error = String.format("Failed to retrieve all widgets: %s", exc.getMessage());
            return Result.Fail(new Error(error));
        }
    }

    public Result<V1WidgetDto> v1Update(V1UpdateWidgetModel model) {
        var widgetEntity = idToWidgetMap.getOrDefault(model.getId(), null);

        if (widgetEntity == null)
            return Result.Fail(new NotFoundError(String.format("Widget with id '%s' not found", model.getId())));

        if (model.getCenterX() != null)
            widgetEntity.setCenterX(model.getCenterX());

        if (model.getCenterY() != null)
            widgetEntity.setCenterY(model.getCenterY());

        if (model.getWidth() != null)
            widgetEntity.setWidth(model.getWidth());

        if (model.getHeight() != null)
            widgetEntity.setHeight(model.getHeight());

        if (model.getZ() != null) {
            widgetEntity.setZ(model.getZ());
            zIndexToWidgetMap.put(widgetEntity.getZ(), widgetEntity);
        }

        widgetEntity.setUpdatedAt(ZonedDateTime.now());

        return Result.Ok(mapper.v1EntityToDto(widgetEntity));
    }

    public PlainResult v1Delete(UUID id) {
        var removedByIdWidget = idToWidgetMap.remove(id);
        if (removedByIdWidget == null)
            return PlainResult.Fail(new NotFoundError(String.format("Widget with id '%s' not found", id)));

        var removedByZIndexWidget = zIndexToWidgetMap.remove(removedByIdWidget.getZ());
        if (removedByZIndexWidget == null) {
            var error = String.format(
                "Widget with z index '%d' wasn't found while one with ID '%s' was", removedByIdWidget.getZ(), id);
            return PlainResult.Fail(new Error(error));
        }

        if (!removedByIdWidget.equals(removedByZIndexWidget)) {
            return PlainResult.Fail(new Error(String.format(
                "Widget deleted by ID is not the same as deleted by z index. First: %s, second: %s",
                removedByIdWidget,
                removedByZIndexWidget)));
        }

        return PlainResult.Ok();
    }

    public PlainResult v1DeleteAll() {
        try {
            idToWidgetMap.clear();
            zIndexToWidgetMap.clear();
        } catch (Exception exc) {
            var error = String.format("Failed to delete all widgets: %s", exc.getMessage());
            return PlainResult.Fail(new Error(error));
        }

        return PlainResult.Ok();
    }
}
