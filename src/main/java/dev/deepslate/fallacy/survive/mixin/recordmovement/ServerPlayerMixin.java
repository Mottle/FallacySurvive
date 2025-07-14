package dev.deepslate.fallacy.survive.mixin.recordmovement;

import dev.deepslate.fallacy.survive.inject.recordmovement.MovementRecord;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements MovementRecord {

    @Unique
    private float fallacy$recordSprintDistance = 0.0f;

    @Override
    public void fallacy$recordSprint(float value) {
        this.fallacy$recordSprintDistance += value;
    }

    @Override
    public float fallacy$getSprintDistance() {
        return this.fallacy$recordSprintDistance;
    }
}
