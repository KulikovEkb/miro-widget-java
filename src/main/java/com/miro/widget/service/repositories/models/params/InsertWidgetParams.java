package com.miro.widget.service.repositories.models.params;

import lombok.NonNull;
import lombok.Value;

@Value
public class InsertWidgetParams {
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

