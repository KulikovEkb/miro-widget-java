package com.miro.widget.service.repositories;

import com.miro.widget.service.models.Widget2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Profile("!h2-repository")
public class InMemoryRepositoryImpl2 implements WidgetRepository2 {
    private static final Logger log = LoggerFactory.getLogger(InMemoryRepositoryImpl2.class);

    private final ConcurrentMap<UUID, Widget2> idToWidgetMap;
    private final ConcurrentNavigableMap<Integer, Widget2> zIndexToWidgetMap;

    @Autowired
    public InMemoryRepositoryImpl2() {
        idToWidgetMap = new ConcurrentHashMap<>();
        zIndexToWidgetMap = new ConcurrentSkipListMap<>();
    }

    @Override
    public <S extends Widget2> S save(S widget) {
        var existingWidget = idToWidgetMap.getOrDefault(widget.getId(), null);

        if (existingWidget != null
            && !existingWidget.getZ().equals(widget.getZ())
            && zIndexToWidgetMap.get(existingWidget.getZ()).getId() == existingWidget.getId()) {
            zIndexToWidgetMap.remove(existingWidget.getZ());
        }

        widget.setUpdatedAt(ZonedDateTime.now());

        idToWidgetMap.put(widget.getId(), widget);
        zIndexToWidgetMap.put(widget.getZ(), widget);

        return widget;
    }

    @Override
    public <S extends Widget2> Iterable<S> saveAll(Iterable<S> widgets) {
        var result = new ArrayList<S>();

        for (var widget : widgets) {
            result.add(save(widget));
        }

        return result;
    }

    public Optional<Widget2> findById(UUID id) {
        return Optional.ofNullable(idToWidgetMap.getOrDefault(id, null));
    }

    public Optional<Widget2> findByZ(int z) {
        return Optional.ofNullable(zIndexToWidgetMap.getOrDefault(z, null));
    }

    @Override
    public boolean existsById(UUID id) {
        return idToWidgetMap.containsKey(id);
    }

    @Override
    public Iterable<Widget2> findAll() {
        return zIndexToWidgetMap.values();
    }

    @Override
    public Iterable<Widget2> findAllById(Iterable<UUID> ids) {
        var result = new ArrayList<Widget2>();

        for (var id : ids) {
            findById(id).ifPresent(result::add);
        }

        return result;
    }

    @Override
    public long count() {
        return idToWidgetMap.size();
    }

    public void deleteById(UUID id) {
        var removedByIdWidget = idToWidgetMap.remove(id);

        zIndexToWidgetMap.remove(removedByIdWidget.getZ());
    }

    @Override
    public void delete(Widget2 widget) {
        var removedByIdWidget = idToWidgetMap.remove(widget.getId());

        zIndexToWidgetMap.remove(removedByIdWidget.getZ());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> ids) {
        for (var id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Widget2> widgets) {
        for (var widget : widgets) {
            delete(widget);
        }
    }

    public void deleteAll() {
        idToWidgetMap.clear();
        zIndexToWidgetMap.clear();
    }

    @Override
    public Page<Widget2> findAll(Pageable pageable) {
        var allWidgetsCount = zIndexToWidgetMap.values().size();

        var pageWidgets = zIndexToWidgetMap
            .values()
            .stream()
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .limit(pageable.getPageSize())
            .collect(Collectors.toList());

        return new PageImpl<>(pageWidgets, pageable, allWidgetsCount);
    }
}
