package com.miro.widget.service.repositories;

import com.miro.widget.mappers.BllAndDalMapper;
import com.miro.widget.service.models.*;
import com.miro.widget.service.repositories.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import result.PlainResult;
import result.Result;
import result.errors.Error;
import result.errors.NotFoundError;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryRepositoryImpl implements WidgetRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryRepositoryImpl.class);
    private static final ConcurrentMap<UUID, V1WidgetEntity> idToWidgetMap = new ConcurrentHashMap<>();
    private static final ConcurrentNavigableMap<Integer, V1WidgetEntity> zIndexToWidgetMap =
        new ConcurrentSkipListMap<>();

    private final BllAndDalMapper mapper;

    @Autowired
    public InMemoryRepositoryImpl(BllAndDalMapper mapper) {
        this.mapper = mapper;
    }

    public Result<V1WidgetDto> v1Insert(V1InsertWidgetModel model) {
        try {
            var widgetEntity = mapper.v1InsertModelToEntity(model);

            idToWidgetMap.put(widgetEntity.getId(), widgetEntity);
            zIndexToWidgetMap.put(widgetEntity.getZ(), widgetEntity);

            return Result.Ok(mapper.v1EntityToDto(widgetEntity));
        } catch (Exception exc) {
            var message = String.format("Failed to insert widget %s: %s", model, exc.getMessage());
            log.error(message);
            return Result.Fail(new Error(message));
        }
    }

    public Result<V1WidgetDto> v1GetById(UUID id) {
        var widgetEntity = idToWidgetMap.getOrDefault(id, null);

        if (widgetEntity == null) {
            var message = String.format("Widget with id '%s' not found", id);
            log.warn(message);
            return Result.Fail(new NotFoundError(message));
        }

        return Result.Ok(mapper.v1EntityToDto(widgetEntity));
    }

    public Result<V1WidgetDto> v1GetByZIndex(int z) {
        var widgetEntity = zIndexToWidgetMap.getOrDefault(z, null);

        if (widgetEntity == null) {
            var message = String.format("Widget with z index '%d' not found", z);
            log.warn(message);
            return Result.Fail(new NotFoundError(message));
        }

        return Result.Ok(mapper.v1EntityToDto(widgetEntity));
    }

    public Result<List<V1WidgetDto>> v1GetAll() {
        try {
            return Result.Ok(zIndexToWidgetMap
                .values()
                .stream()
                .map(mapper::v1EntityToDto)
                .collect(Collectors.toList()));
        } catch (Exception exc) {
            var message = String.format("Failed to retrieve all widgets: %s", exc.getMessage());
            log.error(message);
            return Result.Fail(new Error(message));
        }
    }

    public Result<V1WidgetDto> v1Update(V1UpdateWidgetModel model) {
        var widgetEntity = idToWidgetMap.getOrDefault(model.getId(), null);

        if (widgetEntity == null) {
            var message = String.format("Widget with id '%s' not found", model.getId());
            log.error(message);
            return Result.Fail(new NotFoundError(message));
        }

        if (model.getCenterX() != null)
            widgetEntity.setCenterX(model.getCenterX());

        if (model.getCenterY() != null)
            widgetEntity.setCenterY(model.getCenterY());

        if (model.getWidth() != null)
            widgetEntity.setWidth(model.getWidth());

        if (model.getHeight() != null)
            widgetEntity.setHeight(model.getHeight());

        if (model.getZ() != null) {
            if (zIndexToWidgetMap.get(widgetEntity.getZ()).getId() == widgetEntity.getId()) {
                zIndexToWidgetMap.remove(widgetEntity.getZ());
            }

            widgetEntity.setZ(model.getZ());
            zIndexToWidgetMap.put(widgetEntity.getZ(), widgetEntity);
        }

        widgetEntity.setUpdatedAt(ZonedDateTime.now());

        return Result.Ok(mapper.v1EntityToDto(widgetEntity));
    }

    public PlainResult v1Delete(UUID id) {
        var removedByIdWidget = idToWidgetMap.remove(id);
        if (removedByIdWidget == null) {
            var message = String.format("Widget with id '%s' not found", id);
            log.warn(message);
            return PlainResult.Fail(new NotFoundError(message));
        }

        var removedByZIndexWidget = zIndexToWidgetMap.remove(removedByIdWidget.getZ());
        if (removedByZIndexWidget == null) {
            var message = String.format(
                "Widget with z index '%d' wasn't found while one with ID '%s' was", removedByIdWidget.getZ(), id);
            log.error(message);
            return PlainResult.Fail(new Error(message));
        }

        if (!removedByIdWidget.equals(removedByZIndexWidget)) {
            var message = String.format(
                "Widget deleted by ID is not the same as deleted by z index. First: %s, second: %s",
                removedByIdWidget,
                removedByZIndexWidget);
            log.error(message);
            return PlainResult.Fail(new Error(message));
        }

        return PlainResult.Ok();
    }

    public PlainResult v1DeleteAll() {
        try {
            idToWidgetMap.clear();
            zIndexToWidgetMap.clear();
        } catch (Exception exc) {
            var message = String.format("Failed to delete all widgets: %s", exc.getMessage());
            log.error(message);
            return PlainResult.Fail(new Error(message));
        }

        return PlainResult.Ok();
    }
}
