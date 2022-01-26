package com.miro.widget.service;

import com.miro.widget.service.models.*;
import com.miro.widget.service.models.V1WidgetRangeDto;
import result.PlainResult;
import result.Result;

import java.util.UUID;

public interface WidgetService {
    Result<V1WidgetDto> v1Create(V1CreateWidgetDto dto);

    Result<V1WidgetDto> v1GetById(UUID id);

    Result<V1WidgetRangeDto> v1GetRange(int page, int size);

    Result<V1WidgetDto> v1Update(UUID id, V1UpdateWidgetDto dto);

    PlainResult v1Delete(UUID id);
}
