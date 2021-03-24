package eu.iteije.clanwar.messages.storage;

import lombok.Getter;

public enum StorageKey {

    PERMISSION_ERROR("permission-error"),
    PROTOCOL_ERROR("protocol-error"),
    INVALID_ARGUMENTS("invalid-arguments"),

    SETSPAWN_SUCCESS("setspawn-success")

    ;

    @Getter private final String path;

    StorageKey(String path) {
        this.path = path;
    }
}
