package com.miro.widget.service.repositories;

import com.miro.widget.exceptions.WidgetNotFoundException;
import com.miro.widget.service.models.Widget;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Profile("!h2-repository")
public class InMemoryRepositoryImpl implements WidgetRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryRepositoryImpl.class);

    private final ConcurrentMap<UUID, Widget> idToWidgetMap;
    private final ConcurrentNavigableMap<Integer, Widget> zIndexToWidgetMap;

    @Autowired
    public InMemoryRepositoryImpl() {
        idToWidgetMap = new ConcurrentHashMap<>();
        zIndexToWidgetMap = new ConcurrentSkipListMap<>();
    }

    @Override
    public Widget save(Widget widget) {
        var existingWidget = idToWidgetMap.getOrDefault(widget.getId(), null);

        if (existingWidget != null
            && !existingWidget.getZ().equals(widget.getZ())
            && zIndexToWidgetMap.get(existingWidget.getZ()).getId() == existingWidget.getId()) {
            zIndexToWidgetMap.remove(existingWidget.getZ());
        }

        var newWidget = new Widget(
            widget.getId(),
            widget.getZ(),
            widget.getCenterX(),
            widget.getCenterY(),
            widget.getWidth(),
            widget.getHeight(),
            ZonedDateTime.now()
        );

        idToWidgetMap.put(widget.getId(), newWidget);
        zIndexToWidgetMap.put(widget.getZ(), newWidget);

        return newWidget;
    }

    @Override
    public <S extends Widget> Iterable<S> saveAll(Iterable<S> widgets) {
        var result = new ArrayList<S>();

        for (var widget : widgets) {
            result.add((S) save(widget));
        }

        return result;
    }

    public Optional<Widget> findById(UUID id) {
        var widget = idToWidgetMap.getOrDefault(id, null);

        if (widget == null)
            return Optional.empty();

        return Optional.of(new Widget(
            widget.getId(),
            widget.getZ(),
            widget.getCenterX(),
            widget.getCenterY(),
            widget.getWidth(),
            widget.getHeight(),
            widget.getUpdatedAt()
        ));
    }

    public Optional<Widget> findByZ(int z) {
        var widget = zIndexToWidgetMap.getOrDefault(z, null);

        if (widget == null)
            return Optional.empty();

        return Optional.of(new Widget(
            widget.getId(),
            widget.getZ(),
            widget.getCenterX(),
            widget.getCenterY(),
            widget.getWidth(),
            widget.getHeight(),
            widget.getUpdatedAt()
        ));
    }

    @Override
    public boolean existsById(UUID id) {
        return idToWidgetMap.containsKey(id);
    }

    @Override
    public Iterable<Widget> findAll() {
        return zIndexToWidgetMap
            .values()
            .stream()
            .map(x -> new Widget(
                x.getId(),
                x.getZ(),
                x.getCenterX(),
                x.getCenterY(),
                x.getWidth(),
                x.getHeight(),
                x.getUpdatedAt()))
            .collect(Collectors.toList());
    }

    @Override
    public Iterable<Widget> findAllById(Iterable<UUID> ids) {
        var result = new ArrayList<Widget>();

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
        if (removedByIdWidget == null) {
            var message = String.format("Widget with id '%s' not found", id);
            log.warn(message);
            throw new WidgetNotFoundException(message);
        }

        var removedByZIndexWidget = zIndexToWidgetMap.remove(removedByIdWidget.getZ());
        if (removedByZIndexWidget == null) {
            var message = String.format(
                "Widget with z index '%d' wasn't found while one with ID '%s' was", removedByIdWidget.getZ(), id);
            log.error(message);
            throw new WidgetNotFoundException(message);
        }

        if (!removedByIdWidget.equals(removedByZIndexWidget)) {
            var message = String.format(
                "Widget deleted by ID is not the same as deleted by z index. First: %s, second: %s",
                removedByIdWidget,
                removedByZIndexWidget);
            log.error(message);
            throw new WidgetNotFoundException(message);
        }
    }

    @Override
    public void delete(Widget widget) {
        deleteById(widget.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> ids) {
        for (var id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Widget> widgets) {
        for (var widget : widgets) {
            deleteById(widget.getId());
        }
    }

    public void deleteAll() {
        idToWidgetMap.clear();
        zIndexToWidgetMap.clear();
    }

    @Override
    public Page<Widget> findAll(Pageable pageable) {
        var allWidgetsCount = zIndexToWidgetMap.values().size();

        var pageWidgets = zIndexToWidgetMap
            .values()
            .stream()
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .limit(pageable.getPageSize())
            .map(x -> new Widget(
                x.getId(),
                x.getZ(),
                x.getCenterX(),
                x.getCenterY(),
                x.getWidth(),
                x.getHeight(),
                x.getUpdatedAt()
            ))
            .collect(Collectors.toList());

        return new PageImpl<>(pageWidgets, pageable, allWidgetsCount);
    }
}
