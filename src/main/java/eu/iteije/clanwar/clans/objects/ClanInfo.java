package eu.iteije.clanwar.clans.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class ClanInfo {

    @Getter @Setter
    private String ownerName;
    @Getter private final Map<String, UUID> members;

    public ClanInfo() {
        this.ownerName = "-";
        this.members = new HashMap<>();
    }

    public void addMember(UUID uuid, String name) {
        members.put(name, uuid);
    }

    public void removeMember(String name) {
        members.remove(name);
    }

    public UUID getFromName(String name) {
        return this.members.get(name);
    }

    public String getMembersReadable() {
        if (members.size() == 0) return "-";
        return String.join(", ", members.keySet());
    }

}
