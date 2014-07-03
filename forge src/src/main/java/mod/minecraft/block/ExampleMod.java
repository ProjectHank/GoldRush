package mod.minecraft.block;
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
/** Exmample Mod -- Class that loads all of our custom classes into Minecraft.
 * 
 * @author Tyler McKillop
 * @param MODID = "examplemod"
 * @param VERSION = "1.0"
 * @version July 1, 2014
 */
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
    public static Block CurrencyBlock;
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	
    	/** awesomeblock -- Testing Block
    	 * 
    	 * @param setHardness = 100
    	 * @param setResistance = 6000
    	 * @param setStepSound = wood
    	 * 
    	 */
    	// This was the initial test block to be indestructible
    	awesomeblock = new AwesomeBlock(Material.ground)
    		.setHardness(100F)
    		.setResistance(6000F)
    		.setStepSound(Block.soundTypeWood)
    		.setBlockName("awesomeblock")
    		.setCreativeTab(CreativeTabs.tabBlock)
    		.setBlockTextureName(MODID + ":" + "awesomeblock");
    	GameRegistry.registerBlock(awesomeblock, "awesomeblock");
    	
    	/** iOakBlock -- Indestructible Block for Oak
    	 * 
    	 * @param setHardness = 100
    	 * @param setResistance = 6000
    	 * @param setStepSound = wood
    	 * 
    	 */
    	// Indestructible Oak Block
    	iOakBlock = new iOakBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeWood)
			.setBlockName("iOakBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iOakBlock");
    	GameRegistry.registerBlock(iOakBlock, "iOakBlock");
    	
    	/** iSpruceBlock -- Indestructible Block for Spruce
    	 * 
    	 * @param setHardness = 100
    	 * @param setResistance = 6000
    	 * @param setStepSound = wood
    	 * 
    	 */
    	// Indestructible Spruce Block
    	iSpruceBlock = new iSpruceBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeWood)
			.setBlockName("iSpruceBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iSpruceBlock");
    	GameRegistry.registerBlock(iSpruceBlock, "iSpruceBlock");
    	
    	/** iAcaciaBlock -- Indestructible Block for Acacia
    	 * 
    	 * @param setHardness = 100
    	 * @param setResistance = 6000
    	 * @param setStepSound = wood
    	 * 
    	 */
    	// Indestructible Acacia Block
    	iAcaciaBlock = new iAcaciaBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeWood)
			.setBlockName("iAcaciaBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iAcaciaBlock");
    	GameRegistry.registerBlock(iAcaciaBlock, "iAcaciaBlock");
    	
    	/** iBirchBlock -- Indestructible Block for Birch
    	 * 
    	 * @param setHardness = 100
    	 * @param setResistance = 6000
    	 * @param setStepSound = wood
    	 * 
    	 */
    	// Indestructible Birch Block
    	iBirchBlock = new iBirchBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeWood)
			.setBlockName("iBirchBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iBirchBlock");
    	GameRegistry.registerBlock(iBirchBlock, "iBirchBlock");
    	
    	/** iGlassBlock -- Indestructible Block for Glass
    	 * 
    	 * @param setHardness = 100
    	 * @param setResistance = 6000
    	 * @param setStepSound = glass
    	 * 
    	 */
    	// Indestructible Glass Block
    	iGlassBlock = new iGlassBlock(Material.ground)
			.setHardness(100F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeGlass)
			.setBlockName("iGlassBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "iGlassBlock");
    	GameRegistry.registerBlock(iGlassBlock, "iGlassBlock");
    	
    	/** CurrencyBlock -- Potentially for trading use
    	 * 
    	 * @param setHardness = 1
    	 * @param setResistance = 6000
    	 * @param setStepSound = metal
    	 * 
    	 */
    	// Currency Block
    	CurrencyBlock = new CurrencyBlock(Material.ground)
			.setHardness(1F)
			.setResistance(6000F)
			.setStepSound(Block.soundTypeMetal)
			.setBlockName("CurrencyBlock")
			.setCreativeTab(CreativeTabs.tabBlock)
			.setBlockTextureName(MODID + ":" + "CurrencyBlock");
    	GameRegistry.registerBlock(CurrencyBlock, "CurrencyBlock");
    }
    	
}
