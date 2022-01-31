package com.miro.widget.helpers;

import com.miro.widget.controllers.models.requests.V1CreateWidgetRequest;
import com.miro.widget.controllers.models.requests.V1UpdateWidgetRequest;
import com.miro.widget.service.models.Widget2;
import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import com.miro.widget.service.models.widget.Coordinates;
import com.miro.widget.service.models.widget.Size;
import com.miro.widget.service.models.widget.Widget;
import org.apache.commons.lang3.RandomUtils;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Generator2 {
    public static CreateWidgetParams generateCreationParams() {
        return generateCreationParams(RandomUtils.nextInt());
    }

    public static CreateWidgetParams generateCreationParams(Integer z) {
        return new CreateWidgetParams(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            z,
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static Widget2 generateWidget() {
        return new Widget2(
            UUID.randomUUID(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            ZonedDateTime.now());
    }

    public static Widget2 convertFromWidgetToWidget(Widget2 existing, Integer z) {
        return new Widget2(
            existing.getId(),
            z,
            existing.getCenterX(),
            existing.getCenterY(),
            existing.getWidth(),
            existing.getHeight(),
            existing.getUpdatedAt());
    }

    public static Widget2 convertToWidgetFromCreationParams(CreateWidgetParams dto) {
        return new Widget2(
            UUID.randomUUID(),
            dto.getZ(),
            dto.getCenterX(),
            dto.getCenterY(),
            dto.getWidth(),
            dto.getHeight(),
            ZonedDateTime.now());
    }

    public static UpdateWidgetParams generateUpdatingParams() {
        return new UpdateWidgetParams(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static UpdateWidgetParams generateUpdatingParams(Integer z) {
        return new UpdateWidgetParams(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            z,
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static Widget2 convertToWidgetFromUpdatingDto(UUID id, UpdateWidgetParams dto) {
        return new Widget2(
            id,
            dto.getZ(),
            dto.getCenterX(),
            dto.getCenterY(),
            dto.getWidth(),
            dto.getHeight(),
            ZonedDateTime.now());
    }

    public static V1CreateWidgetRequest generateV1CreateWidgetRequest() {
        return new V1CreateWidgetRequest(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static Widget2 convertToWidgetFromCreateRequest(UUID id, V1CreateWidgetRequest request) {
        return new Widget2(
            id,
            request.getZ(),
            request.getCenterX(),
            request.getCenterY(),
            request.getWidth(),
            request.getHeight(),
            ZonedDateTime.now());
    }

    public static V1UpdateWidgetRequest generateV1UpdateWidgetRequest() {
        return new V1UpdateWidgetRequest(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static Widget2 convertToWidgetFromUpdateRequest(UUID id, V1UpdateWidgetRequest request) {
        return new Widget2(
            id,
            request.getZ(),
            request.getCenterX(),
            request.getCenterY(),
            request.getWidth(),
            request.getHeight(),
            ZonedDateTime.now());
    }

    public static Widget2 generateWidgetForInsert() {
        return new Widget2(
            UUID.randomUUID(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            null);
    }

    public static Widget2 generateWidgetForUpdate(UUID id) {
        return new Widget2(
            id,
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            null);
    }
}
