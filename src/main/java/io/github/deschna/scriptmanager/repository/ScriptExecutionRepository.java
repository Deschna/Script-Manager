package io.github.deschna.scriptmanager.repository;

import io.github.deschna.scriptmanager.domain.execution.ScriptExecution;
import java.util.Optional;
import java.util.UUID;

public interface ScriptExecutionRepository {

    ScriptExecution save(ScriptExecution execution);

    Optional<ScriptExecution> findById(UUID id);
}
