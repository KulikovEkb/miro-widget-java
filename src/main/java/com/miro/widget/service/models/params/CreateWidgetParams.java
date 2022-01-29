package com.miro.widget.service.models.params;

import lombok.*;

@Data
@AllArgsConstructor
public class CreateWidgetParams {
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
