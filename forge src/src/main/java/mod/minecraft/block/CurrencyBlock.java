package mod.minecraft.block;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class CurrencyBlock extends Block {
	public CurrencyBlock (Material material) {
		super(material);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon(ExampleMod.MODID + ":" + (this.getUnlocalizedName().substring(5)));
	}
}