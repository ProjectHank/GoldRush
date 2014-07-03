package mod.minecraft.currency;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FakeInventory {
	private final int size;
	private ItemStack[] inventory;
	
	
	public FakeInventory() {
		size = 32;
		inventory = new ItemStack[32];
	}
	
	
	 /**
     * Returns the number of slots in the inventory.
     */
    int getSizeInventory() {
    	return size;
    }


    /**
     * Returns the stack in slot i
     */
    ItemStack getStackInSlot(int var1) {
    	return inventory[var1];
    }
    
    
    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    void setInventorySlotContents(int var1, ItemStack var2) {
    	inventory[var1] = var2;
    }
    
	
}
