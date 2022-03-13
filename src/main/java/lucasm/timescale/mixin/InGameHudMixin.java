package lucasm.timescale.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
	@Shadow
	public abstract TextRenderer getTextRenderer();

	@Inject(at = @At("TAIL"), method = "render")
	public void render(
		MatrixStack matrices,
		float tickDelta,
		CallbackInfo info
	) {
		var client = MinecraftClient.getInstance();
		var time = client.world.getTimeOfDay();
		var timeText = String.valueOf(time);
		var textRenderer = getTextRenderer();
		textRenderer.draw(matrices, timeText, 0, 0, 0xffffff);
	}
}
