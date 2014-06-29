package mod.minecraft.currency;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import mod.minecraft.npc.Trader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;


@Mod(modid = Currency.modid, name = "Currency", version = "1.0")

public class Currency {
	public static final String modid = "Currency";
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		Block moneyBlock = new MoneyBlock(Material.ground)
		.setCreativeTab(CreativeTabs.tabMisc)
		.setBlockUnbreakable()
		.setLightLevel(1f)
		.setBlockName("moneyBlock");
		GameRegistry.registerBlock(moneyBlock, "moneyBlock");
	}
	
	
	@EventHandler
	public void load(FMLInitializationEvent event){

	}
	

	public int getCurrency() {
		Minecraft mc = Minecraft.getMinecraft();
		int numGoldNuggets = 0;
		
		for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) 
			if (mc.thePlayer.inventory.getStackInSlot(i) != null)
				if (mc.thePlayer.inventory.getStackInSlot(i).getItem().getUnlocalizedName().equals(Items.gold_nugget.getUnlocalizedName())) 
					numGoldNuggets += mc.thePlayer.inventory.getStackInSlot(i).stackSize;
		
		System.out.println(numGoldNuggets);
			
		return 0;
	}
	
}

/*
 * //Item item = new Item();
 * 
boolean hasitem = mc.thePlayer.inventory.hasItem(item);
itemstack = mc.thePlayer.inventory.decrStackSize(1,1);
//ItemStack blahhh = mc.thePlayer.inventory.
ItemStack blahhhh mc.thePlayer.inventory.getStackInSlot(this.thePlayer.inventory.currentItem)
*/