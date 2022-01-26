package com.miro.widget;

import com.miro.widget.service.WidgetServiceImpl;
import com.miro.widget.service.repositories.WidgetRepository;
import com.miro.widget.service.models.V1WidgetRangeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import result.PlainResult;
import result.Result;
import result.errors.Error;
import result.errors.NotFoundError;

import java.util.List;
import java.util.UUID;

import static com.miro.widget.helpers.Generator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceTests {
    private final WidgetRepository widgetRepository = Mockito.mock(WidgetRepository.class);

    WidgetServiceImpl widgetService;

    @BeforeEach
    public void setUp() {
        widgetService = new WidgetServiceImpl(widgetRepository);
    }

    @Test
    public void should_successfully_create_single_widget() {
        var creationDto = generateV1CreateWidgetDto();
        var widgetDto = convertToWidgetDtoFromCreationDto(creationDto);

        when(widgetRepository.v1GetByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(widgetDto));

        var creationResult = widgetService.v1Create(creationDto);

        assertTrue(creationResult.isSucceed());
        assertThat(creationResult.getValue()).usingRecursiveComparison().isEqualTo(widgetDto);

        /*verify(widgetRepository, times(1)).v1GetByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ())));*/
    }

    @Test
    public void should_be_failed_when_failed_to_insert() {
        var creationDto = generateV1CreateWidgetDto();

        when(widgetRepository.v1GetByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Fail(new Error("insertion error")));

        var creationResult = widgetService.v1Create(creationDto);

        assertTrue(creationResult.isFailed());
        assertNull(creationResult.getValue());

        /*verify(widgetRepository, times(1)).v1GetByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ())));*/
    }

    @Test
    public void should_be_failed_when_failed_to_insert_because_of_Z_index_max_value_reached() {
        var creationDto = generateV1CreateWidgetDto(Integer.MAX_VALUE - 1);

        when(widgetRepository.v1GetByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(convertToWidgetDtoFromCreationDto(creationDto)));

        widgetService.v1Create(creationDto);

        var creationResult = widgetService.v1Create(generateV1CreateWidgetDto(null));

        assertTrue(creationResult.isFailed());
        assertNull(creationResult.getValue());
        assertThat(creationResult.getError().getMessage()).isEqualTo("Max available Z index value reached");

        /*verify(widgetRepository, times(1)).v1GetByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ())));*/
    }

    @Test
    public void should_successfully_get_by_ID() {
        var widgetDto = generateV1WidgetDto();

        when(widgetRepository.v1GetById(widgetDto.getId())).thenReturn(Result.Ok(widgetDto));

        var getByIdResult = widgetService.v1GetById(widgetDto.getId());

        assertTrue(getByIdResult.isSucceed());
        assertThat(getByIdResult.getValue()).usingRecursiveComparison().isEqualTo(widgetDto);

        //verify(widgetRepository, times(1)).v1GetById(widgetDto.getId());
    }

    @Test
    public void should_be_failed_when_failed_to_get_by_ID() {
        var widgetId = UUID.randomUUID();

        when(widgetRepository.v1GetById(widgetId)).thenReturn(Result.Fail(new NotFoundError("widget not found")));

        var getByIdResult = widgetService.v1GetById(widgetId);

        assertTrue(getByIdResult.isFailed());
        assertNull(getByIdResult.getValue());

        //verify(widgetRepository, times(1)).v1GetById(widgetId);
    }

    @Test
    public void should_successfully_get_all_inserted_widgets() {
        var widgetDto = generateV1WidgetDto();

        when(widgetRepository.v1GetRange(1, 1)).thenReturn(Result.Ok(new V1WidgetRangeDto(1, 1, List.of(widgetDto))));

        var getAllResult = widgetService.v1GetRange(1, 1);

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets().size()).isEqualTo(1);
        assertThat(getAllResult.getValue().getWidgets().get(0)).usingRecursiveComparison().isEqualTo(widgetDto);

        //verify(widgetRepository, times(1)).v1GetAll();
    }

    @Test
    public void should_successfully_update_single_existing_widget() {
        var creationDto = generateV1CreateWidgetDto();
        var createdWidget = convertToWidgetDtoFromCreationDto(creationDto);
        var updatingDto = generateV1UpdateWidgetDto();
        var updatedWidget = convertToWidgetDtoFromUpdatingDto(createdWidget.getId(), updatingDto);

        when(widgetRepository.v1GetByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1GetByZIndex(updatingDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(createdWidget));
        when(widgetRepository.v1GetById(createdWidget.getId())).thenReturn(Result.Ok(createdWidget));
        when(widgetRepository.v1Update(argThat(x -> x.getId().equals(createdWidget.getId()))))
            .thenReturn(Result.Ok(updatedWidget));

        widgetService.v1Create(creationDto);

        var updatingResult = widgetService.v1Update(createdWidget.getId(), updatingDto);

        assertTrue(updatingResult.isSucceed());
        assertThat(updatingResult.getValue()).usingRecursiveComparison().isEqualTo(updatedWidget);

        /*verify(widgetRepository, times(1)).v1GetByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).v1GetByZIndex(updatingDto.getZ());
        verify(widgetRepository, times(1)).v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ())));
        verify(widgetRepository, times(1)).v1GetById(createdWidget.getId());
        verify(widgetRepository, times(1)).v1Update(argThat(x -> x.getId().equals(createdWidget.getId())));*/
    }

    @Test
    public void should_be_failed_when_failed_to_update_single_existing_widget() {
        var creationDto = generateV1CreateWidgetDto();
        var createdWidget = convertToWidgetDtoFromCreationDto(creationDto);
        var updatingDto = generateV1UpdateWidgetDto(null);

        when(widgetRepository.v1GetByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(createdWidget));
        when(widgetRepository.v1Update(argThat(x -> x.getId().equals(createdWidget.getId()))))
            .thenReturn(Result.Fail(new Error("updating error")));

        widgetService.v1Create(creationDto);

        var updatingResult = widgetService.v1Update(createdWidget.getId(), updatingDto);

        assertTrue(updatingResult.isFailed());
        assertNull(updatingResult.getValue());
        assertThat(updatingResult.getError().getMessage()).isEqualTo("updating error");

        /*verify(widgetRepository, times(1)).v1GetByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ())));
        verify(widgetRepository, times(1)).v1Update(argThat(x -> x.getId().equals(createdWidget.getId())));*/
    }

    @Test
    public void should_successfully_delete_by_ID() {
        var widgetId = UUID.randomUUID();

        when(widgetRepository.v1Delete(widgetId)).thenReturn(PlainResult.Ok());

        var deletingResult = widgetRepository.v1Delete(widgetId);

        assertTrue(deletingResult.isSucceed());

        //verify(widgetRepository, times(1)).v1Delete(widgetId);
    }

    @Test
    public void should_be_failed_when_failed_to_delete_by_ID() {
        var widgetId = UUID.randomUUID();

        when(widgetRepository.v1Delete(widgetId)).thenReturn(PlainResult.Fail(new Error("deleting error")));

        var deletingResult = widgetRepository.v1Delete(widgetId);

        assertTrue(deletingResult.isFailed());
        assertThat(deletingResult.getError().getMessage()).isEqualTo("deleting error");

        //verify(widgetRepository, times(1)).v1Delete(widgetId);
    }
}
