package com.illbann.nickname.client.mixin;

import com.illbann.nickname.client.NicknameClient;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerClientMixin {
    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void nickname$name(CallbackInfoReturnable<Component> cir) {
        String nickname = NicknameClient.getNickname(((Player) (Object) this).getUUID());
        if (nickname != null) {
            cir.setReturnValue(Component.literal(nickname));
        }
    }

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void nickname$displayName(CallbackInfoReturnable<Component> cir) {
        String nickname = NicknameClient.getNickname(((Player) (Object) this).getUUID());
        if (nickname != null) {
            cir.setReturnValue(Component.literal(nickname));
        }
    }

}
