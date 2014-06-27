package mod.minecraft.npc.proxy;

import mod.minecraft.npc.EntityTrader;
import mod.minecraft.npc.RenderTrader;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void registerRenderers() {
	RenderingRegistry.registerEntityRenderingHandler(EntityTrader.class, new RenderTrader(new ModelBiped(), 0.5F));
	}
}
