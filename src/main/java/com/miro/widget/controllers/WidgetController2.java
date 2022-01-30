package com.miro.widget.controllers;

import com.miro.widget.controllers.models.requests.*;
import com.miro.widget.controllers.models.responses.*;
import com.miro.widget.controllers.validation.models.ValidationErrorResponse;
import com.miro.widget.exceptions.WidgetNotFoundException;
import com.miro.widget.mappers.WebAndBllMapper;
import com.miro.widget.service.WidgetService2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Validated
@RestController
@RequestMapping(path = "api/v2/widgets", produces = MediaType.APPLICATION_JSON_VALUE)
public class WidgetController2 {
    private final WebAndBllMapper mapper;
    private final WidgetService2 widgetService;

    @Autowired
    public WidgetController2(WebAndBllMapper mapper, WidgetService2 widgetService) {
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
    public ResponseEntity<?> create(@RequestBody @NotNull @Valid V1CreateWidgetRequest request) {
        try {
            var createdWidget = widgetService.create(mapper.v1CreateRequestToBllModel(request));

            var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdWidget.getId())
                .toUri();

            return ResponseEntity
                .created(uri)
                .body(mapper.bllModelToDto(createdWidget));
        } catch (WidgetNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage(), exc);
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), exc);
        }
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
    public ResponseEntity<?> getById(@PathVariable @NotNull UUID id) {
        var foundWidget = widgetService.findById(id);

        if (foundWidget.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(mapper.bllModelToDto(foundWidget.get()));
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
    public ResponseEntity<?> getRange(
        @RequestParam(defaultValue = "0") @Min(0) @Max(Integer.MAX_VALUE) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(500) int size) {
        var foundWidgets = widgetService.findRange(page, size);

        return ResponseEntity.ok(new PageImpl<>(
            foundWidgets.getContent()
                .stream()
                .map(mapper::bllModelToDto)
                .collect(toList()),
            foundWidgets.getPageable(),
            foundWidgets.getTotalElements()));
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
    public ResponseEntity<?> update(
        @NotNull @PathVariable UUID id, @Valid @NotNull @RequestBody V1UpdateWidgetRequest request) {
        try {
            var updatedWidget = widgetService.update(id, mapper.v1UpdateRequestToBllModel(request));

            if (request.getCenterX() == null &&
                request.getCenterY() == null &&
                request.getWidth() == null &&
                request.getHeight() == null &&
                request.getZ() == null) {
                return ResponseEntity.badRequest().body("There is nothing to update according to the request");
            }

            return ResponseEntity.ok(mapper.bllModelToDto(updatedWidget));
        } catch (WidgetNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage(), exc);
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), exc);
        }
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
    public ResponseEntity<?> delete(@NotNull @PathVariable UUID id) {
        try {
            widgetService.delete(id);

            return ResponseEntity.ok().build();
        } catch (WidgetNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage(), exc);
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), exc);
        }
    }
}
