package com.miro.widget.controllers.validation.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class Violation {
    @Schema(name = "fieldName", example = "width", description = "name of the field with error")
    private final String fieldName;

    @Schema(name = "message", example = "must be greater than or equal to 1", description = "error message")
    private final String message;

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
