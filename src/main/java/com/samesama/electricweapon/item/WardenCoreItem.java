package com.samesama.electricweapon.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import javax.annotation.Nullable;
import java.util.List;

public class WardenCoreItem extends Item {
    private static final int DEFAULT_LIFETIME_TICKS = 20 * 60; // 60 seconds until the item expires
    private static final String LIFETIME_KEY = "LifetimeTicks";

    public WardenCoreItem(){
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal(getRemainingLifetime(stack) + " / " + DEFAULT_LIFETIME_TICKS + " FE")
                    .withStyle(ChatFormatting.AQUA));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        if (level.isClientSide) {
            return; // server側のみでカウントダウン
        }
        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return; // クリエイティブは消耗させない
        }

        int remaining = getRemainingLifetime(stack);
        if (remaining <= 0) {
            stack.shrink(1); // 既に寿命切れなら削除
            
            return;
        }

        setRemainingLifetime(stack, remaining - 1); // 1tick消費
        if (getRemainingLifetime(stack) <= 0) {
            stack.shrink(1); // 寿命切れで消滅
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        float ratio = (float)getRemainingLifetime(stack) / DEFAULT_LIFETIME_TICKS;
        return Math.round(Mth.clamp(ratio, 0.0F, 1.0F) * 13.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float ratio = (float)getRemainingLifetime(stack) / DEFAULT_LIFETIME_TICKS;
        return Mth.hsvToRgb(Mth.clamp(ratio, 0.0F, 1.0F) / 3.0F, 1.0F, 1.0F);
    }

    private int getRemainingLifetime(ItemStack stack) {
        return stack.getOrCreateTag().getInt(LIFETIME_KEY) == 0
                ? initializeLifetime(stack)
                : stack.getOrCreateTag().getInt(LIFETIME_KEY);
    }

    private int initializeLifetime(ItemStack stack) {
        setRemainingLifetime(stack, DEFAULT_LIFETIME_TICKS);
        return DEFAULT_LIFETIME_TICKS;
    }

    private void setRemainingLifetime(ItemStack stack, int ticks) {
        stack.getOrCreateTag().putInt(LIFETIME_KEY, Mth.clamp(ticks, 0, DEFAULT_LIFETIME_TICKS));
    }

}