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

@Mod(modid = BucketGoldPan.MODID, version = BucketGoldPan.VERSION)
public class BucketGoldPan{
	public static final String MODID = "Bucket Item";
	public static final String VERSION = "1.5";
	
	
	@Instance(BucketGoldPan.MODID)
	public static BucketGoldPan bucketitem;
	    
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		//Creating the bucket item
		BucketGoldFinder = new itemBucketGoldFinder(Block.getBlockFromItem(BucketGoldFinder))
		.setMaxStackSize(1)
        .setUnlocalizedName("bucketgoldfinder")
        .setContainerItem(Items.bucket)
        .setTextureName("items:BucketGoldFinder");
		//Declaring the buckets as a fluid container
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(Items.bucket));
		GameRegistry.registerItem(BucketGoldFinder, MODID); 
		//Called the EmptyBucket class if player wants to empty the bucket
		MinecraftForge.EVENT_BUS.register(EmptyBucket.INSTANCE);
		
	}
	
	public static Item BucketGoldFinder;
	
	@EventHandler
	public void load(FMLInitializationEvent event){ 
	}
}
