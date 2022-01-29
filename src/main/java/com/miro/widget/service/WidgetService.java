package com.miro.widget.service;

import com.miro.widget.service.models.*;
import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import com.miro.widget.service.models.widget.Widget;
import result.PlainResult;
import result.Result;

import java.util.UUID;

public interface WidgetService {
    Result<Widget> create(CreateWidgetParams dto);

    Result<Widget> getById(UUID id);

    Result<WidgetRange> getRange(int page, int size);

    Result<Widget> update(UUID id, UpdateWidgetParams dto);

    PlainResult delete(UUID id);
}
