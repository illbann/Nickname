package com.illbann.nickname.mixin;

import com.illbann.nickname.Nickname;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void nickname$displayName(CallbackInfoReturnable<Component> cir) {
        cir.setReturnValue(Nickname.getDisplayName((Player) (Object) this));
    }

}
