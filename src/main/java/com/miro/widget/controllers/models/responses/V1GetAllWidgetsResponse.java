package com.miro.widget.controllers.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class V1GetAllWidgetsResponse {
    @NotNull
    @Schema(name = "widgets", description = "collection of all widgets")
    Iterable<V1GetAllWidgetsItem> Widgets;
}

