package mod.minecraft.npc;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTrader extends RenderBiped {

		private static final ResourceLocation textureLocation = new ResourceLocation("minecraft:trader/entity/Trader.png");

		public RenderTrader(ModelBiped model, float shadowSize) {
		super(model, shadowSize);
		}

		@Override
		protected ResourceLocation getEntityTexture(Entity par1Entity)
		{
		return textureLocation;
		}
}