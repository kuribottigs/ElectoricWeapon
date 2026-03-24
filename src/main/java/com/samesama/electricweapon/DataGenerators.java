package com.samesama.electricweapon;

import com.samesama.electricweapon.datagen.ModLanguageProvider;
import com.samesama.electricweapon.datagen.ModJapaneseLanguageProvider;
import com.samesama.electricweapon.modifier.ModGlobalLootModifierProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = ElectricWeapon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // generator.addProvider(event.include)
        generator.addProvider(event.includeServer(),
                new ModGlobalLootModifierProvider(packOutput, ElectricWeapon.MODID));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new ModJapaneseLanguageProvider(packOutput));
    }
}
