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
    CLAN_CREATE_SUCCESS("clan-create-success"),

    CLAN_UNAVAILABLE("clan-unavailable"),
    CLAN_NOT_FOUND("clan-not-found"),
    CLAN_OWNERSHIP_ERROR("clan-ownership-error"),

    CLAN_INFO_CLAN_NAME("clan-info-clan-name"),
    CLAN_INFO_OWNER("clan-info-owner"),
    CLAN_INFO_MEMBERS("clan-info-members"),

    CLAN_TRANSFER_ERROR("clan-transfer-error"),
    CLAN_TRANSFER_SUCCESS("clan-transfer-success"),

    CLAN_DISBAND_CONFIRM("clan-disband-confirm"),
    CLAN_DISBAND_SUCCESS("clan-disband-success")

    ;

    @Getter private final String path;

    StorageKey(String path) {
        this.path = path;
    }
}
