package com.miro.widget.service.repositories.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1InsertWidgetModel {
    @NonNull
    Integer z;

    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;

    @NonNull
    Integer width;

    @NonNull
    Integer height;
}

