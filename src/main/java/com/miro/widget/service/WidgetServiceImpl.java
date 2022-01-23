package com.miro.widget.service;

import com.miro.widget.service.models.V1CreateWidgetDto;
import com.miro.widget.service.models.V1UpdateWidgetDto;
import com.miro.widget.service.models.V1WidgetDto;
import com.miro.widget.service.repositories.WidgetRepository;
import com.miro.widget.service.repositories.models.V1InsertWidgetModel;
import com.miro.widget.service.repositories.models.V1UpdateWidgetModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class WidgetServiceImpl implements WidgetService {
    private static int nextMaxIndex;
    private final WidgetRepository widgetRepository;

    @Autowired
    WidgetServiceImpl(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    public V1WidgetDto v1Create(V1CreateWidgetDto dto) {
        if (dto.getZIndex() != null)
            shift(dto.getZIndex());

        return widgetRepository.v1Insert(new V1InsertWidgetModel(
            dto.getZIndex() == null ? nextMaxIndex++ : dto.getZIndex(),
            dto.getCenterX(),
            dto.getCenterY(),
            dto.getWidth(),
            dto.getHeight()
        ));
    }

    public V1WidgetDto v1GetById(UUID id) {
        return widgetRepository.v1GetById(id);
    }

    public List<V1WidgetDto> v1GetAll() {
        return widgetRepository.v1GetAll();
    }

    public V1WidgetDto v1Update(UUID id, V1UpdateWidgetDto dto) {
        // todo(kulikov): fix that
        //if (dto.ZIndex.HasValue)
        //{
        //    var currentWidget = await _widgetRepository.V1GetById(dto.Id);
        //    await Shift(dto.ZIndex.Value, currentWidget.Value.ZIndex);
        //}
        if (dto.getZIndex() != null) {
            var currentWidget = widgetRepository.v1GetById(id);

            widgetRepository.v1Delete(id);

            shift(dto.getZIndex());

            return widgetRepository.v1Update(new V1UpdateWidgetModel(
                id, dto.getZIndex(), dto.getCenterX(), dto.getCenterY(), dto.getWidth(), dto.getHeight()));
        }

        return widgetRepository.v1Update(new V1UpdateWidgetModel(
            id, dto.getZIndex(), dto.getCenterX(), dto.getCenterY(), dto.getWidth(), dto.getHeight()));
    }

    public void v1Delete(UUID id) {
        widgetRepository.v1Delete(id);
    }

    void shift(int startIndex) {
        this.shift(startIndex, Integer.MAX_VALUE);
    }

    void shift(int startIndex, int endIndex) {
        var swapItem = widgetRepository.v1GetByZIndex(startIndex);

        for (var i = startIndex + 1; swapItem != null && i <= endIndex; i++) {
            var tempItem = widgetRepository.v1GetByZIndex(i);

            widgetRepository.v1Update(new V1UpdateWidgetModel(swapItem.getId(), i));

            swapItem = tempItem;

            if (i >= nextMaxIndex)
                nextMaxIndex = i;
        }
    }
}
