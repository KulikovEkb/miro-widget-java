package com.miro.widget.services;

import com.miro.widget.services.models.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface WidgetService {
    V1WidgetDto v1Create(V1CreateWidgetDto dto);

    V1WidgetDto v1GetById(UUID id);

    List<V1WidgetDto> v1GetAll();

    V1WidgetDto v1Update(UUID id, V1UpdateWidgetDto dto);

    void v1Delete(UUID id);
}
