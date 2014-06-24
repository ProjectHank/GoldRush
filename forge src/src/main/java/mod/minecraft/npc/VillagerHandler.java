package mod.minecraft.npc;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class VillagerHandler implements IVillageTradeHandler{

	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		recipeList.add(new MerchantRecipe(
				new ItemStack(Items.gold_nugget, 5),
				new ItemStack(Items.iron_pickaxe)));
		recipeList.add(new MerchantRecipe(
				new ItemStack(Blocks.gold_block, 10),
				new ItemStack(Items.diamond_pickaxe)));
	}
}
