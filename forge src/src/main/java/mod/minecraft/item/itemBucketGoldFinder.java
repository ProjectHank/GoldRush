package mod.minecraft.item;

import javax.swing.Icon;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.minecraft.block.ExampleMod;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class itemBucketGoldFinder extends ItemBucket{
	
	private Block isFilled;
	private static final String __OBFID = "CL_00000000";
	
	private static final ResourceLocation texture = new ResourceLocation(items.MODID, "item/bucketgoldfinder.png"); 

	public itemBucketGoldFinder(Block par1){
		
		super(par1);
		this.maxStackSize = 1;
		this.isFilled = par1;
		this.setCreativeTab(CreativeTabs.tabTools);
	}	
}
