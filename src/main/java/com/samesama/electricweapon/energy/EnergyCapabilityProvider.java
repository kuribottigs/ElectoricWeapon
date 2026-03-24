package com.samesama.electricweapon.energy;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    private final ModEnergyStorage energy;
    private final LazyOptional<IEnergyStorage> lazyEnergy;

    public EnergyCapabilityProvider(ItemStack stack, int capacity) {
        this.energy = new ModEnergyStorage(capacity, capacity) {
            @Override
            public void onContentsChanged() {
                stack.getOrCreateTag().putInt("energy", getEnergyStored());
            }
        };
        this.lazyEnergy = LazyOptional.of(() -> energy);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return ForgeCapabilities.ENERGY.orEmpty(cap, lazyEnergy);
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("energy", energy.getEnergyStored());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        energy.setEnergy(nbt.getInt("energy"));
    }
}
