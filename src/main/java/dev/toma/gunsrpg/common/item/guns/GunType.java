package dev.toma.gunsrpg.common.item.guns;

public enum GunType {

    PISTOL(AmmoType._9MM),
    SMG(AmmoType._45ACP),
    AR(AmmoType._556MM),
    SR(AmmoType._762MM),
    SG(AmmoType._12G);

    private final AmmoType ammoType;

    GunType(AmmoType ammoType) {
        this.ammoType = ammoType;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }
}
