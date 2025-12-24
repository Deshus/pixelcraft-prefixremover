package ru.pixelcraft.prefixremover;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (player.hasPermission("prefixremover.ignore")) {
            return;
        }

        LuckPerms luckPerms = Main.getInstance().getServer().getServicesManager().load(LuckPerms.class);
        if (luckPerms == null) {
            return;
        }

        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return;
        }
        boolean hasPremiumGroup = user.getNodes(NodeType.INHERITANCE).stream()
                .map(node -> ((InheritanceNode) node).getGroupName())
                .anyMatch(group -> group.equalsIgnoreCase("premium") || group.equalsIgnoreCase("premiumplus"));
        if (!hasPremiumGroup) {
            Node prefixNodeToRemove = null;

            for (Node node : user.getNodes()) {
                if (node.getKey().startsWith("prefix.2147483647.")) {
                    prefixNodeToRemove = node;
                    break;
                }
            }

            if (prefixNodeToRemove != null) {
                user.data().remove(prefixNodeToRemove);
                luckPerms.getUserManager().saveUser(user);
            }
        }
    }
}