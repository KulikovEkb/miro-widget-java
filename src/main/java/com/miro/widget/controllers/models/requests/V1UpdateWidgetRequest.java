package com.miro.widget.controllers.models.requests;

import lombok.Value;

@Value
public class V1UpdateWidgetRequest {
    Integer centerX;

    Integer centerY;

    Integer zIndex;

    Integer width;

    Integer height;
}
