package com.miro.widget.service;

import com.miro.widget.exceptions.WidgetNotFoundException;
import com.miro.widget.service.models.Widget;
import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface WidgetService {
    Widget create(CreateWidgetParams dto);

    Optional<Widget> findById(UUID id);

    Page<Widget> findRange(int page, int size);

    Widget update(UUID id, UpdateWidgetParams dto) throws WidgetNotFoundException;

    void delete(UUID id);
}
