package com.miro.widget.controllers.models.responses;

import lombok.NonNull;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
public class V1CreateWidgetResponse {
    @NonNull
    UUID id;

    @NonNull
    Integer zIndex;

    @NonNull
    V1Coordinates coordinates;

    @NonNull
    V1Size size;

    @NonNull
    ZonedDateTime updatedAt;
}

