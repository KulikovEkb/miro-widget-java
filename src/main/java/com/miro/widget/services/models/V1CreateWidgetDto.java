package com.miro.widget.services.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1CreateWidgetDto {
    @NonNull
    Integer middleX;

    @NonNull
    Integer middleY;

    Integer zIndex;

    @NonNull
    Integer width;

    @NonNull
    Integer height;
}
