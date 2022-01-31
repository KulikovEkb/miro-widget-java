package com.miro.widget;

import com.miro.widget.exceptions.WidgetNotFoundException;
import com.miro.widget.service.repositories.InMemoryRepositoryImpl2;
import com.miro.widget.service.repositories.WidgetRepository2;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.miro.widget.helpers.Generator2.generateWidgetForInsert;
import static com.miro.widget.helpers.Generator2.generateWidgetForUpdate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2-repository")
public class H2RepositoryTests {
    @Autowired
    private WidgetRepository2 widgetRepository;

    @AfterEach
    public void tearDown() {
        widgetRepository.deleteAll();
    }

    @Test
    public void should_successfully_insert() {
        var widget = generateWidgetForInsert();
        var insertedWidget = widgetRepository.save(widget);

        assertNotNull(insertedWidget);
        assertEquals(insertedWidget.getZ(), widget.getZ());
        assertEquals(insertedWidget.getCenterX(), widget.getCenterX());
        assertEquals(insertedWidget.getCenterY(), widget.getCenterY());
        assertEquals(insertedWidget.getWidth(), widget.getWidth());
        assertEquals(insertedWidget.getHeight(), widget.getHeight());
        assertNotNull(insertedWidget.getUpdatedAt());

        assertThat(insertedWidget)
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.findById(insertedWidget.getId()).get());
    }

    @Test
    public void should_successfully_get_by_ID() {
        var insertedWidget = widgetRepository.save(generateWidgetForInsert());

        var getByIdResult = widgetRepository.findById(insertedWidget.getId());

        assertTrue(getByIdResult.isPresent());
        assertThat(getByIdResult.get()).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_be_empty_when_failed_to_get_by_ID() {
        widgetRepository.save(generateWidgetForInsert());

        var getByIdResult = widgetRepository.findById(UUID.randomUUID());

        assertTrue(getByIdResult.isEmpty());
        assertThrows(NoSuchElementException.class, getByIdResult::get);
    }

    @Test
    public void should_successfully_get_by_Z_index() {
        var insertedWidget = widgetRepository.save(generateWidgetForInsert());

        var getByZIndexResult = widgetRepository.findByZ(insertedWidget.getZ());

        assertTrue(getByZIndexResult.isPresent());
        assertThat(getByZIndexResult.get()).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_be_empty_when_failed_to_get_by_Z_index() {
        widgetRepository.save(generateWidgetForInsert());

        var getByZResult = widgetRepository.findByZ(RandomUtils.nextInt());

        assertTrue(getByZResult.isEmpty());
        assertThrows(NoSuchElementException.class, getByZResult::get);
    }

    @Test
    public void should_successfully_get_all_inserted_widgets() {
        var insertedWidget = widgetRepository.save(generateWidgetForInsert());

        var widgetsPage = widgetRepository.findAll(PageRequest.of(0, 10)).getContent();

        assertThat(widgetsPage.size()).isEqualTo(1);
        assertThat(widgetsPage.get(0)).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_successfully_update() {
        var insertedWidget = widgetRepository.save(generateWidgetForInsert());

        var updatingModel = generateWidgetForUpdate(insertedWidget.getId());
        var updatingResult = widgetRepository.save(updatingModel);

        assertNotNull(updatingResult);
        assertThat(updatingResult.getId()).isEqualTo(insertedWidget.getId());
        assertEquals(updatingResult.getZ(), updatingModel.getZ());
        assertEquals(updatingResult.getCenterX(), updatingModel.getCenterX());
        assertEquals(updatingResult.getCenterY(), updatingModel.getCenterY());
        assertEquals(updatingResult.getWidth(), updatingModel.getWidth());
        assertEquals(updatingResult.getHeight(), updatingModel.getHeight());

        assertNotEquals(updatingResult.getZ(), insertedWidget.getZ());
        assertNotEquals(updatingResult.getCenterX(), insertedWidget.getCenterX());
        assertNotEquals(updatingResult.getCenterY(), insertedWidget.getCenterY());
        assertNotEquals(updatingResult.getWidth(), insertedWidget.getWidth());
        assertNotEquals(updatingResult.getHeight(), insertedWidget.getHeight());

        assertThat(updatingResult)
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.findById(updatingModel.getId()).get());
        assertThat(updatingResult)
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.findByZ(updatingModel.getZ()).get());
        assertThat(updatingResult)
            .usingRecursiveComparison()
            .isEqualTo(widgetRepository.findAll(PageRequest.of(0, 10)).getContent().get(0));
    }

    @Test
    public void should_successfully_delete_by_ID() {
        var insertedWidget = widgetRepository.save(generateWidgetForInsert());

        widgetRepository.deleteById(insertedWidget.getId());

        var getAllResult = widgetRepository.findAll(PageRequest.of(0, 10));

        assertFalse(getAllResult.hasContent());
    }

    @Test
    public void should_be_failed_when_failed_to_delete_by_ID() {
        var insertedWidget = widgetRepository.save(generateWidgetForInsert());

        assertThrows(EmptyResultDataAccessException.class, () -> widgetRepository.deleteById(UUID.randomUUID()));

        var widgetsPage = widgetRepository.findAll(PageRequest.of(0, 10)).getContent();

        assertThat(widgetsPage.size()).isEqualTo(1);
        assertThat(widgetsPage.get(0)).usingRecursiveComparison().isEqualTo(insertedWidget);
    }

    @Test
    public void should_successfully_delete_all() {
        widgetRepository.save(generateWidgetForInsert());

        widgetRepository.deleteAll();

        var getAllResult = widgetRepository.findAll(PageRequest.of(0, 10));

        assertFalse(getAllResult.hasContent());
    }
}
