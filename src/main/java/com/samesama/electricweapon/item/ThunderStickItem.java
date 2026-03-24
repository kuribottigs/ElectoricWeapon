package com.samesama.electricweapon.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class ThunderStickItem extends Item {
    public ThunderStickItem(Properties properties) {
        super(properties);
    }


    @Override
    public boolean isBarVisible(ItemStack a) {
        return true;
    }
    // 右クリックした時の処理
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) { // サーバー側でのみ実行（重要！）
            BlockHitResult hit = (BlockHitResult) player.pick(20.0D, 0.0F, false);
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, hit.getBlockPos(), MobSpawnType.COMMAND);

            // クールタイムを設定（連続で使用できないようにする）
            player.getCooldowns().addCooldown(this, 60);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
