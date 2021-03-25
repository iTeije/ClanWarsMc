package eu.iteije.clanwar.clans.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ClanInfo {

    @Getter @Setter
    private String ownerName;
    private final List<String> members;

    public ClanInfo() {
        this.ownerName = "-";
        this.members = new ArrayList<>();
    }

    public void addMember(String name) {
        members.add(name);
    }

    public void removeMember(String name) {
        members.remove(name);
    }

    public String getMembersReadable() {
        if (members.size() == 0) return "-";
        return String.join(", ", members);
    }

}
