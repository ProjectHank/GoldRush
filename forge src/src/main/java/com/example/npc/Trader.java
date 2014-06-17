package mod.minecraft.npc;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;

@Mod(modid = Trader.modid, name = "Trader", version = "1.0")

public class Trader {

	public static final String modid = "Trader";
	
	@EventHandler
	public void load(FMLInitializationEvent event){
		int redColor = (255 << 16);
		int orangeColor = (255 << 16)+ (200 << 8);
		    
		EntityRegistry.registerGlobalEntityID(EntityTrader.class,"Trader",EntityRegistry.findGlobalUniqueEntityId(), redColor,orangeColor);
		EntityRegistry.registerModEntity(EntityTrader.class, "Trader", 1, this, 80, 3, true);
        EntityRegistry.addSpawn(EntityTrader.class, 10, 1, 3, EnumCreatureType.creature, BiomeGenBase.plains);
        LanguageRegistry.instance().addStringLocalization("entity.Trader.name", "Trader");
	
	}
}