package me.marsmc.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.marsmc.DuelsPlugin;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

@Getter
@RequiredArgsConstructor
public class MySQLConnectionProvider {

    private final DuelsPlugin duelsPlugin;
    @Setter
    private HikariDataSource dataSource;

    private final HikariConfig configuration;
    @Setter
    private Object plugin;

    public MySQLConnectionProvider(DuelsPlugin duelsPlugin) {
        this.duelsPlugin = duelsPlugin;
        this.configuration = new HikariConfig();
        Properties credentials = getProperties( duelsPlugin );
        configuration.setJdbcUrl(credentials.getProperty("hikari.jdbcUrl"));
        configuration.setDriverClassName(credentials.getProperty("hikari.driverClassName"));
        configuration.setUsername(credentials.getProperty("hikari.username"));
        configuration.setPassword(credentials.getProperty("hikari.password"));
        configuration.setMinimumIdle(Integer.parseInt(credentials.getProperty("hikari.minimumIdle")));
        configuration.setMaximumPoolSize(Integer.parseInt(credentials.getProperty("hikari.maximumPoolSize")));
    }

    private Properties getProperties(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "hikaricp.properties");
        if (!file.exists()) {
            plugin.saveResource("hikaricp.properties", false);
        }
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
        } catch (FileNotFoundException exception) {
            plugin.getLogger().log(Level.WARNING, "Unable to find the hikaricp.properties file.", exception);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public void initialize() {
        if (dataSource == null || dataSource.isClosed()) {
            dataSource = new HikariDataSource(configuration);
        }
    }

    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

}
