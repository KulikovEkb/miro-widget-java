package com.miro.widget.service.repositories.models;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class V1UpdateWidgetModel {
    @NonNull
    UUID id;

    Integer z;

    Integer centerX;

    Integer centerY;

    Integer width;

    Integer height;

    public V1UpdateWidgetModel(@NonNull UUID id, @NonNull Integer z) {
        this.id = id;
        this.z = z;
    }
}

