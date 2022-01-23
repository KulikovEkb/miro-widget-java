package com.miro.widget.controllers;

import com.miro.widget.controllers.models.requests.*;
import com.miro.widget.controllers.models.responses.*;
import com.miro.widget.mappers.WebAndBllMapper;
import com.miro.widget.service.WidgetService;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<V1GetWidgetByIdResponse> v1GetById(@PathVariable UUID id) {
        var widget = widgetService.v1GetById(id);

        return ResponseEntity.ok(mapper.v1DtoToGetByIdResponse(widget));
    }

    @GetMapping
    public ResponseEntity<V1GetAllWidgetsResponse> v1GetAll() {
        var widgets = widgetService.v1GetAll();

        return ResponseEntity.ok(new V1GetAllWidgetsResponse(widgets
            .stream()
            .map(mapper::v1DtoToGetAllItem)
            .collect(toList())));
    }

    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<V1UpdateWidgetResponse> v1Update(
        @PathVariable UUID id, @RequestBody V1UpdateWidgetRequest request) {
        var updatedWidget = widgetService.v1Update(id, mapper.v1UpdateRequestToDto(request));

        return ResponseEntity.ok(mapper.v1DtoToUpdatingResponse(updatedWidget));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> v1Delete(@PathVariable UUID id) {
        widgetService.v1Delete(id);

        return ResponseEntity.ok().build();
    }
}
