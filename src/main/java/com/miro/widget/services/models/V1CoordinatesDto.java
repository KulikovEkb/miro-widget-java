package com.miro.widget.services.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1CoordinatesDto {
    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;
}
