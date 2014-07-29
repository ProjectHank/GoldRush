package mod.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBucket;

public class itemBucketGoldFinder extends ItemBucket{
	
	private Block isFilled;
	private static final String __OBFID = "CL_00000000"; 

	public itemBucketGoldFinder(Block par1){
		//Declaring the stacksize, to make sure the bucket is filled and place the item into a slot of the creative GUI
		super(par1);
		this.maxStackSize = 1;
		this.isFilled = par1;
		this.setCreativeTab(CreativeTabs.tabTools);
	}
}
