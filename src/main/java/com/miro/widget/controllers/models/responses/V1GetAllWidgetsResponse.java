package com.miro.widget.controllers.models.responses;

import lombok.NonNull;
import lombok.Value;

@Value
public class V1GetAllWidgetsResponse {
    @NonNull
    Iterable<V1GetAllWidgetsItem> Widgets;
}

