package com.miro.widget.service.models.params;

import lombok.*;

@Data
@AllArgsConstructor
public class UpdateWidgetParams {
    Integer centerX;

    Integer centerY;

    Integer z;

    Integer width;

    Integer height;

    public UpdateWidgetParams(@NonNull Integer z) {
        this.z = z;
    }
}
