package dev.deepslate.fallacy.survive.inject.recordmovement;

public interface MovementRecord {

    default void fallacy$setSprint(float value) {
        throw new UnsupportedOperationException("This will not happen");
    }

    default float fallacy$getSprintDistance() {
        throw new UnsupportedOperationException("This will not happen");
    }

    default float fallacy$getAndResetSprintDistance() {
        float cached = fallacy$getSprintDistance();
        fallacy$setSprint(0f);
        return cached;
    }
}
