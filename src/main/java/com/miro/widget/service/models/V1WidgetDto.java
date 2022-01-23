package com.miro.widget.service.models;

import lombok.NonNull;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
public class V1WidgetDto {
    @NonNull
    UUID id;

    @NonNull
    Integer zIndex;

    @NonNull
    V1CoordinatesDto coordinates;

    @NonNull
    V1SizeDto size;

    @NonNull
    ZonedDateTime updatedAt;
}

