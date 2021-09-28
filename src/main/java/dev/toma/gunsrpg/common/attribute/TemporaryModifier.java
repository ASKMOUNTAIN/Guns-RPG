package dev.toma.gunsrpg.common.attribute;

import java.util.UUID;

public class TemporaryModifier extends AttributeModifier implements ITickableModifier {

    private int ticksLeft;

    TemporaryModifier(String uid, IModifierOp op, double value, int ticks) {
        this(UUID.fromString(uid), op, value, ticks);
    }

    TemporaryModifier(UUID uid, IModifierOp op, double value, int ticks) {
        super(uid, op, value);
        this.ticksLeft = ticks;
    }

    @Override
    public void tick() {
        --ticksLeft;
    }

    @Override
    public boolean shouldRemove() {
        return ticksLeft <= 0;
    }
}
