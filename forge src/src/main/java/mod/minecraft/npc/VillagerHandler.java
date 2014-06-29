package mod.minecraft.npc;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class VillagerHandler implements IVillageTradeHandler{

	public static VillagerHandler INSTANCE = new VillagerHandler();
	
	public void load()
	{
		VillagerRegistry.instance().registerVillageTradeHandler(6, this);
	}
	
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random){	recipeList.add(new MerchantRecipe(new ItemStack(Items.gold_nugget, 5),new ItemStack(Items.iron_pickaxe)));
		manipulateTraderTrades(recipeList);
	}
	
	private void manipulateTraderTrades(MerchantRecipeList recipeList)
	{
		ArrayList<MerchantRecipe> a = new ArrayList<MerchantRecipe>();
		a.add(new MerchantRecipe(
				new ItemStack(Items.gold_nugget, 64),
				new ItemStack(Items.iron_pickaxe)));
		a.add(new MerchantRecipe(
				new ItemStack(Items.gold_nugget, 47),
				new ItemStack(Items.iron_axe)));
		a.add(new MerchantRecipe(
				new ItemStack(Items.gold_nugget, 25),
				new ItemStack(Items.cooked_beef)));
		a.add(new MerchantRecipe(
				new ItemStack(Blocks.gold_block, 1),
				new ItemStack(Items.gold_nugget, 64)));
		a.add(new MerchantRecipe(
				new ItemStack(Blocks.stone, 1),
				new ItemStack(Items.gold_nugget, 5)));
		recipeList.addAll(a);
		/*recipeList.addToListWithCheck(new MerchantRecipe(
				new ItemStack(Items.gold_nugget, 5),
				new ItemStack(Items.iron_pickaxe)));
		recipeList.addToListWithCheck(new MerchantRecipe(
				new ItemStack(Blocks.gold_block, 10),
				new ItemStack(Items.diamond_pickaxe)));
		recipeList.addToListWithCheck(new MerchantRecipe(
				new ItemStack(Blocks.gold_block, 10),
				new ItemStack(Items.wooden_pickaxe)));*/
	}
}
