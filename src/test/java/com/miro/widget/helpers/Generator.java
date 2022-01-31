package com.miro.widget.helpers;

import com.miro.widget.controllers.models.requests.V1CreateWidgetRequest;
import com.miro.widget.controllers.models.requests.V1UpdateWidgetRequest;
import com.miro.widget.service.models.Widget;
import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import org.apache.commons.lang3.RandomUtils;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Generator {
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

    public static Widget generateWidget() {
        return new Widget(
            UUID.randomUUID(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            ZonedDateTime.now());
    }

    public static Widget convertFromWidgetToWidget(Widget existing, Integer z) {
        return new Widget(
            existing.getId(),
            z,
            existing.getCenterX(),
            existing.getCenterY(),
            existing.getWidth(),
            existing.getHeight(),
            existing.getUpdatedAt());
    }

    public static Widget convertToWidgetFromCreationParams(CreateWidgetParams dto) {
        return new Widget(
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

    public static Widget convertToWidgetFromUpdatingDto(UUID id, UpdateWidgetParams dto) {
        return new Widget(
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

    public static Widget convertToWidgetFromCreateRequest(UUID id, V1CreateWidgetRequest request) {
        return new Widget(
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

    public static Widget convertToWidgetFromUpdateRequest(UUID id, V1UpdateWidgetRequest request) {
        return new Widget(
            id,
            request.getZ(),
            request.getCenterX(),
            request.getCenterY(),
            request.getWidth(),
            request.getHeight(),
            ZonedDateTime.now());
    }

    public static Widget generateWidgetForInsert() {
        return new Widget(
            UUID.randomUUID(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            null);
    }

    public static Widget generateWidgetForUpdate(UUID id) {
        return new Widget(
            id,
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            null);
    }
}
