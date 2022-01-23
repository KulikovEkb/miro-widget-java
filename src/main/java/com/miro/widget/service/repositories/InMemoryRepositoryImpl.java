package com.miro.widget.service.repositories;

import com.miro.widget.service.models.V1CoordinatesDto;
import com.miro.widget.service.models.V1SizeDto;
import com.miro.widget.service.models.V1WidgetDto;
import com.miro.widget.service.repositories.models.V1InsertWidgetModel;
import com.miro.widget.service.repositories.models.V1UpdateWidgetModel;
import com.miro.widget.service.repositories.models.V1WidgetEntity;
import org.springframework.stereotype.Repository;

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

    public InMemoryRepositoryImpl() {
        idToWidgetMap = new ConcurrentHashMap<UUID, V1WidgetEntity>();
        zIndexToWidgetMap = new ConcurrentHashMap<Integer, V1WidgetEntity>();
    }

    public V1WidgetDto v1Insert(V1InsertWidgetModel model) {
        var widgetEntity = new V1WidgetEntity(
            UUID.randomUUID(),
            model.getZIndex(),
            model.getCenterX(),
            model.getCenterY(),
            model.getWidth(),
            model.getHeight(),
            ZonedDateTime.now()
        );

        idToWidgetMap.put(widgetEntity.getId(), widgetEntity);
        zIndexToWidgetMap.put(widgetEntity.getZIndex(), widgetEntity);

        return new V1WidgetDto(
            widgetEntity.getId(),
            widgetEntity.getZIndex(),
            new V1CoordinatesDto(
                widgetEntity.getCenterX(),
                widgetEntity.getCenterY()
            ),
            new V1SizeDto(
                widgetEntity.getWidth(),
                widgetEntity.getHeight()
            ),
            widgetEntity.getUpdatedAt());
    }

    public V1WidgetDto v1GetById(UUID id) {
        var widgetEntity = idToWidgetMap.getOrDefault(id, null);

        // todo(kulikov): uncomment
        /*if (widget == null){
            return new Result(NotFoundError);*/

        return new V1WidgetDto(
            widgetEntity.getId(),
            widgetEntity.getZIndex(),
            new V1CoordinatesDto(
                widgetEntity.getCenterX(),
                widgetEntity.getCenterY()
            ),
            new V1SizeDto(
                widgetEntity.getWidth(),
                widgetEntity.getHeight()
            ),
            widgetEntity.getUpdatedAt());
    }

    public V1WidgetDto v1GetByZIndex(int zIndex) {
        var widgetEntity = zIndexToWidgetMap.getOrDefault(zIndex, null);

        // todo(kulikov): uncomment
        /*if (widget == null){
            return new Result(NotFoundError);*/

        return new V1WidgetDto(
            widgetEntity.getId(),
            widgetEntity.getZIndex(),
            new V1CoordinatesDto(
                widgetEntity.getCenterX(),
                widgetEntity.getCenterY()
            ),
            new V1SizeDto(
                widgetEntity.getWidth(),
                widgetEntity.getHeight()
            ),
            widgetEntity.getUpdatedAt());
    }

    public List<V1WidgetDto> v1GetAll() {
        return idToWidgetMap
            .values()
            .stream()
            .map(widgetEntity -> new V1WidgetDto(
                widgetEntity.getId(),
                widgetEntity.getZIndex(),
                new V1CoordinatesDto(
                    widgetEntity.getCenterX(),
                    widgetEntity.getCenterY()
                ),
                new V1SizeDto(
                    widgetEntity.getWidth(),
                    widgetEntity.getHeight()
                ),
                widgetEntity.getUpdatedAt()))
            .sorted(Comparator.comparing(V1WidgetDto::getZIndex))
            .collect(Collectors.toList());
    }

    public V1WidgetDto v1Update(V1UpdateWidgetModel model) {
        var widgetEntity = idToWidgetMap.getOrDefault(model.getId(), null);

        // todo(kulikov): uncomment
        /*if (widget == null){
            return new Result(NotFoundError);*/

        if (model.getCenterX() != null)
            widgetEntity.setCenterX(model.getCenterX());

        if (model.getCenterY() != null)
            widgetEntity.setCenterY(model.getCenterY());

        if (model.getWidth() != null)
            widgetEntity.setWidth(model.getWidth());

        if (model.getHeight() != null)
            widgetEntity.setHeight(model.getHeight());

        if (model.getZIndex() != null) {
            widgetEntity.setZIndex(model.getZIndex());
            zIndexToWidgetMap.put(widgetEntity.getZIndex(), widgetEntity);
        }

        widgetEntity.setUpdatedAt(ZonedDateTime.now());

        return new V1WidgetDto(
            widgetEntity.getId(),
            widgetEntity.getZIndex(),
            new V1CoordinatesDto(
                widgetEntity.getCenterX(),
                widgetEntity.getCenterY()
            ),
            new V1SizeDto(
                widgetEntity.getWidth(),
                widgetEntity.getHeight()
            ),
            widgetEntity.getUpdatedAt());
    }

    public void v1Delete(UUID id) {
        var widgetEntity = idToWidgetMap.remove(id);
        zIndexToWidgetMap.remove(widgetEntity.getZIndex());
    }

    public void v1DeleteAll() {
        idToWidgetMap.clear();
        zIndexToWidgetMap.clear();
    }
}
