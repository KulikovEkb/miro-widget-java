package com.miro.widget.controllers.validation.models;


import lombok.Getter;

import java.util.List;
@Getter
public class ValidationErrorResponse {
    private final List<Violation> violations;

    public ValidationErrorResponse(List<Violation> violations) {
        this.violations = violations;
    }
}

