package com.samesama.electricweapon.item;

import com.samesama.electricweapon.ElectricWeapon;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ElectricWeapon.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static RegistryObject<Item> registerItem(String name, Supplier<? extends Item> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }

    private static RegistryObject<Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    public static final RegistryObject<Item> THUNDER_STICK = registerItem("thunder_stick", () -> new ThunderStickItem(new Item.Properties()));
    public static final RegistryObject<Item> SONIC_BOOM_GUN = registerItem("sonic_boom_gun", () -> new SonicBoomGunItem(new Item.Properties()));
    public static final RegistryObject<Item> WARDEN_CORE = registerItem("warden_core", () -> new WardenCoreItem());
}
