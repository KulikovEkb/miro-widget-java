package com.miro.widget.service.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1CoordinatesDto {
    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;
}
