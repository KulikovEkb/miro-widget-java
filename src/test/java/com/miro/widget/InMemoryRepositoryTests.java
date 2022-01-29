package com.miro.widget;

import com.miro.widget.mappers.BllAndDalMapperImpl;
import com.miro.widget.service.repositories.InMemoryRepositoryImpl;
import com.miro.widget.service.repositories.WidgetRepository;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import result.errors.NotFoundError;

import java.util.UUID;

import static com.miro.widget.helpers.Generator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InMemoryRepositoryTests {
    private WidgetRepository widgetRepository;

    @BeforeEach
    public void setUp() {
        widgetRepository = new InMemoryRepositoryImpl(new BllAndDalMapperImpl());
    }

    @AfterEach
    public void tearDown() {
        widgetRepository.deleteAll();
    }

    @Test
    public void should_successfully_insert() {
        var insertModel = generateV1InsertWidgetModel();
        var insertionResult = widgetRepository.insert(insertModel);

        assertTrue(insertionResult.isSucceed());
        assertNotNull(insertionResult.getValue().getId());
        assertEquals(insertionResult.getValue().getZ(), insertModel.getZ());
        assertEquals(insertionResult.getValue().getCoordinates().getCenterX(), insertModel.getCenterX());
        assertEquals(insertionResult.getValue().getCoordinates().getCenterY(), insertModel.getCenterY());
        assertEquals(insertionResult.getValue().getSize().getWidth(), insertModel.getWidth());
        assertEquals(insertionResult.getValue().getSize().getHeight(), insertModel.getHeight());

        assertThat(insertionResult.getValue())
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.getById(insertionResult.getValue().getId()).getValue());
    }

    @Test
    public void should_be_failed_when_failed_to_insert() {
        var insertionResult = widgetRepository.insert(null);

        assertTrue(insertionResult.isFailed());
        assertNull(insertionResult.getValue());
        assertThat(widgetRepository.getRange(1, 10).getValue().getWidgets().size()).isEqualTo(0);
    }

    @Test
    public void should_successfully_get_by_ID() {
        var insertedWidget = widgetRepository.insert(generateV1InsertWidgetModel()).getValue();

        var getByIdResult = widgetRepository.getById(insertedWidget.getId());

        assertTrue(getByIdResult.isSucceed());
        assertThat(getByIdResult.getValue()).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_be_failed_when_failed_to_get_by_ID() {
        widgetRepository.insert(generateV1InsertWidgetModel());

        var getByIdResult = widgetRepository.getById(UUID.randomUUID());

        assertTrue(getByIdResult.isFailed());
        assertTrue(getByIdResult.hasError(NotFoundError.class));
    }

    @Test
    public void should_successfully_get_by_Z_index() {
        var insertedWidget = widgetRepository.insert(generateV1InsertWidgetModel()).getValue();

        var getByZIndexResult = widgetRepository.getByZIndex(insertedWidget.getZ());

        assertTrue(getByZIndexResult.isSucceed());
        assertThat(getByZIndexResult.getValue()).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_be_failed_when_failed_to_get_by_Z_index() {
        widgetRepository.insert(generateV1InsertWidgetModel());

        var getByZResult = widgetRepository.getByZIndex(RandomUtils.nextInt());

        assertTrue(getByZResult.isFailed());
        assertTrue(getByZResult.hasError(NotFoundError.class));
    }

    @Test
    public void should_successfully_get_all_inserted_widgets() {
        var insertedWidget = widgetRepository.insert(generateV1InsertWidgetModel()).getValue();

        var getAllResult = widgetRepository.getRange(1, 10);

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets().size()).isEqualTo(1);
        assertThat(getAllResult.getValue().getWidgets().get(0)).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_successfully_update() {
        var insertedWidget = widgetRepository.insert(generateV1InsertWidgetModel()).getValue();

        var updatingModel = generateV1UpdateWidgetModel(insertedWidget.getId());
        var updatingResult = widgetRepository.update(updatingModel);

        assertTrue(updatingResult.isSucceed());
        assertThat(updatingResult.getValue().getId()).isEqualTo(insertedWidget.getId());
        assertEquals(updatingResult.getValue().getZ(), updatingModel.getZ());
        assertEquals(updatingResult.getValue().getCoordinates().getCenterX(), updatingModel.getCenterX());
        assertEquals(updatingResult.getValue().getCoordinates().getCenterY(), updatingModel.getCenterY());
        assertEquals(updatingResult.getValue().getSize().getWidth(), updatingModel.getWidth());
        assertEquals(updatingResult.getValue().getSize().getHeight(), updatingModel.getHeight());

        assertNotEquals(updatingResult.getValue().getZ(), insertedWidget.getZ());
        assertNotEquals(
            updatingResult.getValue().getCoordinates().getCenterX(), insertedWidget.getCoordinates().getCenterX());
        assertNotEquals(
            updatingResult.getValue().getCoordinates().getCenterY(), insertedWidget.getCoordinates().getCenterY());
        assertNotEquals(updatingResult.getValue().getSize().getWidth(), insertedWidget.getSize().getWidth());
        assertNotEquals(updatingResult.getValue().getSize().getHeight(), insertedWidget.getSize().getHeight());

        assertThat(updatingResult.getValue())
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.getById(updatingModel.getId()).getValue());
        assertThat(updatingResult.getValue())
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.getByZIndex(updatingModel.getZ()).getValue());
        assertThat(updatingResult.getValue())
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.getRange(1, 10).getValue().getWidgets().get(0));
    }

    @Test
    public void should_be_failed_when_failed_to_update() {
        widgetRepository.insert(generateV1InsertWidgetModel());

        var updatingModel = generateV1UpdateWidgetModel(UUID.randomUUID());
        var updatingResult = widgetRepository.update(updatingModel);

        assertTrue(updatingResult.isFailed());
        assertTrue(updatingResult.hasError(NotFoundError.class));
    }

    @Test
    public void should_successfully_delete_by_ID() {
        var insertedWidget = widgetRepository.insert(generateV1InsertWidgetModel()).getValue();

        var deletingResult = widgetRepository.delete(insertedWidget.getId());

        var getAllResult = widgetRepository.getRange(1, 10);

        assertTrue(deletingResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets().size()).isEqualTo(0);
    }

    @Test
    public void should_be_failed_when_failed_to_delete_by_ID() {
        widgetRepository.insert(generateV1InsertWidgetModel());

        var deletingResult = widgetRepository.delete(UUID.randomUUID());

        assertTrue(deletingResult.isFailed());
        assertTrue(deletingResult.hasError(NotFoundError.class));
    }

    @Test
    public void should_successfully_delete_all() {
        widgetRepository.insert(generateV1InsertWidgetModel());

        var deletingResult = widgetRepository.deleteAll();

        var getAllResult = widgetRepository.getRange(1, 10);

        assertTrue(deletingResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets().size()).isEqualTo(0);
    }
}
