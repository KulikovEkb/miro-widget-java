package com.miro.widget.service.models.widget;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
public class Widget {
    @NonNull
    UUID id;

    @NonNull
    Integer z;

    @NonNull
    Coordinates coordinates;

    @NonNull
    Size size;

    @NonNull
    ZonedDateTime updatedAt;
}

