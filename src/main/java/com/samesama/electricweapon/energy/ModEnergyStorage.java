package com.samesama.electricweapon.energy;

import net.minecraftforge.energy.EnergyStorage;

public class ModEnergyStorage extends EnergyStorage {
    public ModEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int rc = super.receiveEnergy(maxReceive, simulate);
        if (rc > 0 && !simulate) onContentsChanged();
        return rc;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int rc = super.extractEnergy(maxExtract, simulate);
        if (rc > 0 && !simulate) onContentsChanged();
        return rc;
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(energy, getMaxEnergyStored()));
        onContentsChanged();
    }

    public void onContentsChanged() {

    }
}
