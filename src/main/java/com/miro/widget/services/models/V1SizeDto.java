package com.miro.widget.services.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1SizeDto {
    @NonNull
    Integer width;

    @NonNull
    Integer height;
}
