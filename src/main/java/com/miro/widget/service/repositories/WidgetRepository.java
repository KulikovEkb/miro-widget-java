package com.miro.widget.service.repositories;

import com.miro.widget.service.models.widget.Widget;
import com.miro.widget.service.repositories.models.params.InsertWidgetParams;
import com.miro.widget.service.repositories.models.params.UpdateWidgetParams;
import com.miro.widget.service.models.WidgetRange;
import result.PlainResult;
import result.Result;

import java.util.UUID;

public interface WidgetRepository {
    Result<Widget> insert(InsertWidgetParams model);

    Result<Widget> getById(UUID id);

    Result<Widget> getByZIndex(int z);

    Result<WidgetRange> getRange(int page, int size);

    Result<Widget> update(UpdateWidgetParams model);

    PlainResult delete(UUID id);

    PlainResult deleteAll();
}
