package com.miro.widget.service.repositories.models;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class V1WidgetEntity {
    @NonNull
    UUID id;

    @NonNull
    Integer zIndex;

    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;

    @NonNull
    Integer width;

    @NonNull
    Integer height;

    @NonNull
    ZonedDateTime updatedAt;
}
