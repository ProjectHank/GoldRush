package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.IIconRegister;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";
    
    @Instance(ExampleMod.MODID)
    public static ExampleMod exampleMod;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
    
    // Original testing block for features
    public static Block awesomeblock;
    // the following are indestructable blocks for making objects
    public static Block iOakBlock;
    public static Block iSpruceBlock;
    public static Block iAcaciaBlock;
    public static Block iBirchBlock;
    public static Block iGlassBlock;
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	
    	// This was the initial test block to be indestructible
    	awesomeblock = new AwesomeBlock(Material.ground)
    		.setHardness(100F)
    		.setResistance(6000F)
    		.setStepSound(Block.soundTypeWood)
    		.setBlockName("awesomeblock")
    		.setCreativeTab(CreativeTabs.tabBlock)
    		.setBlockTextureName(MODID + ":" + "awesomeblock");
    	GameRegistry.registerBlock(awesomeblock, "awesomeblock");
    	
    	// Indestructible Oak Block
    	iOakBlock = new iOakBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeWood)
			.setBlockName("iOakBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iOakBlock");
    	GameRegistry.registerBlock(iOakBlock, "iOakBlock");
    	
    	// Indestructible Spruce Block
    	iSpruceBlock = new iSpruceBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeWood)
			.setBlockName("iSpruceBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iSpruceBlock");
    	GameRegistry.registerBlock(iSpruceBlock, "iSpruceBlock");
    	
    	// Indestructible Acacia Block
    	iAcaciaBlock = new iAcaciaBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeWood)
			.setBlockName("iAcaciaBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iAcaciaBlock");
    	GameRegistry.registerBlock(iAcaciaBlock, "iAcaciaBlock");
    	
    	// Indestructible Birch Block
    	iBirchBlock = new iBirchBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeWood)
			.setBlockName("iBirchBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iBirchBlock");
    	GameRegistry.registerBlock(iBirchBlock, "iBirchBlock");
    	
    	// Indestructible Glass Block
    	iGlassBlock = new iGlassBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeGlass)
			.setBlockName("iGlassBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iGlassBlock");
    	GameRegistry.registerBlock(iGlassBlock, "iGlassBlock");
    }
    	
}
