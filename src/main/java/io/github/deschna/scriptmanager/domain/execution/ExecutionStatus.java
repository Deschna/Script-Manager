package io.github.deschna.scriptmanager.domain.execution;

public enum ExecutionStatus {

    PENDING,
    EXECUTING,
    COMPLETED,
    FAILED;

    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED;
    }
}
