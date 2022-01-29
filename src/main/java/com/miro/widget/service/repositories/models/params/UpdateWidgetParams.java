package com.miro.widget.service.repositories.models.params;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateWidgetParams {
    @NonNull
    UUID id;

    Integer z;

    Integer centerX;

    Integer centerY;

    Integer width;

    Integer height;

    public UpdateWidgetParams(@NonNull UUID id, @NonNull Integer z) {
        this.id = id;
        this.z = z;
    }
}

