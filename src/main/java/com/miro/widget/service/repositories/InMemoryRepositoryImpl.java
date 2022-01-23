package com.miro.widget.service.repositories;

import com.miro.widget.mappers.BllAndDalMapper;
import com.miro.widget.service.models.*;
import com.miro.widget.service.repositories.models.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final BllAndDalMapper mapper;

    @Autowired
    public InMemoryRepositoryImpl(BllAndDalMapper mapper) {
        idToWidgetMap = new ConcurrentHashMap<UUID, V1WidgetEntity>();
        zIndexToWidgetMap = new ConcurrentHashMap<Integer, V1WidgetEntity>();

        this.mapper = mapper;
    }

    public V1WidgetDto v1Insert(V1InsertWidgetModel model) {
        var widgetEntity = mapper.v1InsertModelToEntity(model);

        idToWidgetMap.put(widgetEntity.getId(), widgetEntity);
        zIndexToWidgetMap.put(widgetEntity.getZIndex(), widgetEntity);

        return mapper.v1EntityToDto(widgetEntity);
    }

    public V1WidgetDto v1GetById(UUID id) {
        var widgetEntity = idToWidgetMap.getOrDefault(id, null);

        if (widgetEntity == null)
            return null;
        // todo(kulikov): uncomment
        /*if (widget == null){
            return new Result(NotFoundError);*/

        return mapper.v1EntityToDto(widgetEntity);
    }

    public V1WidgetDto v1GetByZIndex(int zIndex) {
        var widgetEntity = zIndexToWidgetMap.getOrDefault(zIndex, null);

        if (widgetEntity == null)
            return null;
        // todo(kulikov): uncomment
        /*if (widget == null){
            return new Result(NotFoundError);*/

        return mapper.v1EntityToDto(widgetEntity);
    }

    public List<V1WidgetDto> v1GetAll() {
        return idToWidgetMap
            .values()
            .stream()
            .map(mapper::v1EntityToDto)
            .sorted(Comparator.comparing(V1WidgetDto::getZIndex))
            .collect(Collectors.toList());
    }

    public V1WidgetDto v1Update(V1UpdateWidgetModel model) {
        var widgetEntity = idToWidgetMap.getOrDefault(model.getId(), null);

        if (widgetEntity == null)
            return null;
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

        return mapper.v1EntityToDto(widgetEntity);
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
