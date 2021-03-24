package eu.iteije.clanwar.messages.storage;

import lombok.Getter;

public enum StorageKey {

    PERMISSION_ERROR("permission-error"),
    PROTOCOL_ERROR("protocol-error"),
    INVALID_ARGUMENTS("invalid-arguments"),
    EXECUTE_ERROR("execute-error"),

    SPAWN_SET_SUCCESS("spawn-set-success"),
    SPAWN_TP_NOT_FOUND("spawn-tp-not-found"),

    CLAN_CREATE_EXISTS("clan-create-exists"),
    CLAN_CREATE_UNAVAILABLE("clan-create-unavailable"),
    CLAN_CREATE_SUCCESS("clan-create-success")

    ;

    @Getter private final String path;

    StorageKey(String path) {
        this.path = path;
    }
}
