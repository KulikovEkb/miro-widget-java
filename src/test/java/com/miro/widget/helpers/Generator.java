package com.miro.widget.helpers;

import com.miro.widget.controllers.models.requests.V1CreateWidgetRequest;
import com.miro.widget.controllers.models.requests.V1UpdateWidgetRequest;
import com.miro.widget.service.models.*;
import com.miro.widget.service.repositories.models.V1InsertWidgetModel;
import com.miro.widget.service.repositories.models.V1UpdateWidgetModel;
import org.apache.commons.lang3.RandomUtils;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Generator {
    public static V1CreateWidgetDto generateV1CreateWidgetDto() {
        return generateV1CreateWidgetDto(RandomUtils.nextInt());
    }

    public static V1CreateWidgetDto generateV1CreateWidgetDto(Integer z) {
        return new V1CreateWidgetDto(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            z,
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static V1WidgetDto generateV1WidgetDto() {
        return new V1WidgetDto(
            UUID.randomUUID(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            new V1CoordinatesDto(RandomUtils.nextInt(), RandomUtils.nextInt()),
            new V1SizeDto(RandomUtils.nextInt(1, Integer.MAX_VALUE), RandomUtils.nextInt(1, Integer.MAX_VALUE)),
            ZonedDateTime.now());
    }

    public static V1WidgetDto convertFromV1WidgetDtoToV1WidgetDto(V1WidgetDto existing, Integer z) {
        return new V1WidgetDto(
            existing.getId(), z, existing.getCoordinates(), existing.getSize(), existing.getUpdatedAt());
    }

    public static V1WidgetDto convertToWidgetDtoFromCreationDto(V1CreateWidgetDto dto) {
        return new V1WidgetDto(
            UUID.randomUUID(),
            dto.getZ(),
            new V1CoordinatesDto(dto.getCenterX(), dto.getCenterY()),
            new V1SizeDto(dto.getWidth(), dto.getHeight()),
            ZonedDateTime.now());
    }

    public static V1UpdateWidgetDto generateV1UpdateWidgetDto() {
        return new V1UpdateWidgetDto(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static V1UpdateWidgetDto generateV1UpdateWidgetDto(Integer z) {
        return new V1UpdateWidgetDto(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            z,
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static V1WidgetDto convertToWidgetDtoFromUpdatingDto(UUID id, V1UpdateWidgetDto dto) {
        return new V1WidgetDto(
            id,
            dto.getZ(),
            new V1CoordinatesDto(dto.getCenterX(), dto.getCenterY()),
            new V1SizeDto(dto.getWidth(), dto.getHeight()),
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

    public static V1WidgetDto convertToDtoFromCreateRequest(UUID id, V1CreateWidgetRequest request) {
        return new V1WidgetDto(
            id,
            request.getZ(),
            new V1CoordinatesDto(request.getCenterX(), request.getCenterY()),
            new V1SizeDto(request.getWidth(), request.getHeight()),
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

    public static V1WidgetDto convertToDtoFromUpdateRequest(UUID id, V1UpdateWidgetRequest request) {
        return new V1WidgetDto(
            id,
            request.getZ(),
            new V1CoordinatesDto(request.getCenterX(), request.getCenterY()),
            new V1SizeDto(request.getWidth(), request.getHeight()),
            ZonedDateTime.now());
    }

    public static V1InsertWidgetModel generateV1InsertWidgetModel() {
        return new V1InsertWidgetModel(
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    public static V1UpdateWidgetModel generateV1UpdateWidgetModel(UUID id) {
        return new V1UpdateWidgetModel(
            id,
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }
}
