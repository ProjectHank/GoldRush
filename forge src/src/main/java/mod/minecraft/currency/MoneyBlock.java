package mod.minecraft.currency;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MoneyBlock extends Block {
	
	
	protected MoneyBlock(Material material) {
		super(material);
	}
	
	@Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_) 
	{	
		
	}
	
	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
		Currency test = new Currency();
		test.getCurrency();
		
		return false;
    }
	
	
	

	/*
	 *
	 * if (p_149699_1_.getBlockLightValue(p_149699_2_, p_149699_3_, p_149699_4_) == 0)
			this.setLightLevel(1f);
		else
			this.setLightLevel(0f);
	 * 
	 * 
	 * 
	 * 
	 * p_149727_5_.inventory.addItemStackToInventory(new ItemStack(Blocks.dirt, 10));
	 * 
	 * int stacksize;
		for (int i = 0; i < p_149727_5_.inventory.getSizeInventory(); i++) 
		if (p_149727_5_.inventory.getStackInSlot(i) != null)
			if (p_149727_5_.inventory.getStackInSlot(i).getItem().getUnlocalizedName().equals(Blocks.dirt.getUnlocalizedName())) {
				stacksize = p_149727_5_.inventory.getStackInSlot(i).stackSize;
				p_149727_5_.inventory.setInventorySlotContents(i+1, new ItemStack(Items.emerald,stacksize));
			}
	 * 
	 * 
	 */

}
