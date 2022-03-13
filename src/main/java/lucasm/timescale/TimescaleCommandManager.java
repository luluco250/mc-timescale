package lucasm.timescale;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

final class TimescaleCommandManager {
	private final ConfigLoader configLoader;

	public TimescaleCommandManager(ConfigLoader configLoader) {
		this.configLoader = configLoader;
	}

	public void registerCommands(TimescaleConfig config) {
		CommandRegistrationCallback
		.EVENT
		.register((dispatcher, dedicated) -> dispatcher.register(
			literal("timescale")
			.then(
				argument("delay", IntegerArgumentType.integer(1))
				.then(
					argument("stride", IntegerArgumentType.integer(1))
					.executes(context -> {
						var delay = context.getArgument("delay", Integer.class);
						var stride = context.getArgument("stride", Integer.class);
						config.setDelay(delay);
						config.setStride(stride);
						configLoader.updateAndSaveConfig(config);
						context
						.getSource()
						.sendFeedback(getCommandReply(config), false);
						return 0;
					})
				)
			)
			.executes(context -> {
				context
				.getSource()
				.sendFeedback(getCommandReply(config), false);
				return 0;
			})
		));
	}

	private Text getCommandReply(TimescaleConfig config) {
		var reply = String.format(
		"Delay: %d\nStride: %d\nScale: %f",
		config.getDelay(),
		config.getStride(),
		config.calculateTimeScale()
		);
		return Text.of(reply);
	}
}
