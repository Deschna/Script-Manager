package io.github.deschna.scriptmanager.controller.dto;

import io.github.deschna.scriptmanager.domain.execution.ExecutionStatus;
import java.time.Instant;
import java.util.UUID;

public record ScriptExecutionResponse(
        UUID id,
        String sourceCode,
        ExecutionStatus status,
        String stdout,
        String stderr,
        String stackTrace,
        Instant createdAt,
        Instant startedAt,
        Instant finishedAt
) {

}
