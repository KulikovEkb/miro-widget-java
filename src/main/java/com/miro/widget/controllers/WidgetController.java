package com.miro.widget.controllers;

import com.miro.widget.controllers.models.requests.V1CreateWidgetRequest;
import com.miro.widget.controllers.models.requests.V1UpdateWidgetRequest;
import com.miro.widget.controllers.models.responses.*;
import com.miro.widget.service.WidgetService;
import com.miro.widget.service.models.V1CreateWidgetDto;
import com.miro.widget.service.models.V1UpdateWidgetDto;
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
    private final WidgetService widgetService;

    @Autowired
    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<V1CreateWidgetResponse> v1Create(@RequestBody V1CreateWidgetRequest request) {
        var createdWidget = widgetService.v1Create(new V1CreateWidgetDto(
            request.getMiddleX(),
            request.getMiddleY(),
            request.getZIndex(),
            request.getWidth(),
            request.getHeight()
        ));

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdWidget.getId())
            .toUri();

        return ResponseEntity
            .created(uri)
            .body(new V1CreateWidgetResponse(
                createdWidget.getId(),
                createdWidget.getZIndex(),
                new V1Coordinates(
                    createdWidget.getCoordinates().getCenterX(),
                    createdWidget.getCoordinates().getCenterY()
                ),
                new V1Size(
                    createdWidget.getSize().getWidth(),
                    createdWidget.getSize().getHeight()
                ),
                createdWidget.getUpdatedAt()
            ));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<V1GetWidgetByIdResponse> v1GetById(@PathVariable UUID id) {
        var widget = widgetService.v1GetById(id);

        return ResponseEntity.ok(new V1GetWidgetByIdResponse(
            widget.getId(),
            widget.getZIndex(),
            new V1Coordinates(
                widget.getCoordinates().getCenterX(),
                widget.getCoordinates().getCenterY()
            ),
            new V1Size(
                widget.getSize().getWidth(),
                widget.getSize().getHeight()
            ),
            widget.getUpdatedAt()
        ));
    }

    @GetMapping
    public ResponseEntity<V1GetAllWidgetsResponse> v1GetAll() {
        var widgets = widgetService.v1GetAll();

        return ResponseEntity.ok(new V1GetAllWidgetsResponse(widgets
            .stream()
            .map(x -> new V1GetAllWidgetsItem(
                x.getId(),
                x.getZIndex(),
                new V1Coordinates(
                    x.getCoordinates().getCenterX(),
                    x.getCoordinates().getCenterY()
                ),
                new V1Size(
                    x.getSize().getWidth(),
                    x.getSize().getHeight()
                ),
                x.getUpdatedAt()
            ))
            .collect(toList())));
    }

    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<V1UpdateWidgetResponse> v1Update(
        @PathVariable UUID id, @RequestBody V1UpdateWidgetRequest request) {
        var createdWidget = widgetService.v1Update(id, new V1UpdateWidgetDto(
            request.getMiddleX(),
            request.getMiddleY(),
            request.getZIndex(),
            request.getWidth(),
            request.getHeight()
        ));

        return ResponseEntity.ok(new V1UpdateWidgetResponse(
            createdWidget.getId(),
            createdWidget.getZIndex(),
            new V1Coordinates(
                createdWidget.getCoordinates().getCenterX(),
                createdWidget.getCoordinates().getCenterY()
            ),
            new V1Size(
                createdWidget.getSize().getWidth(),
                createdWidget.getSize().getHeight()
            ),
            createdWidget.getUpdatedAt()
        ));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> v1Delete(@PathVariable UUID id) {
        widgetService.v1Delete(id);

        return ResponseEntity.ok().build();
    }
}
