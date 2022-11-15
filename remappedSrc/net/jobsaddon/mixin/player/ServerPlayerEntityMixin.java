package net.jobsaddon.mixin.player;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.jobsaddon.access.JobsManagerAccess;
import net.jobsaddon.network.JobsServerPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(value = ServerPlayerEntity.class, priority = 1001)
public abstract class ServerPlayerEntityMixin <PlayerPublicKey> extends PlayerEntity {


    private boolean syncTeleportStats;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "playerTick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayerEntity;totalExperience:I", ordinal = 0, shift = At.Shift.BEFORE))
    private void playerTickMixin(CallbackInfo info) {
        if (this.syncTeleportStats)
            JobsServerPacket.writeS2CJobPacket(((JobsManagerAccess) this).getJobsManager(), (ServerPlayerEntity) (Object) this);
    }
}
