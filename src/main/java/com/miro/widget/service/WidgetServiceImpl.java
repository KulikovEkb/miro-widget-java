package com.miro.widget.service;

import com.miro.widget.service.models.V1CreateWidgetDto;
import com.miro.widget.service.models.V1UpdateWidgetDto;
import com.miro.widget.service.models.V1WidgetDto;
import com.miro.widget.service.repositories.WidgetRepository;
import com.miro.widget.service.repositories.models.V1InsertWidgetModel;
import com.miro.widget.service.repositories.models.V1UpdateWidgetModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import result.PlainResult;
import result.Result;
import result.errors.Error;

import java.util.List;
import java.util.UUID;

@Service
public class WidgetServiceImpl implements WidgetService {
    private static int nextMaxIndex;
    private final WidgetRepository widgetRepository;

    @Autowired
    public WidgetServiceImpl(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    public Result<V1WidgetDto> v1Create(V1CreateWidgetDto dto) {
        if (nextMaxIndex == Integer.MAX_VALUE)
            return Result.Fail(new Error("Max available Z index value reached"));

        if (dto.getZ() != null) {
            if (dto.getZ() == Integer.MAX_VALUE)
                return Result.Fail(new Error("Z index value is too big"));

            shift(dto.getZ());

            if (dto.getZ() >= nextMaxIndex)
                nextMaxIndex = dto.getZ() + 1;
        }

        return widgetRepository.v1Insert(new V1InsertWidgetModel(
            dto.getZ() == null ? nextMaxIndex++ : dto.getZ(),
            dto.getCenterX(),
            dto.getCenterY(),
            dto.getWidth(),
            dto.getHeight()
        ));
    }

    public Result<V1WidgetDto> v1GetById(UUID id) {
        return widgetRepository.v1GetById(id);
    }

    public Result<List<V1WidgetDto>> v1GetAll() {
        return widgetRepository.v1GetAll();
    }

    public Result<V1WidgetDto> v1Update(UUID id, V1UpdateWidgetDto dto) {
        if (dto.getZ() != null) {
            if (dto.getZ() == Integer.MAX_VALUE)
                return Result.Fail(new Error("Z index value is too big"));

            var getCurrentWidgetResult = widgetRepository.v1GetById(id);

            if (getCurrentWidgetResult.isFailed())
                return getCurrentWidgetResult;

            if (getCurrentWidgetResult.getValue().getZ() > dto.getZ())
                shift(dto.getZ(), getCurrentWidgetResult.getValue().getZ());
            else {
                shift(dto.getZ());
            }

            if (dto.getZ() >= nextMaxIndex)
                nextMaxIndex = dto.getZ() + 1;
        }

        return widgetRepository.v1Update(new V1UpdateWidgetModel(
            id, dto.getZ(), dto.getCenterX(), dto.getCenterY(), dto.getWidth(), dto.getHeight()));
    }

    public PlainResult v1Delete(UUID id) {
        return widgetRepository.v1Delete(id);
    }

    void shift(int startIndex) {
        this.shift(startIndex, Integer.MAX_VALUE - 1);
    }

    void shift(int startIndex, int endIndex) {
        var getSwapItemResult = widgetRepository.v1GetByZIndex(startIndex);

        for (var i = startIndex + 1; getSwapItemResult.isSucceed() && i <= endIndex; i++) {
            var tempItem = widgetRepository.v1GetByZIndex(i);

            widgetRepository.v1Update(new V1UpdateWidgetModel(getSwapItemResult.getValue().getId(), i));

            getSwapItemResult = tempItem;

            if (i >= nextMaxIndex)
                nextMaxIndex = i;
        }
    }
}
