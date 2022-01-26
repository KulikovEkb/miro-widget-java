package com.miro.widget.functional;

import com.miro.widget.mappers.BllAndDalMapperImpl;
import com.miro.widget.service.WidgetService;
import com.miro.widget.service.WidgetServiceImpl;
import com.miro.widget.service.models.V1UpdateWidgetDto;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceUpdatingTests {
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
    public void should_successfully_update_middle_element_to_the_first_one_without_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetDto = new V1UpdateWidgetDto(null, null, 0, null, null);
        var updateWidgetResult = widgetService.v1Update(thirdWidget.getId(), updateWidgetDto);

        thirdWidget = convertFromV1WidgetDtoToV1WidgetDto(thirdWidget, 0);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(thirdWidget, firstWidget, secondWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_first_element_without_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetDto = new V1UpdateWidgetDto(null, null, 0, null, null);
        var updateWidgetResult = widgetService.v1Update(firstWidget.getId(), updateWidgetDto);

        firstWidget = convertFromV1WidgetDtoToV1WidgetDto(firstWidget, 0);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_middle_element_to_the_first_one_with_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetDto = new V1UpdateWidgetDto(null, null, 1, null, null);
        var updateWidgetResult = widgetService.v1Update(thirdWidget.getId(), updateWidgetDto);

        thirdWidget = convertFromV1WidgetDtoToV1WidgetDto(thirdWidget, 1);
        firstWidget = convertFromV1WidgetDtoToV1WidgetDto(firstWidget, 2);
        secondWidget = convertFromV1WidgetDtoToV1WidgetDto(secondWidget, 3);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(thirdWidget, firstWidget, secondWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_first_element_to_the_middle_one_without_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(5)).getValue();

        var updateWidgetDto = new V1UpdateWidgetDto(null, null, 3, null, null);
        var updateWidgetResult = widgetService.v1Update(firstWidget.getId(), updateWidgetDto);

        firstWidget = convertFromV1WidgetDtoToV1WidgetDto(firstWidget, 3);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(secondWidget, firstWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_last_element_to_the_middle_one_without_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(5)).getValue();

        var updateWidgetResult = widgetService.v1Update(fourthWidget.getId(), new V1UpdateWidgetDto(3));

        fourthWidget = convertFromV1WidgetDtoToV1WidgetDto(fourthWidget, 3);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, fourthWidget, thirdWidget));
    }

    @Test
    public void should_successfully_update_last_element_to_the_middle_one_with_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetResult = widgetService.v1Update(fourthWidget.getId(), new V1UpdateWidgetDto(2));

        fourthWidget = convertFromV1WidgetDtoToV1WidgetDto(fourthWidget, 2);
        secondWidget = convertFromV1WidgetDtoToV1WidgetDto(secondWidget, 3);
        thirdWidget = convertFromV1WidgetDtoToV1WidgetDto(thirdWidget, 4);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, fourthWidget, secondWidget, thirdWidget));
    }

    @Test
    public void should_successfully_update_first_element_to_the_middle_one_with_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetResult = widgetService.v1Update(firstWidget.getId(), new V1UpdateWidgetDto(3));

        firstWidget = convertFromV1WidgetDtoToV1WidgetDto(firstWidget, 3);
        thirdWidget = convertFromV1WidgetDtoToV1WidgetDto(thirdWidget, 4);
        fourthWidget = convertFromV1WidgetDtoToV1WidgetDto(fourthWidget, 5);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(secondWidget, firstWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_middle_element_to_another_middle_one_with_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetResult = widgetService.v1Update(secondWidget.getId(), new V1UpdateWidgetDto(3));

        secondWidget = convertFromV1WidgetDtoToV1WidgetDto(secondWidget, 3);
        thirdWidget = convertFromV1WidgetDtoToV1WidgetDto(thirdWidget, 4);
        fourthWidget = convertFromV1WidgetDtoToV1WidgetDto(fourthWidget, 5);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_switch_middle_elements() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetResult = widgetService.v1Update(thirdWidget.getId(), new V1UpdateWidgetDto(2));

        thirdWidget = convertFromV1WidgetDtoToV1WidgetDto(thirdWidget, 2);
        secondWidget = convertFromV1WidgetDtoToV1WidgetDto(secondWidget, 3);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, thirdWidget, secondWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_middle_element_to_the_last_one_without_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetResult = widgetService.v1Update(secondWidget.getId(), new V1UpdateWidgetDto(5));

        secondWidget = convertFromV1WidgetDtoToV1WidgetDto(secondWidget, 5);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, thirdWidget, fourthWidget, secondWidget));
    }

    @Test
    public void should_successfully_update_last_element_without_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetResult = widgetService.v1Update(fourthWidget.getId(), new V1UpdateWidgetDto(5));

        fourthWidget = convertFromV1WidgetDtoToV1WidgetDto(fourthWidget, 5);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, secondWidget, thirdWidget, fourthWidget));
    }

    @Test
    public void should_successfully_update_middle_element_to_the_last_one_with_shift() {
        var firstWidget = widgetService.v1Create(generateV1CreateWidgetDto(1)).getValue();
        var secondWidget = widgetService.v1Create(generateV1CreateWidgetDto(2)).getValue();
        var thirdWidget = widgetService.v1Create(generateV1CreateWidgetDto(3)).getValue();
        var fourthWidget = widgetService.v1Create(generateV1CreateWidgetDto(4)).getValue();

        var updateWidgetResult = widgetService.v1Update(secondWidget.getId(), new V1UpdateWidgetDto(4));

        secondWidget = convertFromV1WidgetDtoToV1WidgetDto(secondWidget, 4);
        fourthWidget = convertFromV1WidgetDtoToV1WidgetDto(fourthWidget, 5);

        var getAllResult = widgetService.v1GetAll();

        assertTrue(updateWidgetResult.isSucceed());
        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue())
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("updatedAt").build())
            .isEqualTo(List.of(firstWidget, thirdWidget, secondWidget, fourthWidget));
    }
}
