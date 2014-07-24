package mod.minecraft.item;

import ibxm.Player;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

@Mod(modid = items.MODID, version = items.VERSION)
public class items{
	public static final String MODID = "Bucket Item";
	public static final String VERSION = "1.5";
	
	
	@Instance(items.MODID)
	public static items bucketitem;
	    
	@EventHandler
	public void init(FMLInitializationEvent event){
		BucketGoldFinder = new itemBucketGoldFinder(Block.getBlockFromItem(BucketGoldFinder))
		.setMaxStackSize(1)
        .setUnlocalizedName("bucketgoldfinder")
        .setContainerItem(Items.bucket)
        .setTextureName(MODID + ":" + "Bucket Gold Finder");
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(Items.bucket));
		GameRegistry.registerItem(BucketGoldFinder, MODID); 
		MinecraftForge.EVENT_BUS.register(FillBucket.INSTANCE);
	}
	
	public static Item BucketGoldFinder;
	
	@EventHandler
	public void load(FMLInitializationEvent event){ 
	}
}
