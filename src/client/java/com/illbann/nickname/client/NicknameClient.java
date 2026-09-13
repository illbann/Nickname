package com.illbann.nickname.client;

import com.illbann.nickname.Nickname;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NicknameClient implements ClientModInitializer {
    private static final Map<UUID, String> NICKNAMES = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Nickname.SYNC_PACKET, (client, handler, buffer, responseSender) -> {
            UUID uuid = buffer.readUUID();
            String nickname = buffer.readUtf(32);
            client.execute(() -> {
                if (nickname.isEmpty()) {
                    NICKNAMES.remove(uuid);
                } else {
                    NICKNAMES.put(uuid, nickname);
                }
            });
        });
    }

    public static String getNickname(UUID uuid) {
        return NICKNAMES.get(uuid);
    }
}
