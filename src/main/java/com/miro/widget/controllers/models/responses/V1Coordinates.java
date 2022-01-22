package com.miro.widget.controllers.models.responses;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1Coordinates {
    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;
}
