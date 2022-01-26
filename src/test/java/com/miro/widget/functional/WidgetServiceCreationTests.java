package com.miro.widget.functional;

import com.miro.widget.mappers.BllAndDalMapperImpl;
import com.miro.widget.service.WidgetService;
import com.miro.widget.service.WidgetServiceImpl;
import com.miro.widget.service.repositories.InMemoryRepositoryImpl;
import com.miro.widget.service.repositories.WidgetRepository;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.miro.widget.helpers.Generator.convertFromV1WidgetDtoToV1WidgetDto;
import static com.miro.widget.helpers.Generator.generateV1CreateWidgetDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceCreationTests {
    WidgetRepository widgetRepository;
    WidgetService widgetService;

    @BeforeEach
    public void setUp() {
        widgetRepository = new InMemoryRepositoryImpl(new BllAndDalMapperImpl());
        widgetService = new WidgetServiceImpl(widgetRepository);
    }

    @AfterEach
    public void tearDown() {
        widgetRepository.v1DeleteAll();
    }

    @Test
    public void should_successfully_insert_to_the_end_without_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var getAllResult = widgetService.v1GetRange(1, 10);

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_end_with_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var insertedWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        fourthWidget = convertFromV1WidgetDtoToV1WidgetDto(fourthWidget, fourthWidget.getZ() + 1);

        var getAllResult = widgetService.v1GetRange(1, 10);

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, insertedWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_beginning_without_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var insertedWidget = widgetService.v1Create(generateV1CreateWidgetDto(-4)).getValue();

        var getAllResult = widgetService.v1GetRange(1, 10);

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets())
            .isEqualTo(List.of(insertedWidget, firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_beginning_with_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var insertedWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();

        firstWidget = convertFromV1WidgetDtoToV1WidgetDto(firstWidget, firstWidget.getZ() + 1);
        secondWidget = convertFromV1WidgetDtoToV1WidgetDto(secondWidget, secondWidget.getZ() + 1);
        thirdWidget = convertFromV1WidgetDtoToV1WidgetDto(thirdWidget, thirdWidget.getZ() + 1);
        fourthWidget = convertFromV1WidgetDtoToV1WidgetDto(fourthWidget, fourthWidget.getZ() + 1);

        var getAllResult = widgetService.v1GetRange(1, 10);

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(insertedWidget, firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_middle_without_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(5)).getValue();

        var insertedWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();

        var getAllResult = widgetService.v1GetRange(1, 10);

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets())
            .isEqualTo(List.of(firstWidget, secondWidget, insertedWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_insert_to_the_middle_with_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var insertedWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();

        secondWidget = convertFromV1WidgetDtoToV1WidgetDto(secondWidget, secondWidget.getZ() + 1);
        thirdWidget = convertFromV1WidgetDtoToV1WidgetDto(thirdWidget, thirdWidget.getZ() + 1);
        fourthWidget = convertFromV1WidgetDtoToV1WidgetDto(fourthWidget, fourthWidget.getZ() + 1);

        var getAllResult = widgetService.v1GetRange(1, 10);

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().getWidgets())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, insertedWidget, secondWidget, thirdWidget, fourthWidget));
    }
}
