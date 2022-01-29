package com.miro.widget.service.models.widget;

import lombok.*;

@Data
@AllArgsConstructor
public class Size {
    @NonNull
    Integer width;

    @NonNull
    Integer height;
}
