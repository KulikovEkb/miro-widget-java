package com.miro.widget.controllers.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
public class V1Coordinates {
    @NotNull
    @Min(Integer.MIN_VALUE)
    @Max(Integer.MAX_VALUE)
    @Schema(
        name = "centerX",
        example = "1",
        description = "X coordinate of the center of the widget in a Cartesian coordinate system")
    Integer centerX;

    @NotNull
    @Min(Integer.MIN_VALUE)
    @Max(Integer.MAX_VALUE)
    @Schema(
        name = "centerY",
        example = "-1",
        description = "Y coordinate of the center of the widget in a Cartesian coordinate system")
    Integer centerY;
}
