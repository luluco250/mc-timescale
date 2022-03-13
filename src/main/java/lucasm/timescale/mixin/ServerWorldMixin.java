package lucasm.timescale.mixin;

import lucasm.timescale.TimescaleMod;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
	private int ticksSinceDelay = 0;
	private int ticksToAdd = 0;
	private final TimescaleMod mod = TimescaleMod.getInstance();

	@Shadow
	@Final
	private MinecraftServer server;

	@Shadow
	@Final
	private boolean shouldTickTime;

	@Shadow
	public abstract @NotNull MinecraftServer getServer();

	@Shadow
	@Final
	private ServerWorldProperties worldProperties;

	protected ServerWorldMixin(
		MutableWorldProperties properties,
		RegistryKey<World> registryRef,
		RegistryEntry<DimensionType> registryEntry,
		Supplier<Profiler> profiler,
		boolean isClient,
		boolean debugWorld,
		long seed
	) {
		super(
			properties,
			registryRef,
			registryEntry,
			profiler,
			isClient,
			debugWorld,
			seed
		);
	}

	@Shadow
	public abstract void setTimeOfDay(long timeOfDay);

	/**
	 * @author lucasm
	 * @reason Adds logic to scale the daylight cycle.
	 */
	@Overwrite
	public void tickTime() {
		if (!shouldTickTime) return;

		var time = properties.getTime() + 1L;
		worldProperties.setTime(time);
		worldProperties.getScheduledEvents().processEvents(server, time);

		if (
			!properties
			.getGameRules()
			.getBoolean(GameRules.DO_DAYLIGHT_CYCLE)
		) return;

		var timeOfDay = properties.getTimeOfDay();
		var config = mod.getConfig();

		if (++ticksSinceDelay >= config.getDelay()) {
			ticksSinceDelay = 0;
			ticksToAdd = config.getStride();
			//timeOfDay += config.getStride();
		}

		if (ticksToAdd > 0) {
			timeOfDay += 1L;
			--ticksToAdd;
		}

		setTimeOfDay(timeOfDay);
		updateClientTime(time, timeOfDay);
	}

	private void updateClientTime(long time, long timeOfDay) {
		var packet = new WorldTimeUpdateS2CPacket(
			time,
			timeOfDay,
			worldProperties
			.getGameRules()
			.getBoolean(GameRules.DO_DAYLIGHT_CYCLE)
		);
		getServer()
		.getPlayerManager()
		.sendToDimension(packet, getRegistryKey());
	}
}
