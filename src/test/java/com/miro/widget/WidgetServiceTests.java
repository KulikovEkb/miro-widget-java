package com.miro.widget;

import com.miro.widget.service.WidgetServiceImpl;
import com.miro.widget.service.models.*;
import com.miro.widget.service.repositories.WidgetRepository;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import result.PlainResult;
import result.Result;
import result.errors.Error;
import result.errors.NotFoundError;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

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

        when(widgetRepository.v1GetByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(widgetDto));

        var creationResult = widgetService.v1Create(creationDto);

        assertTrue(creationResult.isSucceed());
        assertThat(creationResult.getValue()).usingRecursiveComparison().isEqualTo(widgetDto);

        /*verify(widgetRepository, times(1)).v1GetByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ())));*/
    }

    @Test
    public void should_be_failed_when_failed_to_insert() {
        var creationDto = generateV1CreateWidgetDto();

        when(widgetRepository.v1GetByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Fail(new Error("insertion error")));

        var creationResult = widgetService.v1Create(creationDto);

        assertTrue(creationResult.isFailed());
        assertNull(creationResult.getValue());

        /*verify(widgetRepository, times(1)).v1GetByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ())));*/
    }

    @Test
    public void should_be_failed_when_failed_to_insert_because_of_Z_index_max_value_reached() {
        var creationDto = generateV1CreateWidgetDto(Integer.MAX_VALUE - 1);

        when(widgetRepository.v1GetByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(convertToWidgetDtoFromCreationDto(creationDto)));

        widgetService.v1Create(creationDto);

        var creationResult = widgetService.v1Create(generateV1CreateWidgetDto(null));

        assertTrue(creationResult.isFailed());
        assertNull(creationResult.getValue());
        assertThat(creationResult.getError().getMessage()).isEqualTo("Max available Z index value reached");

        /*verify(widgetRepository, times(1)).v1GetByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ())));*/
    }

    @Test
    public void should_successfully_get_by_ID() {
        var widgetDto = generateV1WidgetDto();

        when(widgetRepository.v1GetById(widgetDto.getId())).thenReturn(Result.Ok(widgetDto));

        var getByIdResult = widgetService.v1GetById(widgetDto.getId());

        assertTrue(getByIdResult.isSucceed());
        assertThat(getByIdResult.getValue()).usingRecursiveComparison().isEqualTo(widgetDto);

        //verify(widgetRepository, times(1)).v1GetById(widgetDto.getId());
    }

    @Test
    public void should_be_failed_when_failed_to_get_by_ID() {
        var widgetId = UUID.randomUUID();

        when(widgetRepository.v1GetById(widgetId)).thenReturn(Result.Fail(new NotFoundError("widget not found")));

        var getByIdResult = widgetService.v1GetById(widgetId);

        assertTrue(getByIdResult.isFailed());
        assertNull(getByIdResult.getValue());

        //verify(widgetRepository, times(1)).v1GetById(widgetId);
    }

    @Test
    public void should_successfully_get_all_inserted_widgets() {
        var widgetDto = generateV1WidgetDto();

        when(widgetRepository.v1GetAll()).thenReturn(Result.Ok(List.of(widgetDto)));

        var getAllResult = widgetService.v1GetAll();

        assertTrue(getAllResult.isSucceed());
        assertThat(getAllResult.getValue().size()).isEqualTo(1);
        assertThat(getAllResult.getValue().get(0)).usingRecursiveComparison().isEqualTo(widgetDto);

        //verify(widgetRepository, times(1)).v1GetAll();
    }

    @Test
    public void should_successfully_update_single_existing_widget() {
        var creationDto = generateV1CreateWidgetDto();
        var createdWidget = convertToWidgetDtoFromCreationDto(creationDto);
        var updatingDto = generateV1UpdateWidgetDto();
        var updatedWidget = convertToWidgetDtoFromUpdatingDto(createdWidget.getId(), updatingDto);

        when(widgetRepository.v1GetByZIndex(creationDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1GetByZIndex(updatingDto.getZ()))
            .thenReturn(Result.Fail(new NotFoundError("")));
        when(widgetRepository.v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ()))))
            .thenReturn(Result.Ok(createdWidget));
        when(widgetRepository.v1GetById(createdWidget.getId())).thenReturn(Result.Ok(createdWidget));
        when(widgetRepository.v1Update(argThat(x -> x.getId().equals(createdWidget.getId()))))
            .thenReturn(Result.Ok(updatedWidget));

        widgetService.v1Create(creationDto);

        var updatingResult = widgetService.v1Update(createdWidget.getId(), updatingDto);

        assertTrue(updatingResult.isSucceed());
        assertThat(updatingResult.getValue()).usingRecursiveComparison().isEqualTo(updatedWidget);

        /*verify(widgetRepository, times(1)).v1GetByZIndex(creationDto.getZ());
        verify(widgetRepository, times(1)).v1GetByZIndex(updatingDto.getZ());
        verify(widgetRepository, times(1)).v1Insert(argThat(x -> x.getZ().equals(creationDto.getZ())));
        verify(widgetRepository, times(1)).v1GetById(createdWidget.getId());
        verify(widgetRepository, times(1)).v1Update(argThat(x -> x.getId().equals(createdWidget.getId())));*/
    }

    @Test
    public void should_successfully_delete_by_ID() {
        var widgetId = UUID.randomUUID();

        when(widgetRepository.v1Delete(widgetId)).thenReturn(PlainResult.Ok());

        var deletingResult = widgetRepository.v1Delete(widgetId);

        assertTrue(deletingResult.isSucceed());

        //verify(widgetRepository, times(1)).v1Delete(widgetId);
    }


    /*[Test]
        public async Task Should_successfully_insert_to_the_end_without_shift()
        {
            var fixture = new Fixture();

            var firstWidget =
                await _widgetService.V1Create(fixture.Build<V1CreateWidgetDto>().With(x => x.ZIndex, 1).Create());
            var secondWidget =
                await _widgetService.V1Create(fixture.Build<V1CreateWidgetDto>().With(x => x.ZIndex, 2).Create());
            var thirdWidget =
                await _widgetService.V1Create(fixture.Build<V1CreateWidgetDto>().With(x => x.ZIndex, 3).Create());
            var fourthWidget =
                await _widgetService.V1Create(fixture.Build<V1CreateWidgetDto>().With(x => x.ZIndex, 4).Create());

            var expected = new List<V1WidgetDto>(new[]
            {
                firstWidget.Value,
                secondWidget.Value,
                thirdWidget.Value,
                fourthWidget.Value
            });

            var getAllResult = await _widgetService.V1GetAll();

            getAllResult.Should().BeEquivalentTo(expected);
        }
*/

    /*

    @Test
    public void should_be_failed_when_failed_to_update() {
        widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var updatingModel = generateV1UpdateWidgetModel(UUID.randomUUID());
        var updatingResult = widgetRepository.v1Update(updatingModel);

        assertTrue(updatingResult.isFailed());
        assertTrue(updatingResult.hasError(NotFoundError.class));
    }
    @Test
    public void should_be_failed_when_failed_to_delete_by_ID() {
        widgetRepository.v1Insert(generateV1InsertWidgetModel());

        var deletingResult = widgetRepository.v1Delete(UUID.randomUUID());

        assertTrue(deletingResult.isFailed());
        assertTrue(deletingResult.hasError(NotFoundError.class));
    }*/

    private static V1CreateWidgetDto generateV1CreateWidgetDto() {
        return generateV1CreateWidgetDto(RandomUtils.nextInt());
    }

    private static V1CreateWidgetDto generateV1CreateWidgetDto(Integer z) {
        return new V1CreateWidgetDto(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            z,
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    private static V1WidgetDto generateV1WidgetDto() {
        return new V1WidgetDto(
            UUID.randomUUID(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            new V1CoordinatesDto(RandomUtils.nextInt(), RandomUtils.nextInt()),
            new V1SizeDto(RandomUtils.nextInt(1, Integer.MAX_VALUE), RandomUtils.nextInt(1, Integer.MAX_VALUE)),
            ZonedDateTime.now());
    }

    private static V1WidgetDto convertToWidgetDtoFromCreationDto(V1CreateWidgetDto dto) {
        return new V1WidgetDto(
            UUID.randomUUID(),
            dto.getZ(),
            new V1CoordinatesDto(dto.getCenterX(), dto.getCenterY()),
            new V1SizeDto(dto.getWidth(), dto.getHeight()),
            ZonedDateTime.now());
    }

    private static V1UpdateWidgetDto generateV1UpdateWidgetDto() {
        return new V1UpdateWidgetDto(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));
    }

    private static V1WidgetDto convertToWidgetDtoFromUpdatingDto(UUID id, V1UpdateWidgetDto dto) {
        return new V1WidgetDto(
            id,
            dto.getZ(),
            new V1CoordinatesDto(dto.getCenterX(), dto.getCenterY()),
            new V1SizeDto(dto.getWidth(), dto.getHeight()),
            ZonedDateTime.now());
    }
}
