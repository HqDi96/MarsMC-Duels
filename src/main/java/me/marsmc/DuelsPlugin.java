package me.marsmc;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marsmc.database.MySQLConnectionProvider;
import me.marsmc.user.PlayerProfile;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class DuelsPlugin extends JavaPlugin {

    @NonNull
    @Setter
    private MySQLConnectionProvider mySQLConnectionProvider;
    @Setter
    private PlayerProfile playerProfile;


    @Override
    public void onEnable() {
        mySQLConnectionProvider = new MySQLConnectionProvider( this );
        mySQLConnectionProvider.initialize();
        playerProfile = new PlayerProfile( mySQLConnectionProvider );

    }

    @Override
    public void onDisable() {
        if (mySQLConnectionProvider != null) {
            mySQLConnectionProvider.shutdown();
        }
    }
}
