package com.miro.widget.service;

import com.miro.widget.exceptions.WidgetNotFoundException;
import com.miro.widget.service.models.Widget2;
import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import com.miro.widget.service.repositories.WidgetRepository2;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WidgetServiceImpl2 implements WidgetService2 {
    private final WidgetRepository2 widgetRepository;

    private int nextMaxIndex;

    @Autowired
    public WidgetServiceImpl2(WidgetRepository2 widgetRepository) {
        nextMaxIndex = 0;
        this.widgetRepository = widgetRepository;
    }

    @Synchronized
    public Widget2 create(CreateWidgetParams params) {
        if (nextMaxIndex == Integer.MAX_VALUE)
            throw new ArithmeticException("Max available Z index value reached");

        if (params.getZ() != null) {
            if (params.getZ() == Integer.MAX_VALUE)
                throw new IllegalArgumentException("Z index value is too big");

            shift(params.getZ());

            if (params.getZ() >= nextMaxIndex)
                nextMaxIndex = params.getZ() + 1;
        }

        // todo(kulikov): mapper
        return widgetRepository.save(new Widget2(
            UUID.randomUUID(),
            params.getZ() == null ? nextMaxIndex++ : params.getZ(),
            params.getCenterX(),
            params.getCenterY(),
            params.getWidth(),
            params.getHeight(),
            null
        ));
    }

    public Optional<Widget2> findById(UUID id) {
        return widgetRepository.findById(id);
    }

    public Page<Widget2> findRange(int page, int size) {
        var pageRequest = PageRequest.of(page, size, Sort.by("z"));

        return widgetRepository.findAll(pageRequest);
    }

    @Synchronized
    public Widget2 update(UUID id, UpdateWidgetParams dto) {
        var currentWidget = widgetRepository.findById(id);

        if (currentWidget.isEmpty())
            throw new WidgetNotFoundException(String.format("Failed to find widget with id %s", id));

        if (dto.getZ() != null) {
            if (dto.getZ() == Integer.MAX_VALUE)
                throw new IllegalArgumentException("Z index value is too big");

            if (currentWidget.get().getZ() > dto.getZ())
                shift(dto.getZ(), currentWidget.get().getZ());
            else {
                shift(dto.getZ());
            }

            if (dto.getZ() >= nextMaxIndex)
                nextMaxIndex = dto.getZ() + 1;
        }

        return widgetRepository.save(new Widget2(
            id,
            dto.getZ() == null ? currentWidget.get().getZ() : dto.getZ(),
            dto.getCenterX() == null ? currentWidget.get().getCenterX() : dto.getCenterX(),
            dto.getCenterY() == null ? currentWidget.get().getCenterY() : dto.getCenterY(),
            dto.getWidth() == null ? currentWidget.get().getWidth() : dto.getWidth(),
            dto.getHeight() == null ? currentWidget.get().getHeight() : dto.getHeight(),
            null));
    }

    @Synchronized
    public void delete(UUID id) {
        try {
            widgetRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exc) {
            throw new WidgetNotFoundException(exc.getMessage());
        }
    }

    void shift(int startIndex) {
        this.shift(startIndex, Integer.MAX_VALUE - 1);
    }

    void shift(int startIndex, int endIndex) {
        var getSwapItemResult = widgetRepository.findByZ(startIndex);

        for (var i = startIndex + 1; getSwapItemResult.isPresent() && i <= endIndex; i++) {
            var tempItem = widgetRepository.findByZ(i);

            widgetRepository.save(new Widget2(
                getSwapItemResult.get().getId(),
                i,
                getSwapItemResult.get().getCenterX(),
                getSwapItemResult.get().getCenterY(),
                getSwapItemResult.get().getWidth(),
                getSwapItemResult.get().getHeight(),
                null
            ));

            getSwapItemResult = tempItem;

            if (i >= nextMaxIndex)
                nextMaxIndex = i;
        }
    }
}
