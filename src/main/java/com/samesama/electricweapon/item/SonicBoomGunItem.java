package com.samesama.electricweapon.item;

import com.samesama.electricweapon.energy.EnergyCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

public class SonicBoomGunItem extends Item {
    public static final int ENERGY_PER_SHOT = 1000; // 1発1000FE消費
    public SonicBoomGunItem(Properties props) {
        super(props.stacksTo(1));
    }

    // --- ツールチップ表示 ---
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
            tooltip.add(Component.literal("Energy: " + energy.getEnergyStored() + " / " + energy.getMaxEnergyStored() + " FE")
                    .withStyle(ChatFormatting.AQUA));
        });
        tooltip.add(Component.translatable("tooltip.electricweapon.sonic_boom_gun.desc").withStyle(ChatFormatting.GRAY));
    }

    // --- 右クリック時の処理 ---
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 振りかざすモーションを即座に再生（クライアント/サーバー両側で可）
        player.swing(hand, true);

        // エネルギーのCapabilityを取得
        return stack.getCapability(ForgeCapabilities.ENERGY).map(energy -> {
            // 1. エネルギーが足りるかチェック
            if (energy.getEnergyStored() >= ENERGY_PER_SHOT) {

                // 2. クライアント側でなければ実行（サーバー側で処理）
                if (!level.isClientSide) {
                    // エネルギーを消費（simulate = falseで実際に減らす）
                    if (!player.getAbilities().instabuild) energy.extractEnergy(ENERGY_PER_SHOT, false);

                    // 衝撃波の実行（後述）
                    this.performSonicBoom(level, player);

                    applyRecoil(player);

                    // クールタイム（1.5秒 = 30 ticks）
                    player.getCooldowns().addCooldown(this, 30);
                }

                // 音やアニメーションの再生
                player.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            }

            return InteractionResultHolder.fail(stack);
        }).orElse(InteractionResultHolder.fail(stack));
    }
    private void performSonicBoom(Level level, Player player) {
        float range = 15.0F; // 射程距離
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();

        // 1. 視線の先に向かってパーティクルを出す
        for (int i = 1; i < range; i++) {
            Vec3 particlePos = eyePos.add(lookVec.scale(i));
            // ソニックブーム専用のパーティクル
            ((ServerLevel) level).sendParticles(ParticleTypes.SONIC_BOOM,
                    particlePos.x, particlePos.y, particlePos.z,
                    1, 0, 0, 0, 0);
        }

        // 2. 当たり判定（扇形、または直線上）
        // 視線の先にあるエンティティを探す
        Vec3 reachVec = eyePos.add(lookVec.scale(range));
        AABB searchArea = player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(2.0D);

        for (Entity target : level.getEntities(player, searchArea, e -> e instanceof LivingEntity)) {
            // プレイヤーとターゲットの角度をチェックして、ほぼ正面にいる場合のみヒット
            Vec3 toTarget = target.position().subtract(eyePos).normalize();
            double dotProduct = toTarget.dot(lookVec);

            if (dotProduct > 0.95) { // 約18度以内の狭い範囲
                target.hurt(level.damageSources().sonicBoom(player), 10.0F); // 10ダメージ(ハート5個分)

                // ウォーデン特有のノックバック（垂直方向にも浮かす）
                double xDist = target.getX() - player.getX();
                double zDist = target.getZ() - player.getZ();
                ((LivingEntity) target).knockback(0.5D, xDist, zDist);
                target.setDeltaMovement(target.getDeltaMovement().add(0, 0.3D, 0));
            }
        }
    }

    private void applyRecoil(Player player) {
        Vec3 look = player.getLookAngle();
        // 後方へ少し吹き飛ばす（水平後退＋わずかな上方向）
        player.push(-look.x * 0.6D, 0.05D, -look.z * 0.6D);
        player.hurtMarked = true; // クライアントへ動きの更新を促す
    }

    // 1. Capabilityをアイテムに付与
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new EnergyCapabilityProvider(stack, 10000); // 容量10000 FE
    }

    // 2. 耐久値バーをエネルギー残量として表示
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).map(e ->
                Math.round((float)e.getEnergyStored() * 13.0F / (float)e.getMaxEnergyStored())
        ).orElse(0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF55FF; // 電気っぽい色（ピンク〜紫系）
    }
}
