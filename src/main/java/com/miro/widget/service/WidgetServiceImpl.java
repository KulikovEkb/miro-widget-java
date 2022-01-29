package com.miro.widget.service;

import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import com.miro.widget.service.models.widget.Widget;
import com.miro.widget.service.repositories.WidgetRepository;
import com.miro.widget.service.repositories.models.params.InsertWidgetParams;
import com.miro.widget.service.models.WidgetRange;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import result.PlainResult;
import result.Result;
import result.errors.Error;

import java.util.UUID;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WidgetServiceImpl implements WidgetService {
    private final WidgetRepository widgetRepository;

    private int nextMaxIndex;

    @Autowired
    public WidgetServiceImpl(WidgetRepository widgetRepository) {
        nextMaxIndex = 0;
        this.widgetRepository = widgetRepository;
    }

    @Synchronized
    public Result<Widget> create(CreateWidgetParams dto) {
        if (nextMaxIndex == Integer.MAX_VALUE)
            return Result.Fail(new Error("Max available Z index value reached"));

        if (dto.getZ() != null) {
            if (dto.getZ() == Integer.MAX_VALUE)
                return Result.Fail(new Error("Z index value is too big"));

            shift(dto.getZ());

            if (dto.getZ() >= nextMaxIndex)
                nextMaxIndex = dto.getZ() + 1;
        }

        return widgetRepository.insert(new InsertWidgetParams(
            dto.getZ() == null ? nextMaxIndex++ : dto.getZ(),
            dto.getCenterX(),
            dto.getCenterY(),
            dto.getWidth(),
            dto.getHeight()
        ));
    }

    public Result<Widget> getById(UUID id) {
        return widgetRepository.getById(id);
    }

    public Result<WidgetRange> getRange(int page, int size) {
        return widgetRepository.getRange(page, size);
    }

    @Synchronized
    public Result<Widget> update(UUID id, UpdateWidgetParams dto) {
        if (dto.getZ() != null) {
            if (dto.getZ() == Integer.MAX_VALUE)
                return Result.Fail(new Error("Z index value is too big"));

            var getCurrentWidgetResult = widgetRepository.getById(id);

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

        return widgetRepository.update(new com.miro.widget.service.repositories.models.params.UpdateWidgetParams(
            id, dto.getZ(), dto.getCenterX(), dto.getCenterY(), dto.getWidth(), dto.getHeight()));
    }

    @Synchronized
    public PlainResult delete(UUID id) {
        return widgetRepository.delete(id);
    }

    void shift(int startIndex) {
        this.shift(startIndex, Integer.MAX_VALUE - 1);
    }

    void shift(int startIndex, int endIndex) {
        var getSwapItemResult = widgetRepository.getByZIndex(startIndex);

        for (var i = startIndex + 1; getSwapItemResult.isSucceed() && i <= endIndex; i++) {
            var tempItem = widgetRepository.getByZIndex(i);

            widgetRepository.update(new com.miro.widget.service.repositories.models.params.UpdateWidgetParams(getSwapItemResult.getValue().getId(), i));

            getSwapItemResult = tempItem;

            if (i >= nextMaxIndex)
                nextMaxIndex = i;
        }
    }
}
