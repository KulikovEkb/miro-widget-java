package com.miro.widget.controllers;

        /*[HttpPost("create")]
        public async Task<IActionResult> V1Create([FromBody] V1CreateWidgetRequest request)
        {
            var creationResult = await _widgetService.V1Create(new V1CreateWidgetDto
            {
                X = request.X,
                Y = request.Y,
                Width = request.Width,
                Height = request.Height,
                ZIndex = request.ZIndex
            });

            if (creationResult.IsFailed)
                return StatusCode(StatusCodes.Status500InternalServerError, creationResult.Errors);

            return CreatedAtAction(
                nameof(V1GetById),
                new {id = creationResult.Value.Id},
                V1CreateWidgetResponseMapper.FromDto(creationResult.Value));
        }*/

        /*[HttpPut("{id:guid}")]
        public async Task<IActionResult> V1Update(Guid id, [FromBody] V1UpdateWidgetRequest request)
        {
            var updatingResult = await _widgetService.V1Update(new V1UpdateWidgetDto
            {
                Id = id,
                X = request.X,
                Y = request.Y,
                Width = request.Width,
                Height = request.Height,
                ZIndex = request.ZIndex
            });

            if (updatingResult.HasError<NotFoundError>())
                return NotFound();

            if (updatingResult.IsFailed)
                return StatusCode(StatusCodes.Status500InternalServerError, updatingResult.Errors);

            return Ok(V1UpdateWidgetResponseMapper.FromDto(updatingResult.Value));
        }*/

        /*[HttpDelete("{id:guid}")]
        public async Task<IActionResult> V1Delete(Guid id)
        {
            await _widgetService.V1Delete(id);

            return Ok();
        }*/

        /*[HttpGet("{id:guid}")]
        public async Task<IActionResult> V1GetById(Guid id)
        {
            var getWidgetResult = await _widgetService.V1GetById(id);

            if (getWidgetResult.HasError<NotFoundError>())
                return NotFound();

            if (getWidgetResult.IsFailed)
                return StatusCode(StatusCodes.Status500InternalServerError, getWidgetResult.Errors);

            return Ok(V1GetWidgetByIdResponseMapper.FromDto(getWidgetResult.Value));
        }*/

        /*[HttpGet("")]
        public async Task<IActionResult> V1GetAll()
        {
            var widgets = await _widgetService.V1GetAll();

            return Ok(widgets.Select(V1GetWidgetsResponseMapper.FromDto));
        }*/

import com.miro.widget.controllers.models.requests.V1CreateWidgetRequest;
import com.miro.widget.controllers.models.responses.V1Coordinates;
import com.miro.widget.controllers.models.responses.V1CreateWidgetResponse;
import com.miro.widget.controllers.models.responses.V1Size;
import com.miro.widget.services.WidgetService;
import com.miro.widget.services.models.V1CreateWidgetDto;
import com.miro.widget.services.models.V1WidgetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1/widgets")
public class WidgetController {
    private final WidgetService widgetService;

    @Autowired
    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping(path = "create")
    public ResponseEntity<V1CreateWidgetResponse> v1Create(@RequestBody V1CreateWidgetRequest request) {
        V1WidgetDto createdWidget = widgetService.v1Create(new V1CreateWidgetDto(
                request.getMiddleX(),
                request.getMiddleY(),
                request.getZIndex(),
                request.getWidth(),
                request.getHeight()
        ));

        return ResponseEntity
                // todo(kulikov): uncomment
                // .created(linkTo(methodOn(WidgetController.class).one(createdWidget.getId())).toUri())
                .status(HttpStatus.CREATED)
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
}
