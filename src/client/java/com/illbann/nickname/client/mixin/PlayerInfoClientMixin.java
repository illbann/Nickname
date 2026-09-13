package com.illbann.nickname.client.mixin;

import com.illbann.nickname.client.NicknameClient;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class PlayerInfoClientMixin {
    @Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
    private void nickname$tabListDisplayName(CallbackInfoReturnable<Component> cir) {
        PlayerInfo info = (PlayerInfo) (Object) this;
        String nickname = NicknameClient.getNickname(info.getProfile().getId());
        if (nickname != null) {
            cir.setReturnValue(Component.literal(nickname));
        }
    }
}
