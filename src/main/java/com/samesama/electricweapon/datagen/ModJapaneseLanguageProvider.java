package com.samesama.electricweapon.datagen;

import com.samesama.electricweapon.ElectricWeapon;
import com.samesama.electricweapon.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModJapaneseLanguageProvider extends LanguageProvider {
    public ModJapaneseLanguageProvider(PackOutput output) {
        super(output, ElectricWeapon.MODID, "ja_jp");
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.THUNDER_STICK, "サンダースティック");
        addItem(ModItems.SONIC_BOOM_GUN, "ソニックブームガン");
        addItem(ModItems.WARDEN_CORE, "ウォーデンコア");

        add("itemGroup." + ElectricWeapon.MODID + ".electricweapon_tab", "電気兵器");
    }
}

