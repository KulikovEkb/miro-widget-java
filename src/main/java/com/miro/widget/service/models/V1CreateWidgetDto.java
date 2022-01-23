package com.miro.widget.service.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1CreateWidgetDto {
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