package com.miro.widget.controllers.models.requests;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1CreateWidgetRequest {
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
