package dev.deepslate.fallacy.survive.inject.recordmovement;

public interface MovementRecord {
    default void fallacy$recordSprint(float value) {
        throw new UnsupportedOperationException("This will not happen");
    }

    default float fallacy$getSprintDistance() {
        throw new UnsupportedOperationException("This will not happen");
    }
}
