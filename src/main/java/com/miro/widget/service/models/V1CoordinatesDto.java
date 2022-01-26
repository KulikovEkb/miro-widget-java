package com.miro.widget.service.models;

import lombok.*;

@Data
@AllArgsConstructor
public class V1CoordinatesDto {
    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;
}
