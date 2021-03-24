package eu.iteije.clanwar.clans.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ClanInfo {

    @Getter @Setter
    private String ownerName;
    private List<String> members;

    public void addMember(String name) {
        members.add(name);
    }

    public void removeMember(String name) {
        members.remove(name);
    }

    public String getMembersReadable() {
        return String.join(", ", members);
    }

}
