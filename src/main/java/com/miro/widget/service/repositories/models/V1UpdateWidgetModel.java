package com.miro.widget.service.repositories.models;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class V1UpdateWidgetModel {
    @NonNull
    UUID id;

    Integer zIndex;

    Integer centerX;

    Integer centerY;

    Integer width;

    Integer height;

    public V1UpdateWidgetModel(@NonNull UUID id, @NonNull Integer zIndex) {
        this.id = id;
        this.zIndex = zIndex;
    }
}

