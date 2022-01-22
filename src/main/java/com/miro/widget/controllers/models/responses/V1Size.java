package com.miro.widget.controllers.models.responses;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1Size {
    @NonNull
    Integer width;

    @NonNull
    Integer height;
}
