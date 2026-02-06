package io.github.deschna.scriptmanager.domain.execution;

import java.util.Optional;
import java.util.UUID;

public interface ScriptExecutionRepository {

    ScriptExecution save(ScriptExecution execution);

    Optional<ScriptExecution> findById(UUID id);
}
