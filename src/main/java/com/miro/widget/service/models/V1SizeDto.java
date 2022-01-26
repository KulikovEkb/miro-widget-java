package com.miro.widget.service.models;

import lombok.*;

@Data
@AllArgsConstructor
public class V1SizeDto {
    @NonNull
    Integer width;

    @NonNull
    Integer height;
}
