package io.github.deschna.scriptmanager.domain.execution;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "script_executions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScriptExecution {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "source_code", nullable = false, columnDefinition = "TEXT")
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutionStatus status;

    @Column(columnDefinition = "TEXT")
    private String stdout;

    @Column(columnDefinition = "TEXT")
    private String stderr;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
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
