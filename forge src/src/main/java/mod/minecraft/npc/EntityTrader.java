package mod.minecraft.npc;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class EntityTrader extends EntityVillager{
	public EntityTrader(World par1World){
		super(par1World);
		this.setSize(0.9F, 1.3F);
		this.tasks.addTask(5, new EntityAILookIdle(this));
	}
	
	@Override
	protected void applyEntityAttributes(){
		double MAXHEALTH = 2147483647.0;
		double MOVEMENT = 0.0;
		double KNOCKBACK = 1.0;
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAXHEALTH);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT);
		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(KNOCKBACK);	
	}
	
	public boolean isAIEnable(){
		return true;
	}
	
	@Override
	public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
        boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;

        if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !par1EntityPlayer.isSneaking())
        {
            if (!this.worldObj.isRemote)
            {
                this.setCustomer(par1EntityPlayer);
                par1EntityPlayer.displayGUIMerchant(this, "Hank");
            }

            return true;
        }
        else
        {
            return super.interact(par1EntityPlayer);
        }
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData){
		par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
		this.setProfession(6);
		this.setCustomNameTag("Hank");
		
		return par1EntityLivingData;
	}
	
	
}
