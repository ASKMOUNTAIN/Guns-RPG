package dev.toma.gunsrpg.common;

@FunctionalInterface
public interface IShootProps {

    float getInaccuracy();

    default float getDamageMultiplier() {
        return 1.0F;
    }
}