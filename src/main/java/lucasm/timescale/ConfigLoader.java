package lucasm.timescale;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import static lucasm.timescale.TimescaleMod.*;

final class ConfigLoader {
	private static class Entry {
		public static final String DELAY = "delay";
		public static final String STRIDE = "stride";
	}

	private static class Default {
		public static final String DELAY = String.valueOf(DEFAULT_DELAY);
		public static final String STRIDE = String.valueOf(DEFAULT_STRIDE);
	}

	private final Logger logger;

	public ConfigLoader(Logger logger) {
		this.logger = logger;
	}

	public TimescaleConfig readOrCreateConfig() {
		var path = getConfigPath();
		var props = new Properties();

		try (var file = new FileReader(path.toFile())) {
			props.load(file);
		} catch (FileNotFoundException e) {
			props.setProperty(Entry.DELAY, Default.DELAY);
			props.setProperty(Entry.STRIDE, Default.STRIDE);
		} catch (IOException e) {
			logger.error("Failed to read config file: {}", e.getMessage());
		}

		var config = new TimescaleConfig();
		var delay = props.getProperty(Entry.DELAY, Default.DELAY);
		config.setDelay(Integer.parseInt(delay));

		var stride = props.getProperty(Entry.STRIDE, Default.STRIDE);
		config.setStride(Integer.parseInt(stride));

		saveConfigFile(props, path);

		return config;
	}

	public void updateAndSaveConfig(TimescaleConfig config) {
		assert(config != null);

		var props = new Properties();
		props.setProperty(Entry.DELAY, String.valueOf(config.getDelay()));
		props.setProperty(Entry.STRIDE, String.valueOf(config.getStride()));

		saveConfigFile(props, getConfigPath());
	}

	private void saveConfigFile(Properties props, Path path) {
		try (var file = new FileWriter(path.toFile())) {
			props.store(file, null);
		} catch (IOException e) {
			logWriteError(e.getMessage());
		}
	}

	private Path getConfigPath() {
		var loader = FabricLoader.getInstance();
		return loader.getConfigDir().resolve(MOD_ID + ".properties");
	}

	private void logWriteError(String message) {
		logger.error("Failed to write config file: {}", message);
	}
}
