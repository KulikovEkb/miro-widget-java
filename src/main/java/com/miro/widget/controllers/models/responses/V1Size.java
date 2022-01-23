package com.miro.widget.controllers.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
public class V1Size {
    @NotNull
    @Positive
    @Max(Integer.MAX_VALUE)
    @Schema(
        name = "width",
        example = "100",
        description = "width of the widget")
    Integer width;

    @NotNull
    @Positive
    @Max(Integer.MAX_VALUE)
    @Schema(
        name = "height",
        example = "200",
        description = "height of the widget")
    Integer height;
}
