package me.marsmc.user;

import lombok.Getter;
import lombok.Setter;
import me.marsmc.database.MySQLConnectionProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class PlayerProfile implements Listener {

    @Setter
    private MySQLConnectionProvider mySQLManager;

    public PlayerProfile(MySQLConnectionProvider mySQLManager) {
        this.mySQLManager = mySQLManager;
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) mySQLManager.getPlugin());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        String playerName = event.getPlayer().getName();
        try (Connection connection = mySQLManager.getDataSource().getConnection()) {
            String sql = "INSERT INTO players (uuid, player_name) VALUES (?, ?) ON DUPLICATE KEY UPDATE player_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid);
                statement.setString(2, playerName);
                statement.setString(3, playerName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
