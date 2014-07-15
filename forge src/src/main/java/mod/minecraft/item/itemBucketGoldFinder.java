package mod.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.minecraft.block.ExampleMod;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;

public class itemBucketGoldFinder extends ItemBucket{
	
	private Block isFilled;
	private static final String __OBFID = "CL_00000000";

	public itemBucketGoldFinder(Block par1){
		
		super(par1);
		this.maxStackSize = 1;
		this.isFilled = par1;
		this.setCreativeTab(CreativeTabs.tabTools);
	}
		
}
