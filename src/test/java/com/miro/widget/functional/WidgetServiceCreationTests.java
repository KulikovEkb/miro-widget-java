package com.miro.widget.functional;

import com.miro.widget.service.WidgetService2;
import com.miro.widget.service.WidgetServiceImpl2;
import com.miro.widget.service.repositories.InMemoryRepositoryImpl2;
import com.miro.widget.service.repositories.WidgetRepository2;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.miro.widget.helpers.Generator2.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceCreationTests {
    WidgetRepository2 widgetRepository;
    WidgetService2 widgetService;

    @BeforeEach
    public void setUp() {
        widgetRepository = new InMemoryRepositoryImpl2();
        widgetService = new WidgetServiceImpl2(widgetRepository);
    }

    @AfterEach
    public void tearDown() {
        widgetRepository.deleteAll();
    }

    @Test
    public void should_successfully_insert_to_the_end_without_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var getAllResult = widgetService.findRange(0, 10);

        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_end_with_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var insertedWidget = widgetService.create(generateCreationParams(4));

        fourthWidget = convertFromWidgetToWidget(fourthWidget, fourthWidget.getZ() + 1);

        var getAllResult = widgetService.findRange(0, 10);

        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, insertedWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_beginning_without_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var insertedWidget = widgetService.create(generateCreationParams(-4));

        var getAllResult = widgetService.findRange(0, 10);

        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .isEqualTo(List.of(insertedWidget, firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_beginning_with_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var insertedWidget = widgetService.create(generateCreationParams(1));

        firstWidget = convertFromWidgetToWidget(firstWidget, firstWidget.getZ() + 1);
        secondWidget = convertFromWidgetToWidget(secondWidget, secondWidget.getZ() + 1);
        thirdWidget = convertFromWidgetToWidget(thirdWidget, thirdWidget.getZ() + 1);
        fourthWidget = convertFromWidgetToWidget(fourthWidget, fourthWidget.getZ() + 1);

        var getAllResult = widgetService.findRange(0, 10);

        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(insertedWidget, firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_middle_without_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(4));
        var fourthWidget = widgetService.create(generateCreationParams(5));

        var insertedWidget = widgetService.create(generateCreationParams(3));

        var getAllResult = widgetService.findRange(0, 10);

        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .isEqualTo(List.of(firstWidget, secondWidget, insertedWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_middle_with_shift() {
        var firstWidget = widgetService.create(generateCreationParams(1));
        var secondWidget = widgetService.create(generateCreationParams(2));
        var thirdWidget = widgetService.create(generateCreationParams(3));
        var fourthWidget = widgetService.create(generateCreationParams(4));

        var insertedWidget = widgetService.create(generateCreationParams(2));

        secondWidget = convertFromWidgetToWidget(secondWidget, secondWidget.getZ() + 1);
        thirdWidget = convertFromWidgetToWidget(thirdWidget, thirdWidget.getZ() + 1);
        fourthWidget = convertFromWidgetToWidget(fourthWidget, fourthWidget.getZ() + 1);

        var getAllResult = widgetService.findRange(0, 10);

        assertTrue(getAllResult.hasContent());
        assertThat(getAllResult.getContent())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, insertedWidget, secondWidget, thirdWidget, fourthWidget));
    }
}
