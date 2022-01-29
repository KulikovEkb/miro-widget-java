package com.miro.widget.service.models.widget;

import lombok.*;

@Data
@AllArgsConstructor
public class Coordinates {
    @NonNull
    Integer centerX;

    @NonNull
    Integer centerY;
}
