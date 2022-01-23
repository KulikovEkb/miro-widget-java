package com.miro.widget.controllers.models.requests;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1CreateWidgetRequest {
    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;

    Integer zIndex;

    @NonNull
    Integer width;

    @NonNull
    Integer height;
}
