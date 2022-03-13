package lucasm.timescale.mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
	protected ClientWorldMixin(
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

	/**
	 * @author lucasm
	 * @reason Adds logic to scale the daylight cycle.
	 */
	@Overwrite
	private void tickTime() {
		setTime(properties.getTime() + 1L);
//		if (properties.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
//			setTimeOfDay(properties.getTimeOfDay() + 1L);
//		}
	}

	@Shadow
	public abstract void setTime(long time);

	@Shadow
	public abstract void setTimeOfDay(long timeOfDay);
}
