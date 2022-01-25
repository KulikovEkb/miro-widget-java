package com.miro.widget;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import com.miro.widget.mappers.BllAndDalMapperImpl;
import com.miro.widget.service.repositories.InMemoryRepositoryImpl;
import com.miro.widget.service.repositories.WidgetRepository;
import com.miro.widget.service.repositories.models.V1InsertWidgetModel;
import com.miro.widget.service.repositories.models.V1UpdateWidgetModel;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import result.errors.NotFoundError;

import java.util.UUID;

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
        var insertionResult = widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var getByIdResult = widgetRepository.v1GetById(insertionResult.getValue().getId());

        assertTrue(getByIdResult.isSucceed());
        assertThat(getByIdResult.getValue()).usingRecursiveComparison().isEqualTo(insertionResult.getValue());
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
        var insertionResult = widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var getByZIndexResult = widgetRepository.v1GetByZIndex(insertionResult.getValue().getZ());

        assertTrue(getByZIndexResult.isSucceed());
        assertThat(getByZIndexResult.getValue()).usingRecursiveComparison().isEqualTo(insertionResult.getValue());
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
        var insertionResult = widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var getAllResult = widgetRepository.v1GetAll();

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().size()).isEqualTo(1);
        assertThat(getAllResult.getValue().get(0)).usingRecursiveComparison().isEqualTo(insertionResult.getValue());
    }

    @Test
    public void should_successfully_update() {
        var insertionResult = widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var updatingModel = generateV1UpdateWidgetModel(insertionResult.getValue().getId());
        var updatingResult = widgetRepository.v1Update(updatingModel);

        assertTrue(updatingResult.isSucceed());
        assertThat(updatingResult.getValue().getId()).isEqualTo(insertionResult.getValue().getId());
        assertEquals(updatingResult.getValue().getZ(), updatingModel.getZ());
        assertEquals(updatingResult.getValue().getCoordinates().getCenterX(), updatingModel.getCenterX());
        assertEquals(updatingResult.getValue().getCoordinates().getCenterY(), updatingModel.getCenterY());
        assertEquals(updatingResult.getValue().getSize().getWidth(), updatingModel.getWidth());
        assertEquals(updatingResult.getValue().getSize().getHeight(), updatingModel.getHeight());

        assertNotEquals(updatingResult.getValue().getZ(), insertionResult.getValue().getZ());
        assertNotEquals(
            updatingResult.getValue().getCoordinates().getCenterX(),
            insertionResult.getValue().getCoordinates().getCenterX());
        assertNotEquals(
            updatingResult.getValue().getCoordinates().getCenterY(),
            insertionResult.getValue().getCoordinates().getCenterY());
        assertNotEquals(
            updatingResult.getValue().getSize().getWidth(), insertionResult.getValue().getSize().getWidth());
        assertNotEquals(
            updatingResult.getValue().getSize().getHeight(), insertionResult.getValue().getSize().getHeight());

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
        var insertionResult = widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var deletingResult = widgetRepository.v1Delete(insertionResult.getValue().getId());

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

    private static V1InsertWidgetModel generateV1InsertWidgetModel() {
        return new V1InsertWidgetModel(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    private static V1UpdateWidgetModel generateV1UpdateWidgetModel(UUID id) {
        return new V1UpdateWidgetModel(
            id,
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }
}
