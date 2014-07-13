package mod.minecraft.item;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.block.Block;

@Mod(modid = items.MODID, version = items.VERSION)
public class items {
	public static final String MODID = "Bucket Item";
	public static final String VERSION = "1.0";
	
	@Instance(items.MODID)
	public static items bucketitem;
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		BucketGoldFinder = new itemBucketGoldFinder(Block.getBlockFromItem(BucketGoldFinder))
		.setMaxStackSize(1)
        .setUnlocalizedName("Bucket Gold Finder");
		GameRegistry.registerItem(BucketGoldFinder, MODID);
	}
	
	public static Item BucketGoldFinder;
	
	@EventHandler
	public void load(FMLInitializationEvent event){
		
	}
}
