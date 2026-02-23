package io.github.deschna.scriptmanager.service;

import io.github.deschna.scriptmanager.domain.execution.ScriptExecution;
import io.github.deschna.scriptmanager.exception.ScriptExecutionNotFoundException;
import io.github.deschna.scriptmanager.repository.ScriptExecutionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScriptExecutionService {

    private final ScriptExecutionRepository repository;

    public ScriptExecution create(String sourceCode) {
        ScriptExecution execution = ScriptExecution.create(sourceCode);
        return repository.save(execution);
    }

    @Transactional(readOnly = true)
    public ScriptExecution get(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new ScriptExecutionNotFoundException("Execution not found: id = " + id));
    }
}
