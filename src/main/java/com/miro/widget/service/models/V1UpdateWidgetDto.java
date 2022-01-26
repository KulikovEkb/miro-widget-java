package com.miro.widget.service.models;

import lombok.*;

@Data
@AllArgsConstructor
public class V1UpdateWidgetDto {
    Integer centerX;

    Integer centerY;

    Integer z;

    Integer width;

    Integer height;

    public V1UpdateWidgetDto(@NonNull Integer z) {
        this.z = z;
    }
}
