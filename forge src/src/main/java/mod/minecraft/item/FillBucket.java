package mod.minecraft.item;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class FillBucket {
	public static FillBucket INSTANCE = new FillBucket();
	public Map<Block, Item> buckets = new HashMap<Block, Item>();

    public ItemStack result;

    private FillBucket(){
    }
    
    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event){
    	
    	ItemStack result = fillCustomBucket(event.world, event.target);
    	
    	if(result == null){
    		return;
    	}
    	
    	event.result = result;
    	event.setResult(Result.ALLOW);
    }
    
    private ItemStack fillCustomBucket(World world, MovingObjectPosition pos){
    	Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
    	
    	Item bucket = buckets.get(block);
    	
    	if(block != Blocks.water){
    		return null;
    	}
    	else
		goldNuggetGen.checkForNugget();    		
		return new ItemStack(bucket);
    }
}
