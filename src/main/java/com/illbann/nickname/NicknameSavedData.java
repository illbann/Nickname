package com.illbann.nickname;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NicknameSavedData extends SavedData {
    private final Map<UUID, String> nicknames = new HashMap<>();

    public static NicknameSavedData load(CompoundTag tag) {
        NicknameSavedData data = new NicknameSavedData();
        CompoundTag names = tag.getCompound("nicknames");
        for (String key : names.getAllKeys()) {
            try {
                data.nicknames.put(UUID.fromString(key), names.getString(key));
            } catch (IllegalArgumentException ignored) {
                // Ignore malformed entries from manually edited world data.
            }
        }
        return data;
    }

    public String getNickname(UUID uuid) {
        return nicknames.get(uuid);
    }

    public Map<UUID, String> getNicknames() {
        return nicknames;
    }

    public void setNickname(UUID uuid, String nickname) {
        if (nickname.isEmpty()) {
            nicknames.remove(uuid);
        } else {
            nicknames.put(uuid, nickname);
        }
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag names = new CompoundTag();
        nicknames.forEach((uuid, nickname) -> names.putString(uuid.toString(), nickname));
        tag.put("nicknames", names);
        return tag;
    }
}
