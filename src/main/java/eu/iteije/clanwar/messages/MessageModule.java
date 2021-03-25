package eu.iteije.clanwar.messages;

import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.resources.PluginFile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageModule {

    private final PluginFile messagesFile;

    public MessageModule(PluginFile messagesFile) {
        this.messagesFile = messagesFile;
    }

    public void send(CommandSender sender, StorageKey key, Replacement... replacements) {
        String message = getMessage(key);
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getKey(), replacement.getReplacement());
        }

        sender.sendMessage(message);
    }

    private String getMessage(StorageKey key) {
        return this.translateColors(messagesFile.getConfiguration().getString(key.getPath()));
    }

    private String translateColors(String input) {
        if (input == null) return ChatColor.translateAlternateColorCodes('&', "&cMessage not found");
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
