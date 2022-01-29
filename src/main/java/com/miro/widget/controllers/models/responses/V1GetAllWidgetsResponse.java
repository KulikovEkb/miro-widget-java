package com.miro.widget.controllers.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class V1GetAllWidgetsResponse {
    @Schema(name = "totalWidgetsCount", description = "overall quantity of widgets")
    int totalWidgetsCount;

    @Schema(name = "totalPagesCount", description = "overall quantity of pages for this size")
    int totalPagesCount;

    @NotNull
    @Schema(name = "widgets", description = "range of widgets")
    Iterable<V1GetRangeItem> Widgets;
}

