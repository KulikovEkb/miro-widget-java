package com.miro.widget.service.models;

import lombok.Value;

import java.util.List;

@Value
public class V1WidgetRangeDto {
    int totalWidgetsCount;

    int totalPagesCount;

    List<V1WidgetDto> widgets;
}
