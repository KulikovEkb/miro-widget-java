package com.miro.widget.service.repositories;

import com.miro.widget.service.models.V1WidgetDto;
import com.miro.widget.service.repositories.models.V1InsertWidgetModel;
import com.miro.widget.service.repositories.models.V1UpdateWidgetModel;

import java.util.List;
import java.util.UUID;

public interface WidgetRepository {
    V1WidgetDto v1Insert(V1InsertWidgetModel model);

    V1WidgetDto v1GetById(UUID id);

    V1WidgetDto v1GetByZIndex(int zIndex);

    List<V1WidgetDto> v1GetAll();

    V1WidgetDto v1Update(V1UpdateWidgetModel model);

    void v1Delete(UUID id);

    void v1DeleteAll();
}
