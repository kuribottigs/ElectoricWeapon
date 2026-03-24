package com.samesama.electricweapon.modifier;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import com.samesama.electricweapon.item.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, String modid) {
        super(output, modid);
    }

    @Override
    protected void start() {
        // ここでModifierを追加！
        add("warden_core_drop", new AddItemModifier(
                new LootItemCondition[] {
                        // 条件1: ゾンビ
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(EntityType.WARDEN)).build(),
                        // 条件2: 特定のアイテムで倒した
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.DIAMOND_AXE)).build()
                },
                ModItems.WARDEN_CORE.get()
        ));
    }
}
