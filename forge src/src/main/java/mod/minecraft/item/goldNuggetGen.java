package mod.minecraft.item;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import mod.minecraft.npc.Trader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

import java.util.Random;

public class goldNuggetGen {
	public void checkForNugget(){
		Minecraft mc = Minecraft.getMinecraft();
		int nuggetCount = 0;
		//Create random num generator. 1/20 chance of Getting a Nugget.
		Random randGen = new Random();
		int randNum = randGen.nextInt(20);
		boolean done= false; // to keep track of if nugget is added
		System.out.println(randNum);
		//If number is correct, search inventory for gold nugget stack
		if (randNum == 7){
			for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
				if (mc.thePlayer.inventory.getStackInSlot(i) != null){
					if (mc.thePlayer.inventory.getStackInSlot(i).getItem().getUnlocalizedName().equals(Items.gold_nugget.getUnlocalizedName())){ 
						nuggetCount = mc.thePlayer.inventory.getStackInSlot(i).stackSize;
						if (nuggetCount < 64) { 	//max inventory stack size
							// replaces non full slot of gold nugget with stack of +1 size
							mc.thePlayer.inventory.setInventorySlotContents(i, new ItemStack(Items.gold_nugget, nuggetCount+1));
							done = true;
							break;
						}
					}
				}
			}
			if (done == false){ // No non-full slots with gold nuggets,create new stack 
				mc.thePlayer.inventory.addItemStackToInventory(new ItemStack(Items.gold_nugget, 1));
			}
		}			
	}
}
