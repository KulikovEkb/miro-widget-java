package com.miro.widget.service.models;

import com.miro.widget.service.models.widget.Widget;
import lombok.Value;

import java.util.List;

@Value
public class WidgetRange {
    int totalWidgetsCount;

    int totalPagesCount;

    List<Widget> widgets;
}
