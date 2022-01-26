package com.miro.widget.service.models;

import lombok.*;

@Data
@AllArgsConstructor
public class V1CreateWidgetDto {
    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;

    Integer z;

    @NonNull
    Integer width;

    @NonNull
    Integer height;
}
