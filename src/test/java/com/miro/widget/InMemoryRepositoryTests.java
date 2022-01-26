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
        widgetRepository.v1DeleteAll();
    }

    @Test
    public void should_successfully_insert() {
        var insertModel = generateV1InsertWidgetModel();
        var insertionResult = widgetRepository.v1Insert(insertModel);

        assertTrue(insertionResult.isSucceed());
        assertNotNull(insertionResult.getValue().getId());
        assertEquals(insertionResult.getValue().getZ(), insertModel.getZ());
        assertEquals(insertionResult.getValue().getCoordinates().getCenterX(), insertModel.getCenterX());
        assertEquals(insertionResult.getValue().getCoordinates().getCenterY(), insertModel.getCenterY());
        assertEquals(insertionResult.getValue().getSize().getWidth(), insertModel.getWidth());
        assertEquals(insertionResult.getValue().getSize().getHeight(), insertModel.getHeight());

        assertThat(insertionResult.getValue())
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.v1GetById(insertionResult.getValue().getId()).getValue());
    }

    @Test
    public void should_be_failed_when_failed_to_insert() {
        var insertionResult = widgetRepository.v1Insert(null);

        assertTrue(insertionResult.isFailed());
        assertNull(insertionResult.getValue());
        assertThat(widgetRepository.v1GetAll().getValue().size()).isEqualTo(0);
    }

    @Test
    public void should_successfully_get_by_ID() {
        var insertedWidget = widgetRepository.v1Insert(generateV1InsertWidgetModel()).getValue();

        var getByIdResult = widgetRepository.v1GetById(insertedWidget.getId());

        assertTrue(getByIdResult.isSucceed());
        assertThat(getByIdResult.getValue()).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_be_failed_when_failed_to_get_by_ID() {
        widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var getByIdResult = widgetRepository.v1GetById(UUID.randomUUID());

        assertTrue(getByIdResult.isFailed());
        assertTrue(getByIdResult.hasError(NotFoundError.class));
    }

    @Test
    public void should_successfully_get_by_Z_index() {
        var insertedWidget = widgetRepository.v1Insert(generateV1InsertWidgetModel()).getValue();

        var getByZIndexResult = widgetRepository.v1GetByZIndex(insertedWidget.getZ());

        assertTrue(getByZIndexResult.isSucceed());
        assertThat(getByZIndexResult.getValue()).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_be_failed_when_failed_to_get_by_Z_index() {
        widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var getByZResult = widgetRepository.v1GetByZIndex(RandomUtils.nextInt());

        assertTrue(getByZResult.isFailed());
        assertTrue(getByZResult.hasError(NotFoundError.class));
    }

    @Test
    public void should_successfully_get_all_inserted_widgets() {
        var insertedWidget = widgetRepository.v1Insert(generateV1InsertWidgetModel()).getValue();

        var getAllResult = widgetRepository.v1GetAll();

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().size()).isEqualTo(1);
        assertThat(getAllResult.getValue().get(0)).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_successfully_update() {
        var insertedWidget = widgetRepository.v1Insert(generateV1InsertWidgetModel()).getValue();

        var updatingModel = generateV1UpdateWidgetModel(insertedWidget.getId());
        var updatingResult = widgetRepository.v1Update(updatingModel);

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
            .isEqualTo(widgetRepository.v1GetById(updatingModel.getId()).getValue());
        assertThat(updatingResult.getValue())
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.v1GetByZIndex(updatingModel.getZ()).getValue());
        assertThat(updatingResult.getValue())
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.v1GetAll().getValue().get(0));
    }

    @Test
    public void should_be_failed_when_failed_to_update() {
        widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var updatingModel = generateV1UpdateWidgetModel(UUID.randomUUID());
        var updatingResult = widgetRepository.v1Update(updatingModel);

        assertTrue(updatingResult.isFailed());
        assertTrue(updatingResult.hasError(NotFoundError.class));
    }

    @Test
    public void should_successfully_delete_by_ID() {
        var insertedWidget = widgetRepository.v1Insert(generateV1InsertWidgetModel()).getValue();

        var deletingResult = widgetRepository.v1Delete(insertedWidget.getId());

        var getAllResult = widgetRepository.v1GetAll();

        assertTrue(deletingResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().size()).isEqualTo(0);
    }

    @Test
    public void should_be_failed_when_failed_to_delete_by_ID() {
        widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var deletingResult = widgetRepository.v1Delete(UUID.randomUUID());

        assertTrue(deletingResult.isFailed());
        assertTrue(deletingResult.hasError(NotFoundError.class));
    }

    @Test
    public void should_successfully_delete_all() {
        widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var deletingResult = widgetRepository.v1DeleteAll();

        var getAllResult = widgetRepository.v1GetAll();

        assertTrue(deletingResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().size()).isEqualTo(0);
    }
}
