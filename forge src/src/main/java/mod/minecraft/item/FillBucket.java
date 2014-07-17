package mod.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class FillBucket extends PlayerEvent {
	public final ItemStack current;
    public final World world;
    public final MovingObjectPosition target;

    public ItemStack result;

    public FillBucket(EntityPlayer player, ItemStack current, World world, MovingObjectPosition target)
    {
        super(player);
        this.current = current;
        this.world = world;
        this.target = target;
    }

}
