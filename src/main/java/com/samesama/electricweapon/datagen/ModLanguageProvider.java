package com.samesama.electricweapon.datagen;

import com.samesama.electricweapon.ElectricWeapon;
import com.samesama.electricweapon.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, ElectricWeapon.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Items
        addItem(ModItems.THUNDER_STICK, "Thunder Stick");
        addItem(ModItems.SONIC_BOOM_GUN, "Sonic Boom Gun");
        addItem(ModItems.WARDEN_CORE, "Warden Core");

        // Creative tab
        add("itemGroup." + ElectricWeapon.MODID + ".electricweapon_tab", "Electric Weapon");
    }
}
