package io.github.deschna.scriptmanager.domain.execution;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

public class ScriptExecution {

    @Getter
    private final UUID id;
    @Getter
    private final String sourceCode;

    @Getter
    private ExecutionStatus status;

    @Getter
    private String stdout;
    @Getter
    private String stderr;
    @Getter
    private String stackTrace;

    @Getter
    private final Instant createdAt;
    @Getter
    private Instant startedAt;
    @Getter
    private Instant finishedAt;

    private ScriptExecution(UUID id, String sourceCode) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.sourceCode = Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        this.status = ExecutionStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public static ScriptExecution create(String sourceCode) {
        if (sourceCode == null || sourceCode.isBlank()) {
            throw new IllegalArgumentException("sourceCode must not be blank");
        }
        return new ScriptExecution(UUID.randomUUID(), sourceCode);
    }

    public void start() {
        ensureStatus(ExecutionStatus.PENDING);
        this.status = ExecutionStatus.EXECUTING;
        this.startedAt = Instant.now();
    }

    public void complete(String stdout) {
        ensureStatus(ExecutionStatus.EXECUTING);
        this.status = ExecutionStatus.COMPLETED;
        this.stdout = stdout;
        this.finishedAt = Instant.now();
    }

    public void fail(String stderr, String stackTrace) {
        ensureStatus(ExecutionStatus.EXECUTING);
        this.status = ExecutionStatus.FAILED;
        this.stderr = stderr;
        this.stackTrace = stackTrace;
        this.finishedAt = Instant.now();
    }

    private void ensureStatus(ExecutionStatus expected) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    "Invalid state transition: expected " + expected + " but was " + this.status
            );
        }
    }

}
