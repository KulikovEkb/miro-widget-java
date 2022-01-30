package com.miro.widget.service;

import com.miro.widget.exceptions.WidgetNotFoundException;
import com.miro.widget.service.models.Widget2;
import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface WidgetService2 {
    Widget2 create(CreateWidgetParams dto);

    Optional<Widget2> findById(UUID id);

    Page<Widget2> findRange(int page, int size);

    Widget2 update(UUID id, UpdateWidgetParams dto) throws WidgetNotFoundException;

    void delete(UUID id);
}
