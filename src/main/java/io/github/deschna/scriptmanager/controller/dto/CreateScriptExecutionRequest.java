package io.github.deschna.scriptmanager.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateScriptExecutionRequest(
        @NotBlank String sourceCode
) {

}
