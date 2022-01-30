package com.miro.widget.controllers.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Value
public class V2WidgetDto {
    @NotNull
    @Schema(
        name = "id",
        example = "2ae26a87-cc91-4ac2-9948-f788cb10c2d8",
        description = "unique ID of the widget")
    UUID id;

    @NotNull
    @Min(Integer.MIN_VALUE)
    @Max(Integer.MAX_VALUE)
    @Schema(
        name = "z",
        example = "10",
        description = "unique sequence common to all widgets that determines the order of widgets " +
            "(regardless of their coordinates)")
    Integer z;

    @NotNull
    @Schema(name = "coordinates", description = "coordinates of the widget")
    V1Coordinates coordinates;

    @NotNull
    @Schema(name = "size", description = "size of the widget")
    V1Size size;

    @NotNull
    @Schema(name = "updatedAt", description = "last time the widget was updated in ZonedDateTime format")
    ZonedDateTime updatedAt;
}
