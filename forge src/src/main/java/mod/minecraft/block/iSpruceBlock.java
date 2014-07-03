package mod.minecraft.block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
/** iSpruceBlock: Block added to Minecraft
 * 
 * @author Tyler McKillop
 * @version July 1, 2014
 */
public class iSpruceBlock extends Block {
	public iSpruceBlock (Material material) {
		super(material);
	}

	/** registerIcons() - method to add texture to the created blocks.
	 * 
	 * @param par1IconRegister - required to map texture to the block.
	 */
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon(ExampleMod.MODID + ":" + (this.getUnlocalizedName().substring(5)));
	}
}