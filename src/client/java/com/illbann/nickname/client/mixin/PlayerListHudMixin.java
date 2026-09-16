package com.illbann.nickname.client.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 저장된 닉네임 데이터 불러오기용 Import (모드 패키지에 맞게 변경)
import com.illbann.nickname.NicknameSavedData;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void modifyTabListName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        // 원래 표시하려던 이름 (기존 서버/클라이언트 표시 이름)
        Text originalName = cir.getReturnValue();
        
        // entry의 UUID 또는 닉네임 저장소 데이터를 참조하여 커스텀 닉네임 가져오기
        String customNickname = NicknameSavedData.getNickname(entry.getProfile().getId());

        if (customNickname != null && !customNickname.isEmpty()) {
            // 변경된 닉네임으로 Tab 목록에 표시할 Text 반환
            cir.setReturnValue(Text.literal(customNickname));
        }
    }
}
