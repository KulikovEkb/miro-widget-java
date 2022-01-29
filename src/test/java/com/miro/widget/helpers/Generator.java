package com.miro.widget.helpers;

import com.miro.widget.controllers.models.requests.V1CreateWidgetRequest;
import com.miro.widget.controllers.models.requests.V1UpdateWidgetRequest;
import com.miro.widget.service.models.params.CreateWidgetParams;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import com.miro.widget.service.models.widget.Coordinates;
import com.miro.widget.service.models.widget.Size;
import com.miro.widget.service.models.widget.Widget;
import com.miro.widget.service.repositories.models.params.InsertWidgetParams;
import org.apache.commons.lang3.RandomUtils;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Generator {
    public static CreateWidgetParams generateV1CreateWidgetDto() {
        return generateV1CreateWidgetDto(RandomUtils.nextInt());
    }

    public static CreateWidgetParams generateV1CreateWidgetDto(Integer z) {
        return new CreateWidgetParams(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            z,
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static Widget generateV1WidgetDto() {
        return new Widget(
            UUID.randomUUID(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            new Coordinates(RandomUtils.nextInt(), RandomUtils.nextInt()),
            new Size(RandomUtils.nextInt(1, Integer.MAX_VALUE), RandomUtils.nextInt(1, Integer.MAX_VALUE)),
            ZonedDateTime.now());
    }

    public static Widget convertFromV1WidgetDtoToV1WidgetDto(Widget existing, Integer z) {
        return new Widget(
            existing.getId(), z, existing.getCoordinates(), existing.getSize(), existing.getUpdatedAt());
    }

    public static Widget convertToWidgetDtoFromCreationDto(CreateWidgetParams dto) {
        return new Widget(
            UUID.randomUUID(),
            dto.getZ(),
            new Coordinates(dto.getCenterX(), dto.getCenterY()),
            new Size(dto.getWidth(), dto.getHeight()),
            ZonedDateTime.now());
    }

    public static UpdateWidgetParams generateV1UpdateWidgetDto() {
        return new UpdateWidgetParams(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static UpdateWidgetParams generateV1UpdateWidgetDto(Integer z) {
        return new UpdateWidgetParams(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            z,
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static Widget convertToWidgetDtoFromUpdatingDto(UUID id, UpdateWidgetParams dto) {
        return new Widget(
            id,
            dto.getZ(),
            new Coordinates(dto.getCenterX(), dto.getCenterY()),
            new Size(dto.getWidth(), dto.getHeight()),
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

    public static Widget convertToDtoFromCreateRequest(UUID id, V1CreateWidgetRequest request) {
        return new Widget(
            id,
            request.getZ(),
            new Coordinates(request.getCenterX(), request.getCenterY()),
            new Size(request.getWidth(), request.getHeight()),
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

    public static Widget convertToDtoFromUpdateRequest(UUID id, V1UpdateWidgetRequest request) {
        return new Widget(
            id,
            request.getZ(),
            new Coordinates(request.getCenterX(), request.getCenterY()),
            new Size(request.getWidth(), request.getHeight()),
            ZonedDateTime.now());
    }

    public static InsertWidgetParams generateV1InsertWidgetModel() {
        return new InsertWidgetParams(
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static com.miro.widget.service.repositories.models.params.UpdateWidgetParams generateV1UpdateWidgetModel(UUID id) {
        return new com.miro.widget.service.repositories.models.params.UpdateWidgetParams(
            id,
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }
}
