package mod.minecraft.currency;

import static org.junit.Assert.*;
import junit.framework.TestCase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.junit.Test;

import cpw.mods.fml.common.registry.GameData;

public class TestCurrency extends TestCase {
	
	FakeInventory inventory = new FakeInventory();
	
	public void testgetCurrency() {
		FakeMoney gold_nugget = new FakeMoney();
		FakeEmerald emerald = new FakeEmerald();
		
		//Adding non-currency items
		inventory.setInventorySlotContents(1, new ItemStack(emerald,10));
		inventory.setInventorySlotContents(10, new ItemStack(emerald,2));
		inventory.setInventorySlotContents(17, new ItemStack(emerald,53));
		
		//Adding currency (gold nuggets)
		inventory.setInventorySlotContents(0, new ItemStack(gold_nugget,1));
		inventory.setInventorySlotContents(2, new ItemStack(gold_nugget,3));
		inventory.setInventorySlotContents(15, new ItemStack(gold_nugget,12));
		
		int currency = getCurrency(inventory);
		assertEquals(currency, 16);
	}
	
	
	public int getCurrency(FakeInventory inventory) {
		int numGoldNuggets = 0;

		for (int i = 0; i < inventory.getSizeInventory(); i++) 
			if (inventory.getStackInSlot(i) != null) {
				if (((FakeItem) inventory.getStackInSlot(i).getItem()).getFakeUnlocalizedName().equals("goldNugget")) 
					numGoldNuggets += inventory.getStackInSlot(i).stackSize;
			}
		return numGoldNuggets;
	}

}
