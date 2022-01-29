package com.miro.widget;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.widget.controllers.WidgetController;
import com.miro.widget.controllers.models.requests.V1CreateWidgetRequest;
import com.miro.widget.controllers.models.requests.V1UpdateWidgetRequest;
import com.miro.widget.mappers.WebAndBllMapper;
import com.miro.widget.service.WidgetService;
import com.miro.widget.service.models.WidgetRange;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import result.PlainResult;
import result.Result;
import result.errors.Error;
import result.errors.NotFoundError;

import java.util.List;
import java.util.UUID;

import static com.miro.widget.helpers.Generator.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = WidgetController.class)
public class WidgetControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WebAndBllMapper webAndBllMapper;

    @MockBean
    private WidgetService widgetService;

    @Test
    public void v1_create_should_return_201() throws Exception {
        var request = generateV1CreateWidgetRequest();
        var widgetDto = convertToDtoFromCreateRequest(UUID.randomUUID(), request);

        Mockito.when(widgetService.create(any())).thenReturn(Result.Ok(widgetDto));

        mockMvc.perform(post("/api/v1/widgets")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test
    public void v1_create_should_return_500() throws Exception {
        var request = generateV1CreateWidgetRequest();

        Mockito.when(widgetService.create(any())).thenReturn(Result.Fail(new Error("creation error")));

        mockMvc.perform(post("/api/v1/widgets")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void v1_create_should_return_400_for_invalid_center_x_coordinate() throws Exception {
        var request = new V1CreateWidgetRequest(
            null,
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));

        mockMvc.perform(post("/api/v1/widgets")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void v1_create_should_return_400_for_invalid_center_y_coordinate() throws Exception {
        var request = new V1CreateWidgetRequest(
            RandomUtils.nextInt(),
            null,
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));

        mockMvc.perform(post("/api/v1/widgets")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void v1_create_should_return_400_for_invalid_center_z_index() throws Exception {
        var request = new V1CreateWidgetRequest(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            Integer.MAX_VALUE,
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));

        mockMvc.perform(post("/api/v1/widgets")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void v1_create_should_return_400_for_invalid_width() throws Exception {
        var request = new V1CreateWidgetRequest(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            -1,
            RandomUtils.nextInt(1, Integer.MAX_VALUE));

        mockMvc.perform(post("/api/v1/widgets")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void v1_create_should_return_400_for_invalid_height() throws Exception {
        var request = new V1CreateWidgetRequest(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            -2);

        mockMvc.perform(post("/api/v1/widgets")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void v1_get_by_ID_should_return_200() throws Exception {
        var widgetId = UUID.randomUUID();

        Mockito.when(widgetService.getById(widgetId)).thenReturn(Result.Ok(generateV1WidgetDto()));

        mockMvc.perform(get("/api/v1/widgets/" + widgetId)).andExpect(status().isOk());
    }

    @Test
    public void v1_get_by_ID_should_return_404() throws Exception {
        var widgetId = UUID.randomUUID();

        Mockito.when(widgetService.getById(argThat(x -> x.equals(widgetId))))
            .thenReturn(Result.Fail(new NotFoundError("not found")));

        mockMvc.perform(get("/api/v1/widgets/" + widgetId)).andExpect(status().isNotFound());
    }

    @Test
    public void v1_get_by_ID_should_return_500() throws Exception {
        var widgetId = UUID.randomUUID();

        Mockito.when(widgetService.getById(argThat(x -> x.equals(widgetId))))
            .thenReturn(Result.Fail(new Error("get by ID error")));

        mockMvc.perform(get("/api/v1/widgets/" + widgetId)).andExpect(status().isInternalServerError());
    }

    @Test
    public void v1_get_by_ID_should_return_400() throws Exception {
        mockMvc.perform(get("/api/v1/widgets/" + null)).andExpect(status().isBadRequest());
    }

    @Test
    public void v1_get_all_should_return_200() throws Exception {
        Mockito.when(widgetService.getRange(1, 10))
            .thenReturn(Result.Ok(new WidgetRange(1, 1, List.of(generateV1WidgetDto()))));

        mockMvc.perform(get("/api/v1/widgets")).andExpect(status().isOk());
    }

    @Test
    public void v1_get_all_should_return_500() throws Exception {
        Mockito.when(widgetService.getRange(1, 10)).thenReturn(Result.Fail(new Error("get all error")));

        mockMvc.perform(get("/api/v1/widgets")).andExpect(status().isInternalServerError());
    }

    @Test
    public void v1_update_should_return_200() throws Exception {
        var request = generateV1UpdateWidgetRequest();
        var widgetDto = convertToDtoFromUpdateRequest(UUID.randomUUID(), request);

        Mockito.when(widgetService.update(any(), any())).thenReturn(Result.Ok(widgetDto));

        mockMvc.perform(put("/api/v1/widgets/" + widgetDto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    public void v1_update_should_return_404() throws Exception {
        Mockito.when(widgetService.update(any(), any())).thenReturn(Result.Fail(new NotFoundError("not found")));

        mockMvc.perform(put("/api/v1/widgets/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(generateV1UpdateWidgetRequest())))
            .andExpect(status().isNotFound());
    }

    @Test
    public void v1_update_should_return_500() throws Exception {
        Mockito.when(widgetService.update(any(), any())).thenReturn(Result.Fail(new Error("updating error")));

        mockMvc.perform(put("/api/v1/widgets/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(generateV1UpdateWidgetRequest())))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void v1_update_should_return_400_when_all_values_are_null() throws Exception {
        var request = new V1UpdateWidgetRequest(null, null, null, null, null);

        mockMvc.perform(put("/api/v1/widgets/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void v1_update_should_return_400_for_invalid_center_z_index() throws Exception {
        var request = new V1UpdateWidgetRequest(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            Integer.MAX_VALUE,
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            RandomUtils.nextInt(1, Integer.MAX_VALUE));

        mockMvc.perform(put("/api/v1/widgets/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void v1_update_should_return_400_for_invalid_width() throws Exception {
        var request = new V1UpdateWidgetRequest(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            -1,
            RandomUtils.nextInt(1, Integer.MAX_VALUE));

        mockMvc.perform(put("/api/v1/widgets/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void v1_update_should_return_400_for_invalid_height() throws Exception {
        var request = new V1UpdateWidgetRequest(
            RandomUtils.nextInt(),
            RandomUtils.nextInt(),
            RandomUtils.nextInt(0, Integer.MAX_VALUE - 1),
            RandomUtils.nextInt(1, Integer.MAX_VALUE),
            -2);

        mockMvc.perform(put("/api/v1/widgets/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void v1_delete_by_ID_should_return_200() throws Exception {
        var widgetId = UUID.randomUUID();

        Mockito.when(widgetService.delete(widgetId)).thenReturn(PlainResult.Ok());

        mockMvc.perform(delete("/api/v1/widgets/" + widgetId)).andExpect(status().isOk());
    }

    @Test
    public void v1_delete_by_ID_should_return_404() throws Exception {
        var widgetId = UUID.randomUUID();

        Mockito.when(widgetService.delete(widgetId)).thenReturn(PlainResult.Fail(new NotFoundError("not found")));

        mockMvc.perform(delete("/api/v1/widgets/" + widgetId)).andExpect(status().isNotFound());
    }

    @Test
    public void v1_delete_by_ID_should_return_500() throws Exception {
        var widgetId = UUID.randomUUID();

        Mockito.when(widgetService.delete(widgetId)).thenReturn(PlainResult.Fail(new Error("delete error")));

        mockMvc.perform(delete("/api/v1/widgets/" + widgetId)).andExpect(status().isInternalServerError());
    }

    @Test
    public void v1_delete_by_ID_should_return_400() throws Exception {
        mockMvc.perform(delete("/api/v1/widgets/" + null)).andExpect(status().isBadRequest());
    }

}
