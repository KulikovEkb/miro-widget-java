package com.miro.widget.controllers;

import com.miro.widget.controllers.models.requests.*;
import com.miro.widget.controllers.models.responses.*;
import com.miro.widget.controllers.validation.models.ValidationErrorResponse;
import com.miro.widget.mappers.WebAndBllMapper;
import com.miro.widget.service.WidgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import result.errors.NotFoundError;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Validated
@RestController
@RequestMapping(path = "api/v1/widgets", produces = MediaType.APPLICATION_JSON_VALUE)
public class WidgetController {
    private final WebAndBllMapper mapper;
    private final WidgetService widgetService;

    @Autowired
    public WidgetController(WebAndBllMapper mapper, WidgetService widgetService) {
        this.mapper = mapper;
        this.widgetService = widgetService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a widget", description = "Having a set of coordinates, Z-index, width, and height, " +
        "we get a complete widget description in the response. The server generates the identifier. If a Z-index is " +
        "not specified, the widget moves to the foreground (becomes maximum). If the existing Z-index is specified, " +
        "then the new widget shifts widget with the same (and greater if needed) upwards")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", content = @Content(schema = @Schema(implementation = V1CreateWidgetResponse.class))),
        @ApiResponse(
            responseCode = "400", description = "BadRequest", content = @Content(
            schema = @Schema(implementation = ValidationErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<?> v1Create(@RequestBody @NotNull @Valid V1CreateWidgetRequest request) {
        var createWidgetResult = widgetService.v1Create(mapper.v1CreateRequestToDto(request));

        if (createWidgetResult.isFailed())
            return ResponseEntity.internalServerError().body(createWidgetResult.getError());

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createWidgetResult.getValue().getId())
            .toUri();

        return ResponseEntity
            .created(uri)
            .body(mapper.v1DtoToCreationResponse(createWidgetResult.getValue()));
    }

    @GetMapping(path = "{id}")
    @Operation(summary = "Returns the widget by its ID", description = "Returns a complete description of the widget")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", content = @Content(schema = @Schema(implementation = V1GetWidgetByIdResponse.class))),
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(
            schema = @Schema(implementation = ValidationErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NotFound", content = @Content()),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<?> v1GetById(@PathVariable @NotNull UUID id) {
        var getWidgetResult = widgetService.v1GetById(id);

        if (getWidgetResult.hasError(NotFoundError.class))
            return ResponseEntity.notFound().build();

        if (getWidgetResult.isFailed())
            return ResponseEntity.internalServerError().body(getWidgetResult.getError());

        return ResponseEntity.ok(mapper.v1DtoToGetByIdResponse(getWidgetResult.getValue()));
    }

    @GetMapping
    @Operation(summary = "Returns a list of widgets", description = "Returns a list of all widgets sorted " +
        "by Z-index, from smallest to largest")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", content = @Content(schema = @Schema(implementation = V1GetAllWidgetsResponse.class))),
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(
            schema = @Schema(implementation = ValidationErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<?> v1GetAll() {
        var getWidgetsResult = widgetService.v1GetAll();

        if (getWidgetsResult.isFailed())
            return ResponseEntity.internalServerError().body(getWidgetsResult.getError());

        return ResponseEntity.ok(new V1GetAllWidgetsResponse(getWidgetsResult.getValue()
            .stream()
            .map(mapper::v1DtoToGetAllItem)
            .collect(toList())));
    }

    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates the widget by its ID", description = "Returns an updated full description of the\n" +
        "widget. We cannot change the widget id. All changes to widgets must occur atomically.\n" +
        "That is, if we change the XY coordinates of the widget, then we should not get an\n" +
        "intermediate state during concurrent reading. The rules related to the Z-index are the\n" +
        "same as when creating a widget")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", content = @Content(schema = @Schema(implementation = V1UpdateWidgetResponse.class))),
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(
            schema = @Schema(implementation = ValidationErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NotFound", content = @Content()),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<?> v1Update(
        @NotNull @PathVariable UUID id, @Valid @NotNull @RequestBody V1UpdateWidgetRequest request) {
        var updateWidgetResult = widgetService.v1Update(id, mapper.v1UpdateRequestToDto(request));

        if (request.getCenterX() == null &&
            request.getCenterY() == null &&
            request.getWidth() == null &&
            request.getHeight() == null &&
            request.getZ() == null) {
            return ResponseEntity.badRequest().body("There is nothing to update according to the request");
        }

        if (updateWidgetResult.hasError(NotFoundError.class))
            return ResponseEntity.notFound().build();

        if (updateWidgetResult.isFailed())
            return ResponseEntity.internalServerError().body(updateWidgetResult.getError());

        return ResponseEntity.ok(mapper.v1DtoToUpdatingResponse(updateWidgetResult.getValue()));
    }

    @DeleteMapping(path = "{id}")
    @Operation(summary = "Deletes the widget by its ID", description = "Deletes the widget by its identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(
            schema = @Schema(implementation = ValidationErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NotFound", content = @Content()),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<?> v1Delete(@NotNull @PathVariable UUID id) {
        var deleteWidgetResult = widgetService.v1Delete(id);

        if (deleteWidgetResult.hasError(NotFoundError.class))
            return ResponseEntity.notFound().build();

        if (deleteWidgetResult.isFailed())
            return ResponseEntity.internalServerError().body(deleteWidgetResult.getError());

        return ResponseEntity.ok().build();
    }
}
