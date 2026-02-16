package io.github.deschna.scriptmanager.controller;

import io.github.deschna.scriptmanager.controller.dto.CreateScriptExecutionRequest;
import io.github.deschna.scriptmanager.controller.dto.ScriptExecutionResponse;
import io.github.deschna.scriptmanager.domain.execution.ScriptExecution;
import io.github.deschna.scriptmanager.service.ScriptExecutionService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/executions")
@RequiredArgsConstructor
public class ScriptExecutionController {

    private final ScriptExecutionService service;

    @PostMapping
    public ResponseEntity<ScriptExecutionResponse> create(
            @Valid @RequestBody CreateScriptExecutionRequest request) {

        ScriptExecution execution = service.create(request.sourceCode());
        ScriptExecutionResponse response = toResponse(execution);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(execution.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScriptExecutionResponse> get(@PathVariable UUID id) {
        ScriptExecution execution = service.get(id);
        return ResponseEntity.ok(toResponse(execution));
    }

    private ScriptExecutionResponse toResponse(ScriptExecution execution) {
        return new ScriptExecutionResponse(
                execution.getId(),
                execution.getStatus(),
                execution.getStdout(),
                execution.getStderr(),
                execution.getStackTrace(),
                execution.getCreatedAt(),
                execution.getStartedAt(),
                execution.getFinishedAt()
        );
    }
}
