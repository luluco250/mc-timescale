package lucasm.timescale;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TimescaleMod implements ModInitializer {
	public static final String MOD_ID = "timescale";
	// Noon: 6000 ticks.
	// Midnight: 18000 ticks.
	private static final int TICKS_IN_A_SECOND = 20;
	public static final int DEFAULT_DELAY = TICKS_IN_A_SECOND * 3;
	public static final int DEFAULT_STRIDE = TICKS_IN_A_SECOND;
	private static TimescaleMod instance;
	private Logger logger;
	private TimescaleConfig config;

	public static TimescaleMod getInstance() {
		return instance;
	}

	@Override
	public void onInitialize() {
		instance = this;
		logger = LoggerFactory.getLogger(MOD_ID);

		var configLoader = new ConfigLoader(logger);
		config = configLoader.readOrCreateConfig();

		var commandManager = new TimescaleCommandManager(configLoader);
		commandManager.registerCommands(config);
	}

	public TimescaleConfig getConfig() {
		return config;
	}

	public Logger getLogger() {
		return logger;
	}
}
