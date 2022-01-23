package com.miro.widget.service.repositories;

import com.miro.widget.service.models.V1WidgetDto;
import com.miro.widget.service.repositories.models.V1InsertWidgetModel;
import com.miro.widget.service.repositories.models.V1UpdateWidgetModel;
import result.PlainResult;
import result.Result;

import java.util.List;
import java.util.UUID;

public interface WidgetRepository {
    Result<V1WidgetDto> v1Insert(V1InsertWidgetModel model);

    Result<V1WidgetDto> v1GetById(UUID id);

    Result<V1WidgetDto> v1GetByZIndex(int z);

    Result<List<V1WidgetDto>> v1GetAll();

    Result<V1WidgetDto> v1Update(V1UpdateWidgetModel model);

    PlainResult v1Delete(UUID id);

    PlainResult v1DeleteAll();
}
