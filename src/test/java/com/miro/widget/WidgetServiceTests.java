package com.miro.widget;

import com.miro.widget.exceptions.WidgetNotFoundException;
import com.miro.widget.service.WidgetServiceImpl2;
import com.miro.widget.service.repositories.WidgetRepository2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.miro.widget.helpers.Generator2.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceTests {
    private final WidgetRepository2 widgetRepository = Mockito.mock(WidgetRepository2.class);

    WidgetServiceImpl2 widgetService;

    @BeforeEach
    public void setUp() {
        widgetService = new WidgetServiceImpl2(widgetRepository);
    }

    @Test
    public void should_successfully_create_single_widget() {
        var creationParams = generateCreationParams();
        var createdWidget = convertToWidgetFromCreationParams(creationParams);

        when(widgetRepository.findByZ(creationParams.getZ())).thenReturn(Optional.empty());
        when(widgetRepository.save(argThat(x -> x.getZ().equals(creationParams.getZ()))))
            .thenReturn(createdWidget);

        var serviceResult = widgetService.create(creationParams);

        assertNotNull(serviceResult);
        assertThat(serviceResult).usingRecursiveComparison().isEqualTo(createdWidget);

        verify(widgetRepository, times(1)).findByZ(creationParams.getZ());
        verify(widgetRepository, times(1)).save(argThat(x -> x.getZ().equals(creationParams.getZ())));
    }

    @Test
    public void should_be_failed_when_failed_to_insert_because_of_Z_index_max_value_reached() {
        var creationParams = generateCreationParams(Integer.MAX_VALUE - 1);

        when(widgetRepository.findByZ(creationParams.getZ())).thenReturn(Optional.empty());
        when(widgetRepository.save(argThat(x -> x.getZ().equals(creationParams.getZ()))))
            .thenReturn(convertToWidgetFromCreationParams(creationParams));

        widgetService.create(creationParams);

        assertThrows(
            ArithmeticException.class,
            () -> widgetService.create(generateCreationParams(null)),
            "Max available Z index value reached");

        verify(widgetRepository, times(1)).findByZ(creationParams.getZ());
        verify(widgetRepository, times(1)).save(argThat(x -> x.getZ().equals(creationParams.getZ())));
    }

    @Test
    public void should_successfully_get_by_ID() {
        var widget = generateWidget();

        when(widgetRepository.findById(widget.getId())).thenReturn(Optional.of(widget));

        var getByIdResult = widgetService.findById(widget.getId());

        assertTrue(getByIdResult.isPresent());
        assertThat(getByIdResult.get()).usingRecursiveComparison().isEqualTo(widget);

        verify(widgetRepository, times(1)).findById(widget.getId());
    }

    @Test
    public void should_be_failed_when_failed_to_get_by_ID() {
        var widgetId = UUID.randomUUID();

        when(widgetRepository.findById(widgetId)).thenReturn(Optional.empty());

        var getByIdResult = widgetService.findById(widgetId);

        assertTrue(getByIdResult.isEmpty());

        verify(widgetRepository, times(1)).findById(widgetId);
    }

    @Test
    public void should_successfully_get_all_inserted_widgets() {
        var widget = generateWidget();

        when(widgetRepository.findAll(any())).thenReturn(new PageImpl<>(List.of(widget)));

        var getRangeResult = widgetService.findRange(0, 10);

        assertTrue(getRangeResult.hasContent());
        assertThat(getRangeResult.getContent().size()).isEqualTo(1);
        assertThat(getRangeResult.getContent().get(0)).usingRecursiveComparison().isEqualTo(widget);

        verify(widgetRepository, times(1)).findAll(PageRequest.of(0, 10).withSort(Sort.by("z")));
    }

    @Test
    public void should_successfully_update_single_existing_widget() {
        var creationParams = generateCreationParams();
        var createdWidget = convertToWidgetFromCreationParams(creationParams);
        var updatingParams = generateUpdatingParams();
        var updatedWidget = convertToWidgetFromUpdatingDto(createdWidget.getId(), updatingParams);

        when(widgetRepository.findByZ(creationParams.getZ())).thenReturn(Optional.empty());
        when(widgetRepository.findByZ(updatingParams.getZ())).thenReturn(Optional.empty());
        when(widgetRepository.save(any()))
            .thenReturn(createdWidget);
        when(widgetRepository.findById(createdWidget.getId())).thenReturn(Optional.of(createdWidget));
        when(widgetRepository.save(any()))
            .thenReturn(updatedWidget);

        widgetService.create(creationParams);

        var updatingResult = widgetService.update(createdWidget.getId(), updatingParams);

        assertNotNull(updatingResult);
        assertThat(updatingResult).usingRecursiveComparison().isEqualTo(updatedWidget);

        verify(widgetRepository, times(1)).findByZ(creationParams.getZ());
        verify(widgetRepository, times(1)).findByZ(updatingParams.getZ());
        verify(widgetRepository, times(1)).save(argThat(x -> x.getZ().equals(creationParams.getZ())));
        verify(widgetRepository, times(1)).findById(createdWidget.getId());
        verify(widgetRepository, times(1)).save(argThat(x -> x.getId().equals(createdWidget.getId())));
    }

    @Test
    public void should_successfully_delete_by_ID() {
        var widgetId = UUID.randomUUID();

        widgetService.delete(widgetId);

        verify(widgetRepository, times(1)).deleteById(widgetId);
    }

    @Test
    public void should_be_failed_when_failed_to_delete_by_ID() {
        var widgetId = UUID.randomUUID();

        doThrow(EmptyResultDataAccessException.class).when(widgetRepository).deleteById(widgetId);

        assertThrows(
            WidgetNotFoundException.class,
            () -> widgetService.delete(widgetId),
            String.format("Widget with id '%s' not found", widgetId));

        verify(widgetRepository, times(1)).deleteById(widgetId);
    }
}
