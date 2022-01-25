package com.miro.widget.controllers.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Value
public class V1UpdateWidgetRequest {
    @Min(Integer.MIN_VALUE)
    @Max(Integer.MAX_VALUE)
    @Schema(
        name = "centerX",
        example = "1",
        description = "X coordinate of the center of the widget in a Cartesian coordinate system")
    Integer centerX;

    @Min(Integer.MIN_VALUE)
    @Max(Integer.MAX_VALUE)
    @Schema(
        name = "centerY",
        example = "-1",
        description = "Y coordinate of the center of the widget in a Cartesian coordinate system")
    Integer centerY;

    @Min(Integer.MIN_VALUE)
    @Max(Integer.MAX_VALUE - 1)
    @Schema(
        name = "z",
        example = "10",
        description = "unique sequence common to all widgets that determines the order of widgets " +
            "(regardless of their coordinates)")
    Integer z;

    @Min(1)
    @Max(Integer.MAX_VALUE)
    @Schema(
        name = "width",
        example = "100",
        description = "width of the widget")
    Integer width;

    @Min(1)
    @Max(Integer.MAX_VALUE)
    @Schema(
        name = "height",
        example = "200",
        description = "height of the widget")
    Integer height;
}
