package com.miro.widget.controllers;

import com.miro.widget.controllers.models.requests.*;
import com.miro.widget.controllers.models.responses.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = "api/v1/widgets", produces = MediaType.APPLICATION_JSON_VALUE)
public class WidgetController {
    private final WebAndBllMapper mapper;
    private final WidgetService widgetService;

    @Autowired
    public WidgetController(
        WebAndBllMapper mapper, WidgetService widgetService) {
        this.mapper = mapper;
        this.widgetService = widgetService;
    }

    // todo(kulikov): add error model to swagger
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates widget", description = "Having a set of coordinates, Z-index, width, and height, " +
        "we get a complete widget description in the response. The server generates the identifier. If a Z-index is " +
        "not specified, the widget moves to the foreground (becomes maximum). If the existing Z-index is specified, " +
        "then the new widget shifts widget with the same (and greater if needed) upwards")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", content = @Content(schema = @Schema(implementation = V1CreateWidgetResponse.class))),
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content()),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<V1CreateWidgetResponse> v1Create(@RequestBody V1CreateWidgetRequest request) {
        var createdWidget = widgetService.v1Create(mapper.v1CreateRequestToDto(request));

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdWidget.getId())
            .toUri();

        return ResponseEntity
            .created(uri)
            .body(mapper.v1DtoToCreationResponse(createdWidget));
    }

    @GetMapping(path = "{id}")
    @Operation(summary = "Returns the widget by its ID", description = "Returns a complete description of the widget")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", content = @Content(schema = @Schema(implementation = V1GetWidgetByIdResponse.class))),
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content()),
        @ApiResponse(responseCode = "404", description = "NotFound", content = @Content()),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<V1GetWidgetByIdResponse> v1GetById(@PathVariable UUID id) {
        var widget = widgetService.v1GetById(id);

        return ResponseEntity.ok(mapper.v1DtoToGetByIdResponse(widget));
    }

    @GetMapping
    @Operation(summary = "Returns a list of widgets", description = "Returns a list of all widgets sorted " +
        "by Z-index, from smallest to largest")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", content = @Content(schema = @Schema(implementation = V1GetAllWidgetsResponse.class))),
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content()),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<V1GetAllWidgetsResponse> v1GetAll() {
        var widgets = widgetService.v1GetAll();

        return ResponseEntity.ok(new V1GetAllWidgetsResponse(widgets
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
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content()),
        @ApiResponse(responseCode = "404", description = "NotFound", content = @Content()),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<V1UpdateWidgetResponse> v1Update(
        @PathVariable UUID id, @RequestBody V1UpdateWidgetRequest request) {
        var updatedWidget = widgetService.v1Update(id, mapper.v1UpdateRequestToDto(request));

        return ResponseEntity.ok(mapper.v1DtoToUpdatingResponse(updatedWidget));
    }

    @DeleteMapping(path = "{id}")
    @Operation(summary = "Deletes the widget by its ID", description = "Deletes the widget by its identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content()),
        @ApiResponse(responseCode = "404", description = "NotFound", content = @Content()),
        @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content())
    })
    public ResponseEntity<Void> v1Delete(@PathVariable UUID id) {
        widgetService.v1Delete(id);

        return ResponseEntity.ok().build();
    }
}
