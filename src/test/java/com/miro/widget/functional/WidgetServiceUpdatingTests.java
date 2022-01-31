package com.miro.widget.functional;

import com.miro.widget.mappers.WidgetsMapperImpl;
import com.miro.widget.service.WidgetService;
import com.miro.widget.service.WidgetServiceImpl;
import com.miro.widget.service.models.params.UpdateWidgetParams;
import com.miro.widget.service.repositories.InMemoryRepositoryImpl;
import com.miro.widget.service.repositories.WidgetRepository;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.miro.widget.helpers.Generator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceUpdatingTests {
    WidgetRepository widgetRepository;
    WidgetService widgetService;

    @BeforeEach
    public void setUp() {
        widgetRepository = new InMemoryRepositoryImpl();
        widgetService = new WidgetServiceImpl(new WidgetsMapperImpl(), widgetRepository);
    }

    @AfterEach
    public void tearDown() {
        widgetRepository.deleteAll();
    }

    @Test
    public void should_successfully_update_middle_element_to_the_first_one_without_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(thirdWidget.getId(), new UpdateWidgetParams(0));

        thirdWidget = convertFromWidgetToWidget(thirdWidget, 0);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(thirdWidget, firstWidget, secondWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_first_element_without_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(firstWidget.getId(), new UpdateWidgetParams(0));

        firstWidget = convertFromWidgetToWidget(firstWidget, 0);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_middle_element_to_the_first_one_with_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(thirdWidget.getId(), new UpdateWidgetParams(1));

        thirdWidget = convertFromWidgetToWidget(thirdWidget, 1);
        firstWidget = convertFromWidgetToWidget(firstWidget, 2);
        secondWidget = convertFromWidgetToWidget(secondWidget, 3);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(thirdWidget, firstWidget, secondWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_first_element_to_the_middle_one_without_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(4));
        var fourthWidget = widgetService.create(generateCreationParams(5));

        var updateWidgetResult = widgetService.update(firstWidget.getId(), new UpdateWidgetParams(3));

        firstWidget = convertFromWidgetToWidget(firstWidget, 3);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(secondWidget, firstWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_last_element_to_the_middle_one_without_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(4));
        var fourthWidget = widgetService.create(generateCreationParams(5));

        var updateWidgetResult = widgetService.update(fourthWidget.getId(), new UpdateWidgetParams(3));

        fourthWidget = convertFromWidgetToWidget(fourthWidget, 3);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, fourthWidget, thirdWidget));
    }

    @Test
    public void should_successfully_update_last_element_to_the_middle_one_with_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(fourthWidget.getId(), new UpdateWidgetParams(2));

        fourthWidget = convertFromWidgetToWidget(fourthWidget, 2);
        secondWidget = convertFromWidgetToWidget(secondWidget, 3);
        thirdWidget = convertFromWidgetToWidget(thirdWidget, 4);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, fourthWidget, secondWidget, thirdWidget));
    }

    @Test
    public void should_successfully_update_first_element_to_the_middle_one_with_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(firstWidget.getId(), new UpdateWidgetParams(3));

        firstWidget = convertFromWidgetToWidget(firstWidget, 3);
        thirdWidget = convertFromWidgetToWidget(thirdWidget, 4);
        fourthWidget = convertFromWidgetToWidget(fourthWidget, 5);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(secondWidget, firstWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_middle_element_to_another_middle_one_with_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(secondWidget.getId(), new UpdateWidgetParams(3));

        secondWidget = convertFromWidgetToWidget(secondWidget, 3);
        thirdWidget = convertFromWidgetToWidget(thirdWidget, 4);
        fourthWidget = convertFromWidgetToWidget(fourthWidget, 5);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_switch_middle_elements() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(thirdWidget.getId(), new UpdateWidgetParams(2));

        thirdWidget = convertFromWidgetToWidget(thirdWidget, 2);
        secondWidget = convertFromWidgetToWidget(secondWidget, 3);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, thirdWidget, secondWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_middle_element_to_the_last_one_without_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(secondWidget.getId(), new UpdateWidgetParams(5));

        secondWidget = convertFromWidgetToWidget(secondWidget, 5);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, thirdWidget, fourthWidget, secondWidget));
    }

    @Test
    public void should_successfully_update_last_element_without_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(fourthWidget.getId(), new UpdateWidgetParams(5));

        fourthWidget = convertFromWidgetToWidget(fourthWidget, 5);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_middle_element_to_the_last_one_with_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var updateWidgetResult = widgetService.update(secondWidget.getId(), new UpdateWidgetParams(4));

        secondWidget = convertFromWidgetToWidget(secondWidget, 4);
        fourthWidget = convertFromWidgetToWidget(fourthWidget, 5);

        var getAllResult = widgetService.findRange(0, 10);

        assertNotNull(updateWidgetResult);
        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, thirdWidget, secondWidget, fourthWidget));
    }
}
