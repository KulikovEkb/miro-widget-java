package com.miro.widget.service;

import com.miro.widget.service.models.*;

import java.util.List;
import java.util.UUID;

public interface WidgetService {
    V1WidgetDto v1Create(V1CreateWidgetDto dto);

    V1WidgetDto v1GetById(UUID id);

    List<V1WidgetDto> v1GetAll();

    V1WidgetDto v1Update(UUID id, V1UpdateWidgetDto dto);

    void v1Delete(UUID id);
}
