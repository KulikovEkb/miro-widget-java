package com.miro.widget.service;

import com.miro.widget.exceptions.WidgetNotFoundException;
import com.miro.widget.mappers.WidgetsMapper;
import com.miro.widget.service.models.Widget;
import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import com.miro.widget.service.repositories.WidgetRepository;
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
public class WidgetServiceImpl implements WidgetService {
    private final WidgetsMapper mapper;
    private final WidgetRepository widgetRepository;

    private int nextMaxIndex;

    @Autowired
    public WidgetServiceImpl(WidgetsMapper mapper, WidgetRepository widgetRepository) {
        nextMaxIndex = 0;
        this.mapper = mapper;
        this.widgetRepository = widgetRepository;
    }

    @Synchronized
    public Widget create(CreateWidgetParams params) {
        if (nextMaxIndex == Integer.MAX_VALUE)
            throw new ArithmeticException("Max available Z index value reached");

        if (params.getZ() != null) {
            if (params.getZ() == Integer.MAX_VALUE)
                throw new IllegalArgumentException("Z index value is too big");

            shift(params.getZ());

            if (params.getZ() >= nextMaxIndex)
                nextMaxIndex = params.getZ() + 1;
        }

        return widgetRepository.save(mapper.creationParamsToBllModel(
            params, params.getZ() == null ? nextMaxIndex++ : params.getZ()));
    }

    public Optional<Widget> findById(UUID id) {
        return widgetRepository.findById(id);
    }

    public Page<Widget> findRange(int page, int size) {
        var pageRequest = PageRequest.of(page, size, Sort.by("z"));

        return widgetRepository.findAll(pageRequest);
    }

    @Synchronized
    public Widget update(UUID id, UpdateWidgetParams params) {
        var currentWidget = widgetRepository.findById(id);

        if (currentWidget.isEmpty())
            throw new WidgetNotFoundException(String.format("Failed to find widget with id %s", id));

        if (params.getZ() != null) {
            if (params.getZ() == Integer.MAX_VALUE)
                throw new IllegalArgumentException("Z index value is too big");

            if (currentWidget.get().getZ() > params.getZ())
                shift(params.getZ(), currentWidget.get().getZ());
            else {
                shift(params.getZ());
            }

            if (params.getZ() >= nextMaxIndex)
                nextMaxIndex = params.getZ() + 1;
        }

        return widgetRepository.save(mapper.updatingParamsToBllModel(params, currentWidget.get()));
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

            widgetRepository.save(mapper.toWidgetWithNewZ(getSwapItemResult.get(), i));

            getSwapItemResult = tempItem;

            if (i >= nextMaxIndex)
                nextMaxIndex = i;
        }
    }
}
