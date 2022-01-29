package com.miro.widget;

import com.miro.widget.service.WidgetServiceImpl;
import com.miro.widget.service.repositories.WidgetRepository;
import com.miro.widget.service.models.WidgetRange;
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
import static org.mockito.Mockito.*;

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

        when(widgetRepository.getByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(widgetDto));

        var creationResult = widgetService.create(creationDto);

        assertTrue(creationResult.isSucceed());
        assertThat(creationResult.getValue()).usingRecursiveComparison().isEqualTo(widgetDto);

        verify(widgetRepository, times(1)).getByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).insert(argThat(x -> x.getZ().equals(creationDto.getZ())));
    }

    @Test
    public void should_be_failed_when_failed_to_insert() {
        var creationDto = generateV1CreateWidgetDto();

        when(widgetRepository.getByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Fail(new Error("insertion error")));

        var creationResult = widgetService.create(creationDto);

        assertTrue(creationResult.isFailed());
        assertNull(creationResult.getValue());

        verify(widgetRepository, times(1)).getByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).insert(argThat(x -> x.getZ().equals(creationDto.getZ())));
    }

    @Test
    public void should_be_failed_when_failed_to_insert_because_of_Z_index_max_value_reached() {
        var creationDto = generateV1CreateWidgetDto(Integer.MAX_VALUE - 1);

        when(widgetRepository.getByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(convertToWidgetDtoFromCreationDto(creationDto)));

        widgetService.create(creationDto);

        var creationResult = widgetService.create(generateV1CreateWidgetDto(null));

        assertTrue(creationResult.isFailed());
        assertNull(creationResult.getValue());
        assertThat(creationResult.getError().getMessage()).isEqualTo("Max available Z index value reached");

        verify(widgetRepository, times(1)).getByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).insert(argThat(x -> x.getZ().equals(creationDto.getZ())));
    }

    @Test
    public void should_successfully_get_by_ID() {
        var widgetDto = generateV1WidgetDto();

        when(widgetRepository.getById(widgetDto.getId())).thenReturn(Result.Ok(widgetDto));

        var getByIdResult = widgetService.getById(widgetDto.getId());

        assertTrue(getByIdResult.isSucceed());
        assertThat(getByIdResult.getValue()).usingRecursiveComparison().isEqualTo(widgetDto);

        verify(widgetRepository, times(1)).getById(widgetDto.getId());
    }

    @Test
    public void should_be_failed_when_failed_to_get_by_ID() {
        var widgetId = UUID.randomUUID();

        when(widgetRepository.getById(widgetId)).thenReturn(Result.Fail(new NotFoundError("widget not found")));

        var getByIdResult = widgetService.getById(widgetId);

        assertTrue(getByIdResult.isFailed());
        assertNull(getByIdResult.getValue());

        verify(widgetRepository, times(1)).getById(widgetId);
    }

    @Test
    public void should_successfully_get_all_inserted_widgets() {
        var widgetDto = generateV1WidgetDto();

        when(widgetRepository.getRange(1, 1)).thenReturn(Result.Ok(new WidgetRange(1, 1, List.of(widgetDto))));

        var getAllResult = widgetService.getRange(1, 1);

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets().size()).isEqualTo(1);
        assertThat(getAllResult.getValue().getWidgets().get(0)).usingRecursiveComparison().isEqualTo(widgetDto);

        verify(widgetRepository, times(1)).getRange(1, 1);
    }

    @Test
    public void should_successfully_update_single_existing_widget() {
        var creationDto = generateV1CreateWidgetDto();
        var createdWidget = convertToWidgetDtoFromCreationDto(creationDto);
        var updatingDto = generateV1UpdateWidgetDto();
        var updatedWidget = convertToWidgetDtoFromUpdatingDto(createdWidget.getId(), updatingDto);

        when(widgetRepository.getByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.getByZIndex(updatingDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(createdWidget));
        when(widgetRepository.getById(createdWidget.getId())).thenReturn(Result.Ok(createdWidget));
        when(widgetRepository.update(argThat(x -> x.getId().equals(createdWidget.getId()))))
            .thenReturn(Result.Ok(updatedWidget));

        widgetService.create(creationDto);

        var updatingResult = widgetService.update(createdWidget.getId(), updatingDto);

        assertTrue(updatingResult.isSucceed());
        assertThat(updatingResult.getValue()).usingRecursiveComparison().isEqualTo(updatedWidget);

        verify(widgetRepository, times(1)).getByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).getByZIndex(updatingDto.getZ());
        verify(widgetRepository, times(1)).insert(argThat(x -> x.getZ().equals(creationDto.getZ())));
        verify(widgetRepository, times(1)).getById(createdWidget.getId());
        verify(widgetRepository, times(1)).update(argThat(x -> x.getId().equals(createdWidget.getId())));
    }

    @Test
    public void should_be_failed_when_failed_to_update_single_existing_widget() {
        var creationDto = generateV1CreateWidgetDto();
        var createdWidget = convertToWidgetDtoFromCreationDto(creationDto);
        var updatingDto = generateV1UpdateWidgetDto(null);

        when(widgetRepository.getByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(createdWidget));
        when(widgetRepository.update(argThat(x -> x.getId().equals(createdWidget.getId()))))
            .thenReturn(Result.Fail(new Error("updating error")));

        widgetService.create(creationDto);

        var updatingResult = widgetService.update(createdWidget.getId(), updatingDto);

        assertTrue(updatingResult.isFailed());
        assertNull(updatingResult.getValue());
        assertThat(updatingResult.getError().getMessage()).isEqualTo("updating error");

        verify(widgetRepository, times(1)).getByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).insert(argThat(x -> x.getZ().equals(creationDto.getZ())));
        verify(widgetRepository, times(1)).update(argThat(x -> x.getId().equals(createdWidget.getId())));
    }

    @Test
    public void should_successfully_delete_by_ID() {
        var widgetId = UUID.randomUUID();

        when(widgetRepository.delete(widgetId)).thenReturn(PlainResult.Ok());

        var deletingResult = widgetRepository.delete(widgetId);

        assertTrue(deletingResult.isSucceed());

        verify(widgetRepository, times(1)).delete(widgetId);
    }

    @Test
    public void should_be_failed_when_failed_to_delete_by_ID() {
        var widgetId = UUID.randomUUID();

        when(widgetRepository.delete(widgetId)).thenReturn(PlainResult.Fail(new Error("deleting error")));

        var deletingResult = widgetRepository.delete(widgetId);

        assertTrue(deletingResult.isFailed());
        assertThat(deletingResult.getError().getMessage()).isEqualTo("deleting error");

        verify(widgetRepository, times(1)).delete(widgetId);
    }
}
