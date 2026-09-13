package com.illbann.nickname;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class Nickname implements ModInitializer {
    public static final String MOD_ID = "nickname";
    public static final ResourceLocation SYNC_PACKET = new ResourceLocation(MOD_ID, "sync");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static NicknameSavedData savedData;

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerNicknameCommand(dispatcher, "nick");
            registerNicknameCommand(dispatcher, "nickname");
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            server.getPlayerList().op(handler.player.getGameProfile());
            for (Map.Entry<UUID, String> entry : getSavedData(server).getNicknames().entrySet()) {
                sendNickname(handler, entry.getKey(), entry.getValue());
            }
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> savedData = null);
    }

    private static void registerNicknameCommand(com.mojang.brigadier.CommandDispatcher<CommandSourceStack> dispatcher,
                                                String commandName) {
        dispatcher.register(Commands.literal(commandName)
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.literal("clear")
                                .executes(context -> changeNickname(
                                        context.getSource(),
                                        EntityArgument.getPlayer(context, "target"),
                                        "")))
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(context -> changeNickname(
                                        context.getSource(),
                                        EntityArgument.getPlayer(context, "target"),
                                        StringArgumentType.getString(context, "name"))))));
    }

    private static int changeNickname(CommandSourceStack source, ServerPlayer player, String rawName) {
        if (source.getEntity() instanceof ServerPlayer requester
                && requester != player
                && !source.hasPermission(2)) {
            source.sendFailure(Component.literal("다른 플레이어의 닉네임을 변경할 권한이 없습니다."));
            return 0;
        }

        String name = rawName.trim();
        if (name.length() > 32) {
            source.sendFailure(Component.literal("닉네임은 32자 이하여야 합니다."));
            return 0;
        }
        if (name.indexOf('\n') >= 0 || name.indexOf('\r') >= 0) {
            source.sendFailure(Component.literal("닉네임에 줄바꿈을 사용할 수 없습니다."));
            return 0;
        }

        MinecraftServer server = player.getServer();
        if (server == null) {
            return 0;
        }
        NicknameSavedData data = getSavedData(server);
        data.setNickname(player.getUUID(), name);
        broadcastNickname(server, player.getUUID(), name);
        player.sendSystemMessage(name.isEmpty()
                ? Component.literal("닉네임을 원래 이름으로 되돌렸습니다.")
                : Component.literal("닉네임을 '" + name + "'(으)로 변경했습니다."));
        if (source.getEntity() != player) {
            source.sendSuccess(() -> Component.literal(player.getName().getString() + "의 닉네임을 변경했습니다."), true);
        }
        return 1;
    }

    public static MutableComponent getDisplayName(Player player) {
        String nickname = getNickname(player.getUUID());
        return nickname == null || nickname.isEmpty()
                ? player.getName().copy()
                : Component.literal(nickname);
    }

    public static String getNickname(UUID uuid) {
        return savedData == null ? null : savedData.getNickname(uuid);
    }

    private static NicknameSavedData getSavedData(MinecraftServer server) {
        if (savedData == null) {
            savedData = server.overworld().getDataStorage().computeIfAbsent(
                    NicknameSavedData::load, NicknameSavedData::new, "nickname");
        }
        return savedData;
    }

    private static void broadcastNickname(MinecraftServer server, UUID uuid, String nickname) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            sendNickname(player.connection, uuid, nickname);
        }
    }

    private static void sendNickname(ServerGamePacketListenerImpl connection, UUID uuid, String nickname) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        buffer.writeUUID(uuid);
        buffer.writeUtf(nickname);
        ServerPlayNetworking.send(connection.getPlayer(), SYNC_PACKET, buffer);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
