package com.miro.widget.services;

import com.miro.widget.services.models.V1CreateWidgetDto;
import com.miro.widget.services.models.V1WidgetDto;
import org.springframework.stereotype.Service;

@Service
public interface WidgetService {
    V1WidgetDto v1Create(V1CreateWidgetDto dto);
}
