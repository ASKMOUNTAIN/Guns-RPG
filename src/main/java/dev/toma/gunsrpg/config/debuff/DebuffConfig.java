package dev.toma.gunsrpg.config.debuff;

import dev.toma.configuration.config.Configurable;

public final class DebuffConfig {

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("How much of debuff progression is added when catching already existing type again")
    public int additionalDebuffIncrement = 10;

    @Configurable
    @Configurable.Comment("Disables poison debuff")
    public boolean disablePoison = false;

    @Configurable
    @Configurable.Comment("Disables infection debuff")
    public boolean disableInfection = false;

    @Configurable
    @Configurable.Comment("Disables fracture debuff")
    public boolean disableFractures = false;

    @Configurable
    @Configurable.Comment("Disables bleeding debuff")
    public boolean disableBleeding = false;

    @Configurable
    @Configurable.Comment("Disables reduced health debuff after respawn")
    public boolean disableRespawnDebuff = false;
}
