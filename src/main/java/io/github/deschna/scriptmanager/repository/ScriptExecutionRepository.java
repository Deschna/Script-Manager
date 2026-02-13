package io.github.deschna.scriptmanager.repository;

import io.github.deschna.scriptmanager.domain.execution.ScriptExecution;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScriptExecutionRepository extends JpaRepository<ScriptExecution, UUID> {

}
