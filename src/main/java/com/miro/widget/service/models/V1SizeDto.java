package com.miro.widget.service.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1SizeDto {
    @NonNull
    Integer width;

    @NonNull
    Integer height;
}
