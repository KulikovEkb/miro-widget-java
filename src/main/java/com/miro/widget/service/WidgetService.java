package com.miro.widget.service;

import com.miro.widget.service.models.*;
import result.PlainResult;
import result.Result;

import java.util.List;
import java.util.UUID;

public interface WidgetService {
    Result<V1WidgetDto> v1Create(V1CreateWidgetDto dto);

    Result<V1WidgetDto> v1GetById(UUID id);

    Result<List<V1WidgetDto>> v1GetAll();

    Result<V1WidgetDto> v1Update(UUID id, V1UpdateWidgetDto dto);

    PlainResult v1Delete(UUID id);
}
