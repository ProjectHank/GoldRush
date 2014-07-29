package mod.minecraft.item;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EmptyBucket {
	public static EmptyBucket INSTANCE = new EmptyBucket();
	//TO declare the map area of the blocks
	public Map<Block, Item> buckets = new HashMap<Block, Item>();

	//Getting the result of the block that the player is plaing down
    public ItemStack result;

    private EmptyBucket(){
    }
    
    //When the player wants to full up the bucket 
    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event){
    	
    	ItemStack result = EmptyCustomBucket(event.world, event.target);
    	
    	if(result == null){
    		return;
    	}
    	
    	event.result = result;
    	event.setResult(Result.ALLOW);
    }
    
    //When the player wants to empty the bucket
    private ItemStack EmptyCustomBucket(World world, MovingObjectPosition pos){
    	Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
    	//Will get the block id and saves it into a variable
    	Item bucket = buckets.get(block);
    	//Check if block is not water, will return nothing
    	if(block != Blocks.water){
    		return null;
    	}
    	//If player is emptying bucket in water, then gold nuggets will return to players inventory
    	else
    		goldNuggetGen.checkForNugget();    		
			return new ItemStack(BucketGoldPan.BucketGoldFinder);
    }
}
