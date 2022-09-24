package dev.timecoding.snowfieldpvp;

import dev.timecoding.snowfieldpvp.api.Metrics;
import dev.timecoding.snowfieldpvp.config.ConfigHandler;
import dev.timecoding.snowfieldpvp.config.LanguageHandler;
import dev.timecoding.snowfieldpvp.database.DatabaseService;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SnowfieldPVP extends JavaPlugin {

    private SnowfieldPVP instance;
    private ConfigHandler config;
    private LanguageHandler lang;
    private DatabaseService databaseService;

    @Override
    public void onEnable() {
        //Register Instance
        instance = this;
        //Register Handlers
        registerHandlersAndServices();
        //Enable bStats (if enabled in config)
        boolean bstatsenabled = config.getBoolean("bStats");
        int metricsid = 16494;
        if(bstatsenabled){
            Metrics metrics = new Metrics(this, metricsid);
        }
    }

    @Override
    public void onDisable() {
    }

    private void registerHandlersAndServices(){
        //Load Config-File
        config = new ConfigHandler(this);
        config.init();
        //Load Language-File(s)
        lang = new LanguageHandler(this);
        lang.setup();
        //Load DatabaseService
        databaseService = new DatabaseService(this, true);
    }

    private void registerCommands(){

    }

    private void registerListeners(){

    }

    public SnowfieldPVP getInstance() {
        return instance;
    }

    public ConfigHandler getCustomConfig() {
        return config;
    }

    public LanguageHandler getLanguageHandler() {
        return lang;
    }
}
