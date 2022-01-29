package com.miro.widget.service.repositories;

import com.miro.widget.mappers.BllAndDalMapper;
import com.miro.widget.service.models.*;
import com.miro.widget.service.models.widget.*;
import com.miro.widget.service.repositories.models.*;
import com.miro.widget.service.repositories.models.params.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import result.*;
import result.errors.Error;
import result.errors.NotFoundError;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class InMemoryRepositoryImpl implements WidgetRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryRepositoryImpl.class);

    private final ConcurrentMap<UUID, WidgetEntity> idToWidgetMap;
    private final ConcurrentNavigableMap<Integer, WidgetEntity> zIndexToWidgetMap;

    private final BllAndDalMapper mapper;

    @Autowired
    public InMemoryRepositoryImpl(BllAndDalMapper mapper) {
        idToWidgetMap = new ConcurrentHashMap<>();
        zIndexToWidgetMap = new ConcurrentSkipListMap<>();

        this.mapper = mapper;
    }

    public Result<Widget> insert(InsertWidgetParams model) {
        try {
            var widgetEntity = mapper.insertionParamsToEntity(model);

            idToWidgetMap.put(widgetEntity.getId(), widgetEntity);
            zIndexToWidgetMap.put(widgetEntity.getZ(), widgetEntity);

            return Result.Ok(mapper.entityToBllModel(widgetEntity));
        } catch (Exception exc) {
            var message = String.format("Failed to insert widget %s: %s", model, exc.getMessage());
            log.error(message);
            return Result.Fail(new Error(message));
        }
    }

    public Result<Widget> getById(UUID id) {
        var widgetEntity = idToWidgetMap.getOrDefault(id, null);

        if (widgetEntity == null) {
            var message = String.format("Widget with id '%s' not found", id);
            log.warn(message);
            return Result.Fail(new NotFoundError(message));
        }

        return Result.Ok(mapper.entityToBllModel(widgetEntity));
    }

    public Result<Widget> getByZIndex(int z) {
        var widgetEntity = zIndexToWidgetMap.getOrDefault(z, null);

        if (widgetEntity == null) {
            var message = String.format("Widget with z index '%d' not found", z);
            log.warn(message);
            return Result.Fail(new NotFoundError(message));
        }

        return Result.Ok(mapper.entityToBllModel(widgetEntity));
    }

    public Result<WidgetRange> getRange(int page, int size) {
        try {
            var valuesCount = zIndexToWidgetMap.values().size();
            var pagesCount = valuesCount / size;

            if (pagesCount * size < valuesCount)
                pagesCount++;

            return Result.Ok(new WidgetRange(
                valuesCount,
                pagesCount,
                zIndexToWidgetMap
                    .values()
                    .stream()
                    .skip((long) (page - 1) * size)
                    .limit(size)
                    .map(mapper::entityToBllModel)
                    .collect(Collectors.toList())
            ));
        } catch (Exception exc) {
            var message = String.format("Failed to retrieve all widgets: %s", exc.getMessage());
            log.error(message);
            return Result.Fail(new Error(message));
        }
    }

    public Result<Widget> update(UpdateWidgetParams model) {
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

        return Result.Ok(mapper.entityToBllModel(widgetEntity));
    }

    public PlainResult delete(UUID id) {
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

    public PlainResult deleteAll() {
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
