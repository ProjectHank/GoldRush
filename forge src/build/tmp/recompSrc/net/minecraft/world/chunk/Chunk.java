package net.minecraft.world.chunk;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.ChunkEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk
{
    private static final Logger logger = LogManager.getLogger();
    /**
     * Determines if the chunk is lit or not at a light value greater than 0.
     */
    public static boolean isLit;
    /**
     * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps, and metadata. Each entry corresponds to a
     * logical segment of 16x16x16 blocks, stacked vertically.
     */
    private ExtendedBlockStorage[] storageArrays;
    /**
     * Contains a 16x16 mapping on the X/Z plane of the biome ID to which each colum belongs.
     */
    private byte[] blockBiomeArray;
    /**
     * A map, similar to heightMap, that tracks how far down precipitation can fall.
     */
    public int[] precipitationHeightMap;
    /** Which columns need their skylightMaps updated. */
    public boolean[] updateSkylightColumns;
    /** Whether or not this Chunk is currently loaded into the World */
    public boolean isChunkLoaded;
    /** Reference to the World object. */
    public World worldObj;
    public int[] heightMap;
    /** The x coordinate of the chunk. */
    public final int xPosition;
    /** The z coordinate of the chunk. */
    public final int zPosition;
    private boolean isGapLightingUpdated;
    /** A Map of ChunkPositions to TileEntities in this chunk */
    public Map chunkTileEntityMap;
    /**
     * Array of Lists containing the entities in this Chunk. Each List represents a 16 block subchunk.
     */
    public List[] entityLists;
    /** Boolean value indicating if the terrain is populated. */
    public boolean isTerrainPopulated;
    public boolean isLightPopulated;
    public boolean field_150815_m;
    /**
     * Set to true if the chunk has been modified and needs to be updated internally.
     */
    public boolean isModified;
    /**
     * Whether this Chunk has any Entities and thus requires saving on every tick
     */
    public boolean hasEntities;
    /** The time according to World.worldTime when this chunk was last saved */
    public long lastSaveTime;
    /**
     * Updates to this chunk will not be sent to clients if this is false. This field is set to true the first time the
     * chunk is sent to a client, and never set to false.
     */
    public boolean sendUpdates;
    /** Lowest value in the heightmap. */
    public int heightMapMinimum;
    /** the cumulative number of ticks players have been in this chunk */
    public long inhabitedTime;
    /**
     * Contains the current round-robin relight check index, and is implied as the relight check location as well.
     */
    private int queuedLightChecks;
    private static final String __OBFID = "CL_00000373";

    public Chunk(World par1World, int par2, int par3)
    {
        this.storageArrays = new ExtendedBlockStorage[16];
        this.blockBiomeArray = new byte[256];
        this.precipitationHeightMap = new int[256];
        this.updateSkylightColumns = new boolean[256];
        this.chunkTileEntityMap = new HashMap();
        this.queuedLightChecks = 4096;
        this.entityLists = new List[16];
        this.worldObj = par1World;
        this.xPosition = par2;
        this.zPosition = par3;
        this.heightMap = new int[256];

        for (int k = 0; k < this.entityLists.length; ++k)
        {
            this.entityLists[k] = new ArrayList();
        }

        Arrays.fill(this.precipitationHeightMap, -999);
        Arrays.fill(this.blockBiomeArray, (byte) - 1);
    }

    public Chunk(World p_i45446_1_, Block[] p_i45446_2_, int p_i45446_3_, int p_i45446_4_)
    {
        this(p_i45446_1_, p_i45446_3_, p_i45446_4_);
        int k = p_i45446_2_.length / 256;
        boolean flag = !p_i45446_1_.provider.hasNoSky;

        for (int l = 0; l < 16; ++l)
        {
            for (int i1 = 0; i1 < 16; ++i1)
            {
                for (int j1 = 0; j1 < k; ++j1)
                {
                    Block block = p_i45446_2_[l << 11 | i1 << 7 | j1];

                    if (block != null && block.getMaterial() != Material.air)
                    {
                        int k1 = j1 >> 4;

                        if (this.storageArrays[k1] == null)
                        {
                            this.storageArrays[k1] = new ExtendedBlockStorage(k1 << 4, flag);
                        }

                        this.storageArrays[k1].func_150818_a(l, j1 & 15, i1, block);
                    }
                }
            }
        }
    }

    public Chunk(World p_i45447_1_, Block[] p_i45447_2_, byte[] p_i45447_3_, int p_i45447_4_, int p_i45447_5_)
    {
        this(p_i45447_1_, p_i45447_4_, p_i45447_5_);
        int k = p_i45447_2_.length / 256;
        boolean flag = !p_i45447_1_.provider.hasNoSky;

        for (int l = 0; l < 16; ++l)
        {
            for (int i1 = 0; i1 < 16; ++i1)
            {
                for (int j1 = 0; j1 < k; ++j1)
                {
                    int k1 = l * k * 16 | i1 * k | j1;
                    Block block = p_i45447_2_[k1];

                    if (block != null && block != Blocks.air)
                    {
                        int l1 = j1 >> 4;

                        if (this.storageArrays[l1] == null)
                        {
                            this.storageArrays[l1] = new ExtendedBlockStorage(l1 << 4, flag);
                        }

                        this.storageArrays[l1].func_150818_a(l, j1 & 15, i1, block);
                        this.storageArrays[l1].setExtBlockMetadata(l, j1 & 15, i1, p_i45447_3_[k1]);
                    }
                }
            }
        }
    }

    /**
     * Checks whether the chunk is at the X/Z location specified
     */
    public boolean isAtLocation(int par1, int par2)
    {
        return par1 == this.xPosition && par2 == this.zPosition;
    }

    /**
     * Returns the value in the height map at this x, z coordinate in the chunk
     */
    public int getHeightValue(int par1, int par2)
    {
        return this.heightMap[par2 << 4 | par1];
    }

    /**
     * Returns the topmost ExtendedBlockStorage instance for this Chunk that actually contains a block.
     */
    public int getTopFilledSegment()
    {
        for (int i = this.storageArrays.length - 1; i >= 0; --i)
        {
            if (this.storageArrays[i] != null)
            {
                return this.storageArrays[i].getYLocation();
            }
        }

        return 0;
    }

    /**
     * Returns the ExtendedBlockStorage array for this Chunk.
     */
    public ExtendedBlockStorage[] getBlockStorageArray()
    {
        return this.storageArrays;
    }

    /**
     * Generates the height map for a chunk from scratch
     */
    @SideOnly(Side.CLIENT)
    public void generateHeightMap()
    {
        int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j)
        {
            int k = 0;

            while (k < 16)
            {
                this.precipitationHeightMap[j + (k << 4)] = -999;
                int l = i + 16 - 1;

                while (true)
                {
                    if (l > 0)
                    {
                        Block block = this.getBlock(j, l - 1, k);

                        if (func_150808_b(j, l - 1, k) == 0)
                        {
                            --l;
                            continue;
                        }

                        this.heightMap[k << 4 | j] = l;

                        if (l < this.heightMapMinimum)
                        {
                            this.heightMapMinimum = l;
                        }
                    }

                    ++k;
                    break;
                }
            }
        }

        this.isModified = true;
    }

    /**
     * Generates the initial skylight map for the chunk upon generation or load.
     */
    public void generateSkylightMap()
    {
        int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j)
        {
            int k = 0;

            while (k < 16)
            {
                this.precipitationHeightMap[j + (k << 4)] = -999;
                int l = i + 16 - 1;

                while (true)
                {
                    if (l > 0)
                    {
                        if (this.func_150808_b(j, l - 1, k) == 0)
                        {
                            --l;
                            continue;
                        }

                        this.heightMap[k << 4 | j] = l;

                        if (l < this.heightMapMinimum)
                        {
                            this.heightMapMinimum = l;
                        }
                    }

                    if (!this.worldObj.provider.hasNoSky)
                    {
                        l = 15;
                        int i1 = i + 16 - 1;

                        do
                        {
                            int j1 = this.func_150808_b(j, i1, k);

                            if (j1 == 0 && l != 15)
                            {
                                j1 = 1;
                            }

                            l -= j1;

                            if (l > 0)
                            {
                                ExtendedBlockStorage extendedblockstorage = this.storageArrays[i1 >> 4];

                                if (extendedblockstorage != null)
                                {
                                    extendedblockstorage.setExtSkylightValue(j, i1 & 15, k, l);
                                    this.worldObj.func_147479_m((this.xPosition << 4) + j, i1, (this.zPosition << 4) + k);
                                }
                            }

                            --i1;
                        }
                        while (i1 > 0 && l > 0);
                    }

                    ++k;
                    break;
                }
            }
        }

        this.isModified = true;
    }

    /**
     * Propagates a given sky-visible block's light value downward and upward to neighboring blocks as necessary.
     */
    private void propagateSkylightOcclusion(int par1, int par2)
    {
        this.updateSkylightColumns[par1 + par2 * 16] = true;
        this.isGapLightingUpdated = true;
    }

    private void recheckGaps(boolean p_150803_1_)
    {
        this.worldObj.theProfiler.startSection("recheckGaps");

        if (this.worldObj.doChunksNearChunkExist(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8, 16))
        {
            for (int i = 0; i < 16; ++i)
            {
                for (int j = 0; j < 16; ++j)
                {
                    if (this.updateSkylightColumns[i + j * 16])
                    {
                        this.updateSkylightColumns[i + j * 16] = false;
                        int k = this.getHeightValue(i, j);
                        int l = this.xPosition * 16 + i;
                        int i1 = this.zPosition * 16 + j;
                        int j1 = this.worldObj.getChunkHeightMapMinimum(l - 1, i1);
                        int k1 = this.worldObj.getChunkHeightMapMinimum(l + 1, i1);
                        int l1 = this.worldObj.getChunkHeightMapMinimum(l, i1 - 1);
                        int i2 = this.worldObj.getChunkHeightMapMinimum(l, i1 + 1);

                        if (k1 < j1)
                        {
                            j1 = k1;
                        }

                        if (l1 < j1)
                        {
                            j1 = l1;
                        }

                        if (i2 < j1)
                        {
                            j1 = i2;
                        }

                        this.checkSkylightNeighborHeight(l, i1, j1);
                        this.checkSkylightNeighborHeight(l - 1, i1, k);
                        this.checkSkylightNeighborHeight(l + 1, i1, k);
                        this.checkSkylightNeighborHeight(l, i1 - 1, k);
                        this.checkSkylightNeighborHeight(l, i1 + 1, k);

                        if (p_150803_1_)
                        {
                            this.worldObj.theProfiler.endSection();
                            return;
                        }
                    }
                }
            }

            this.isGapLightingUpdated = false;
        }

        this.worldObj.theProfiler.endSection();
    }

    /**
     * Checks the height of a block next to a sky-visible block and schedules a lighting update as necessary.
     */
    private void checkSkylightNeighborHeight(int par1, int par2, int par3)
    {
        int l = this.worldObj.getHeightValue(par1, par2);

        if (l > par3)
        {
            this.updateSkylightNeighborHeight(par1, par2, par3, l + 1);
        }
        else if (l < par3)
        {
            this.updateSkylightNeighborHeight(par1, par2, l, par3 + 1);
        }
    }

    private void updateSkylightNeighborHeight(int par1, int par2, int par3, int par4)
    {
        if (par4 > par3 && this.worldObj.doChunksNearChunkExist(par1, 0, par2, 16))
        {
            for (int i1 = par3; i1 < par4; ++i1)
            {
                this.worldObj.updateLightByType(EnumSkyBlock.Sky, par1, i1, par2);
            }

            this.isModified = true;
        }
    }

    /**
     * Initiates the recalculation of both the block-light and sky-light for a given block inside a chunk.
     */
    private void relightBlock(int par1, int par2, int par3)
    {
        int l = this.heightMap[par3 << 4 | par1] & 255;
        int i1 = l;

        if (par2 > l)
        {
            i1 = par2;
        }

        while (i1 > 0 && this.func_150808_b(par1, i1 - 1, par3) == 0)
        {
            --i1;
        }

        if (i1 != l)
        {
            this.worldObj.markBlocksDirtyVertical(par1 + this.xPosition * 16, par3 + this.zPosition * 16, i1, l);
            this.heightMap[par3 << 4 | par1] = i1;
            int j1 = this.xPosition * 16 + par1;
            int k1 = this.zPosition * 16 + par3;
            int l1;
            int i2;

            if (!this.worldObj.provider.hasNoSky)
            {
                ExtendedBlockStorage extendedblockstorage;

                if (i1 < l)
                {
                    for (l1 = i1; l1 < l; ++l1)
                    {
                        extendedblockstorage = this.storageArrays[l1 >> 4];

                        if (extendedblockstorage != null)
                        {
                            extendedblockstorage.setExtSkylightValue(par1, l1 & 15, par3, 15);
                            this.worldObj.func_147479_m((this.xPosition << 4) + par1, l1, (this.zPosition << 4) + par3);
                        }
                    }
                }
                else
                {
                    for (l1 = l; l1 < i1; ++l1)
                    {
                        extendedblockstorage = this.storageArrays[l1 >> 4];

                        if (extendedblockstorage != null)
                        {
                            extendedblockstorage.setExtSkylightValue(par1, l1 & 15, par3, 0);
                            this.worldObj.func_147479_m((this.xPosition << 4) + par1, l1, (this.zPosition << 4) + par3);
                        }
                    }
                }

                l1 = 15;

                while (i1 > 0 && l1 > 0)
                {
                    --i1;
                    i2 = this.func_150808_b(par1, i1, par3);

                    if (i2 == 0)
                    {
                        i2 = 1;
                    }

                    l1 -= i2;

                    if (l1 < 0)
                    {
                        l1 = 0;
                    }

                    ExtendedBlockStorage extendedblockstorage1 = this.storageArrays[i1 >> 4];

                    if (extendedblockstorage1 != null)
                    {
                        extendedblockstorage1.setExtSkylightValue(par1, i1 & 15, par3, l1);
                    }
                }
            }

            l1 = this.heightMap[par3 << 4 | par1];
            i2 = l;
            int j2 = l1;

            if (l1 < l)
            {
                i2 = l1;
                j2 = l;
            }

            if (l1 < this.heightMapMinimum)
            {
                this.heightMapMinimum = l1;
            }

            if (!this.worldObj.provider.hasNoSky)
            {
                this.updateSkylightNeighborHeight(j1 - 1, k1, i2, j2);
                this.updateSkylightNeighborHeight(j1 + 1, k1, i2, j2);
                this.updateSkylightNeighborHeight(j1, k1 - 1, i2, j2);
                this.updateSkylightNeighborHeight(j1, k1 + 1, i2, j2);
                this.updateSkylightNeighborHeight(j1, k1, i2, j2);
            }

            this.isModified = true;
        }
    }

    public int func_150808_b(int p_150808_1_, int p_150808_2_, int p_150808_3_)
    {
        int x = (xPosition << 4) + p_150808_1_;
        int z = (zPosition << 4) + p_150808_3_;
        return this.getBlock(p_150808_1_, p_150808_2_, p_150808_3_).getLightOpacity(worldObj, x, p_150808_2_, p_150808_3_);
    }

    /**
     * Returns the block corresponding to the given coordinates inside a chunk.
     */
    public Block getBlock(final int p_150810_1_, final int p_150810_2_, final int p_150810_3_)
    {
        Block block = Blocks.air;

        if (p_150810_2_ >> 4 < this.storageArrays.length)
        {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[p_150810_2_ >> 4];

            if (extendedblockstorage != null)
            {
                try
                {
                    block = extendedblockstorage.getBlockByExtId(p_150810_1_, p_150810_2_ & 15, p_150810_3_);
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
                    crashreportcategory.addCrashSectionCallable("Location", new Callable()
                    {
                        private static final String __OBFID = "CL_00000374";
                        public String call()
                        {
                            return CrashReportCategory.getLocationInfo(p_150810_1_, p_150810_2_, p_150810_3_);
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
        }

        return block;
    }

    /**
     * Return the metadata corresponding to the given coordinates inside a chunk.
     */
    public int getBlockMetadata(int par1, int par2, int par3)
    {
        if (par2 >> 4 >= this.storageArrays.length)
        {
            return 0;
        }
        else
        {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[par2 >> 4];
            return extendedblockstorage != null ? extendedblockstorage.getExtBlockMetadata(par1, par2 & 15, par3) : 0;
        }
    }

    public boolean func_150807_a(int p_150807_1_, int p_150807_2_, int p_150807_3_, Block p_150807_4_, int p_150807_5_)
    {
        int i1 = p_150807_3_ << 4 | p_150807_1_;

        if (p_150807_2_ >= this.precipitationHeightMap[i1] - 1)
        {
            this.precipitationHeightMap[i1] = -999;
        }

        int j1 = this.heightMap[i1];
        Block block1 = this.getBlock(p_150807_1_, p_150807_2_, p_150807_3_);
        int k1 = this.getBlockMetadata(p_150807_1_, p_150807_2_, p_150807_3_);

        if (block1 == p_150807_4_ && k1 == p_150807_5_)
        {
            return false;
        }
        else
        {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[p_150807_2_ >> 4];
            boolean flag = false;

            if (extendedblockstorage == null)
            {
                if (p_150807_4_ == Blocks.air)
                {
                    return false;
                }

                extendedblockstorage = this.storageArrays[p_150807_2_ >> 4] = new ExtendedBlockStorage(p_150807_2_ >> 4 << 4, !this.worldObj.provider.hasNoSky);
                flag = p_150807_2_ >= j1;
            }

            int l1 = this.xPosition * 16 + p_150807_1_;
            int i2 = this.zPosition * 16 + p_150807_3_;

            if (!this.worldObj.isRemote)
            {
                block1.onBlockPreDestroy(this.worldObj, l1, p_150807_2_, i2, k1);
            }

            extendedblockstorage.func_150818_a(p_150807_1_, p_150807_2_ & 15, p_150807_3_, p_150807_4_);
            extendedblockstorage.setExtBlockMetadata(p_150807_1_, p_150807_2_ & 15, p_150807_3_, p_150807_5_); // Move this above to prevent other mods/tile entites from creating invalid ones for the wrong metadata

            if (!this.worldObj.isRemote)
            {
                block1.breakBlock(this.worldObj, l1, p_150807_2_, i2, block1, k1);
            }
            else if (block1.hasTileEntity(k1))
            {
                TileEntity te = this.getTileEntityUnsafe(p_150807_1_ & 0x0F, p_150807_2_, p_150807_3_ & 0x0F);
                if (te != null && te.shouldRefresh(block1, p_150807_4_, k1, p_150807_5_, worldObj, l1, p_150807_2_, i2))
                {
                    this.worldObj.removeTileEntity(l1, p_150807_2_, i2);
                }
            }

            if (extendedblockstorage.getBlockByExtId(p_150807_1_, p_150807_2_ & 15, p_150807_3_) != p_150807_4_)
            {
                return false;
            }
            else
            {
                if (flag)
                {
                    this.generateSkylightMap();
                }
                else
                {
                    int j2 = p_150807_4_.getLightOpacity();
                    int k2 = block1.getLightOpacity();

                    if (j2 > 0)
                    {
                        if (p_150807_2_ >= j1)
                        {
                            this.relightBlock(p_150807_1_, p_150807_2_ + 1, p_150807_3_);
                        }
                    }
                    else if (p_150807_2_ == j1 - 1)
                    {
                        this.relightBlock(p_150807_1_, p_150807_2_, p_150807_3_);
                    }

                    if (j2 != k2 && (j2 < k2 || this.getSavedLightValue(EnumSkyBlock.Sky, p_150807_1_, p_150807_2_, p_150807_3_) > 0 || this.getSavedLightValue(EnumSkyBlock.Block, p_150807_1_, p_150807_2_, p_150807_3_) > 0))
                    {
                        this.propagateSkylightOcclusion(p_150807_1_, p_150807_3_);
                    }
                }

                TileEntity tileentity;

                if (!this.worldObj.isRemote)
                {
                    p_150807_4_.onBlockAdded(this.worldObj, l1, p_150807_2_, i2);
                }

                if (p_150807_4_.hasTileEntity(p_150807_5_))
                {
                    tileentity = this.func_150806_e(p_150807_1_, p_150807_2_, p_150807_3_);

                    if (tileentity != null)
                    {
                        tileentity.updateContainingBlockInfo();
                        tileentity.blockMetadata = p_150807_5_;
                    }
                }

                this.isModified = true;
                return true;
            }
        }
    }

    /**
     * Set the metadata of a block in the chunk
     */
    public boolean setBlockMetadata(int par1, int par2, int par3, int par4)
    {
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[par2 >> 4];

        if (extendedblockstorage == null)
        {
            return false;
        }
        else
        {
            int i1 = extendedblockstorage.getExtBlockMetadata(par1, par2 & 15, par3);

            if (i1 == par4)
            {
                return false;
            }
            else
            {
                this.isModified = true;
                extendedblockstorage.setExtBlockMetadata(par1, par2 & 15, par3, par4);

                if (extendedblockstorage.getBlockByExtId(par1, par2 & 15, par3).hasTileEntity(par4))
                {
                    TileEntity tileentity = this.func_150806_e(par1, par2, par3);

                    if (tileentity != null)
                    {
                        tileentity.updateContainingBlockInfo();
                        tileentity.blockMetadata = par4;
                    }
                }

                return true;
            }
        }
    }

    /**
     * Gets the amount of light saved in this block (doesn't adjust for daylight)
     */
    public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
    {
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[par3 >> 4];
        return extendedblockstorage == null ? (this.canBlockSeeTheSky(par2, par3, par4) ? par1EnumSkyBlock.defaultLightValue : 0) : (par1EnumSkyBlock == EnumSkyBlock.Sky ? (this.worldObj.provider.hasNoSky ? 0 : extendedblockstorage.getExtSkylightValue(par2, par3 & 15, par4)) : (par1EnumSkyBlock == EnumSkyBlock.Block ? extendedblockstorage.getExtBlocklightValue(par2, par3 & 15, par4) : par1EnumSkyBlock.defaultLightValue));
    }

    /**
     * Sets the light value at the coordinate. If enumskyblock is set to sky it sets it in the skylightmap and if its a
     * block then into the blocklightmap. Args enumSkyBlock, x, y, z, lightValue
     */
    public void setLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4, int par5)
    {
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[par3 >> 4];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = this.storageArrays[par3 >> 4] = new ExtendedBlockStorage(par3 >> 4 << 4, !this.worldObj.provider.hasNoSky);
            this.generateSkylightMap();
        }

        this.isModified = true;

        if (par1EnumSkyBlock == EnumSkyBlock.Sky)
        {
            if (!this.worldObj.provider.hasNoSky)
            {
                extendedblockstorage.setExtSkylightValue(par2, par3 & 15, par4, par5);
            }
        }
        else if (par1EnumSkyBlock == EnumSkyBlock.Block)
        {
            extendedblockstorage.setExtBlocklightValue(par2, par3 & 15, par4, par5);
        }
    }

    /**
     * Gets the amount of light on a block taking into account sunlight
     */
    public int getBlockLightValue(int par1, int par2, int par3, int par4)
    {
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[par2 >> 4];

        if (extendedblockstorage == null)
        {
            return !this.worldObj.provider.hasNoSky && par4 < EnumSkyBlock.Sky.defaultLightValue ? EnumSkyBlock.Sky.defaultLightValue - par4 : 0;
        }
        else
        {
            int i1 = this.worldObj.provider.hasNoSky ? 0 : extendedblockstorage.getExtSkylightValue(par1, par2 & 15, par3);

            if (i1 > 0)
            {
                isLit = true;
            }

            i1 -= par4;
            int j1 = extendedblockstorage.getExtBlocklightValue(par1, par2 & 15, par3);

            if (j1 > i1)
            {
                i1 = j1;
            }

            return i1;
        }
    }

    /**
     * Adds an entity to the chunk. Args: entity
     */
    public void addEntity(Entity par1Entity)
    {
        this.hasEntities = true;
        int i = MathHelper.floor_double(par1Entity.posX / 16.0D);
        int j = MathHelper.floor_double(par1Entity.posZ / 16.0D);

        if (i != this.xPosition || j != this.zPosition)
        {
            logger.error("Wrong location! " + par1Entity);
            Thread.dumpStack();
        }

        int k = MathHelper.floor_double(par1Entity.posY / 16.0D);

        if (k < 0)
        {
            k = 0;
        }

        if (k >= this.entityLists.length)
        {
            k = this.entityLists.length - 1;
        }

        MinecraftForge.EVENT_BUS.post(new EntityEvent.EnteringChunk(par1Entity, this.xPosition, this.zPosition, par1Entity.chunkCoordX, par1Entity.chunkCoordZ));
        par1Entity.addedToChunk = true;
        par1Entity.chunkCoordX = this.xPosition;
        par1Entity.chunkCoordY = k;
        par1Entity.chunkCoordZ = this.zPosition;
        this.entityLists[k].add(par1Entity);
    }

    /**
     * removes entity using its y chunk coordinate as its index
     */
    public void removeEntity(Entity par1Entity)
    {
        this.removeEntityAtIndex(par1Entity, par1Entity.chunkCoordY);
    }

    /**
     * Removes entity at the specified index from the entity array.
     */
    public void removeEntityAtIndex(Entity par1Entity, int par2)
    {
        if (par2 < 0)
        {
            par2 = 0;
        }

        if (par2 >= this.entityLists.length)
        {
            par2 = this.entityLists.length - 1;
        }

        this.entityLists[par2].remove(par1Entity);
    }

    /**
     * Returns whether is not a block above this one blocking sight to the sky (done via checking against the heightmap)
     */
    public boolean canBlockSeeTheSky(int par1, int par2, int par3)
    {
        return par2 >= this.heightMap[par3 << 4 | par1];
    }

    public TileEntity func_150806_e(int p_150806_1_, int p_150806_2_, int p_150806_3_)
    {
        ChunkPosition chunkposition = new ChunkPosition(p_150806_1_, p_150806_2_, p_150806_3_);
        TileEntity tileentity = (TileEntity)this.chunkTileEntityMap.get(chunkposition);

        if (tileentity != null && tileentity.isInvalid())
        {
            chunkTileEntityMap.remove(chunkposition);
            tileentity = null;
        }

        if (tileentity == null)
        {
            Block block = this.getBlock(p_150806_1_, p_150806_2_, p_150806_3_);
            int meta = this.getBlockMetadata(p_150806_1_, p_150806_2_, p_150806_3_);

            if (!block.hasTileEntity(meta))
            {
                return null;
            }

            tileentity = block.createTileEntity(worldObj, meta);
            this.worldObj.setTileEntity(this.xPosition * 16 + p_150806_1_, p_150806_2_, this.zPosition * 16 + p_150806_3_, tileentity);
        }

        return tileentity;
    }

    public void addTileEntity(TileEntity p_150813_1_)
    {
        int i = p_150813_1_.xCoord - this.xPosition * 16;
        int j = p_150813_1_.yCoord;
        int k = p_150813_1_.zCoord - this.zPosition * 16;
        this.func_150812_a(i, j, k, p_150813_1_);

        if (this.isChunkLoaded)
        {
            this.worldObj.addTileEntity(p_150813_1_);
        }
    }

    public void func_150812_a(int p_150812_1_, int p_150812_2_, int p_150812_3_, TileEntity p_150812_4_)
    {
        ChunkPosition chunkposition = new ChunkPosition(p_150812_1_, p_150812_2_, p_150812_3_);
        p_150812_4_.setWorldObj(this.worldObj);
        p_150812_4_.xCoord = this.xPosition * 16 + p_150812_1_;
        p_150812_4_.yCoord = p_150812_2_;
        p_150812_4_.zCoord = this.zPosition * 16 + p_150812_3_;

        int metadata = getBlockMetadata(p_150812_1_, p_150812_2_, p_150812_3_);
        if (this.getBlock(p_150812_1_, p_150812_2_, p_150812_3_).hasTileEntity(metadata))
        {
            if (this.chunkTileEntityMap.containsKey(chunkposition))
            {
                ((TileEntity)this.chunkTileEntityMap.get(chunkposition)).invalidate();
            }

            p_150812_4_.validate();
            this.chunkTileEntityMap.put(chunkposition, p_150812_4_);
        }
    }

    public void removeTileEntity(int p_150805_1_, int p_150805_2_, int p_150805_3_)
    {
        ChunkPosition chunkposition = new ChunkPosition(p_150805_1_, p_150805_2_, p_150805_3_);

        if (this.isChunkLoaded)
        {
            TileEntity tileentity = (TileEntity)this.chunkTileEntityMap.remove(chunkposition);

            if (tileentity != null)
            {
                tileentity.invalidate();
            }
        }
    }

    /**
     * Called when this Chunk is loaded by the ChunkProvider
     */
    public void onChunkLoad()
    {
        this.isChunkLoaded = true;
        this.worldObj.func_147448_a(this.chunkTileEntityMap.values());

        for (int i = 0; i < this.entityLists.length; ++i)
        {
            Iterator iterator = this.entityLists[i].iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();
                entity.onChunkLoad();
            }

            this.worldObj.addLoadedEntities(this.entityLists[i]);
        }
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Load(this));
    }

    /**
     * Called when this Chunk is unloaded by the ChunkProvider
     */
    public void onChunkUnload()
    {
        this.isChunkLoaded = false;
        Iterator iterator = this.chunkTileEntityMap.values().iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            this.worldObj.func_147457_a(tileentity);
        }

        for (int i = 0; i < this.entityLists.length; ++i)
        {
            this.worldObj.unloadEntities(this.entityLists[i]);
        }
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Unload(this));
    }

    /**
     * Sets the isModified flag for this Chunk
     */
    public void setChunkModified()
    {
        this.isModified = true;
    }

    /**
     * Fills the given list of all entities that intersect within the given bounding box that aren't the passed entity
     * Args: entity, aabb, listToFill
     */
    public void getEntitiesWithinAABBForEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB, List par3List, IEntitySelector par4IEntitySelector)
    {
        int i = MathHelper.floor_double((par2AxisAlignedBB.minY - World.MAX_ENTITY_RADIUS) / 16.0D);
        int j = MathHelper.floor_double((par2AxisAlignedBB.maxY + World.MAX_ENTITY_RADIUS) / 16.0D);
        i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);

        for (int k = i; k <= j; ++k)
        {
            List list1 = this.entityLists[k];

            for (int l = 0; l < list1.size(); ++l)
            {
                Entity entity1 = (Entity)list1.get(l);

                if (entity1 != par1Entity && entity1.boundingBox.intersectsWith(par2AxisAlignedBB) && (par4IEntitySelector == null || par4IEntitySelector.isEntityApplicable(entity1)))
                {
                    par3List.add(entity1);
                    Entity[] aentity = entity1.getParts();

                    if (aentity != null)
                    {
                        for (int i1 = 0; i1 < aentity.length; ++i1)
                        {
                            entity1 = aentity[i1];

                            if (entity1 != par1Entity && entity1.boundingBox.intersectsWith(par2AxisAlignedBB) && (par4IEntitySelector == null || par4IEntitySelector.isEntityApplicable(entity1)))
                            {
                                par3List.add(entity1);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets all entities that can be assigned to the specified class. Args: entityClass, aabb, listToFill
     */
    public void getEntitiesOfTypeWithinAAAB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, List par3List, IEntitySelector par4IEntitySelector)
    {
        int i = MathHelper.floor_double((par2AxisAlignedBB.minY - World.MAX_ENTITY_RADIUS) / 16.0D);
        int j = MathHelper.floor_double((par2AxisAlignedBB.maxY + World.MAX_ENTITY_RADIUS) / 16.0D);
        i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);

        for (int k = i; k <= j; ++k)
        {
            List list1 = this.entityLists[k];

            for (int l = 0; l < list1.size(); ++l)
            {
                Entity entity = (Entity)list1.get(l);

                if (par1Class.isAssignableFrom(entity.getClass()) && entity.boundingBox.intersectsWith(par2AxisAlignedBB) && (par4IEntitySelector == null || par4IEntitySelector.isEntityApplicable(entity)))
                {
                    par3List.add(entity);
                }
            }
        }
    }

    /**
     * Returns true if this Chunk needs to be saved
     */
    public boolean needsSaving(boolean par1)
    {
        if (par1)
        {
            if (this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime || this.isModified)
            {
                return true;
            }
        }
        else if (this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L)
        {
            return true;
        }

        return this.isModified;
    }

    public Random getRandomWithSeed(long par1)
    {
        return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ par1);
    }

    public boolean isEmpty()
    {
        return false;
    }

    public void populateChunk(IChunkProvider par1IChunkProvider, IChunkProvider par2IChunkProvider, int par3, int par4)
    {
        if (!this.isTerrainPopulated && par1IChunkProvider.chunkExists(par3 + 1, par4 + 1) && par1IChunkProvider.chunkExists(par3, par4 + 1) && par1IChunkProvider.chunkExists(par3 + 1, par4))
        {
            par1IChunkProvider.populate(par2IChunkProvider, par3, par4);
        }

        if (par1IChunkProvider.chunkExists(par3 - 1, par4) && !par1IChunkProvider.provideChunk(par3 - 1, par4).isTerrainPopulated && par1IChunkProvider.chunkExists(par3 - 1, par4 + 1) && par1IChunkProvider.chunkExists(par3, par4 + 1) && par1IChunkProvider.chunkExists(par3 - 1, par4 + 1))
        {
            par1IChunkProvider.populate(par2IChunkProvider, par3 - 1, par4);
        }

        if (par1IChunkProvider.chunkExists(par3, par4 - 1) && !par1IChunkProvider.provideChunk(par3, par4 - 1).isTerrainPopulated && par1IChunkProvider.chunkExists(par3 + 1, par4 - 1) && par1IChunkProvider.chunkExists(par3 + 1, par4 - 1) && par1IChunkProvider.chunkExists(par3 + 1, par4))
        {
            par1IChunkProvider.populate(par2IChunkProvider, par3, par4 - 1);
        }

        if (par1IChunkProvider.chunkExists(par3 - 1, par4 - 1) && !par1IChunkProvider.provideChunk(par3 - 1, par4 - 1).isTerrainPopulated && par1IChunkProvider.chunkExists(par3, par4 - 1) && par1IChunkProvider.chunkExists(par3 - 1, par4))
        {
            par1IChunkProvider.populate(par2IChunkProvider, par3 - 1, par4 - 1);
        }
    }

    /**
     * Gets the height to which rain/snow will fall. Calculates it if not already stored.
     */
    public int getPrecipitationHeight(int par1, int par2)
    {
        int k = par1 | par2 << 4;
        int l = this.precipitationHeightMap[k];

        if (l == -999)
        {
            int i1 = this.getTopFilledSegment() + 15;
            l = -1;

            while (i1 > 0 && l == -1)
            {
                Block block = this.getBlock(par1, i1, par2);
                Material material = block.getMaterial();

                if (!material.blocksMovement() && !material.isLiquid())
                {
                    --i1;
                }
                else
                {
                    l = i1 + 1;
                }
            }

            this.precipitationHeightMap[k] = l;
        }

        return l;
    }

    public void func_150804_b(boolean p_150804_1_)
    {
        if (this.isGapLightingUpdated && !this.worldObj.provider.hasNoSky && !p_150804_1_)
        {
            this.recheckGaps(this.worldObj.isRemote);
        }

        this.field_150815_m = true;

        if (!this.isLightPopulated && this.isTerrainPopulated)
        {
            this.func_150809_p();
        }
    }

    public boolean func_150802_k()
    {
        return this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated;
    }

    /**
     * Gets a ChunkCoordIntPair representing the Chunk's position.
     */
    public ChunkCoordIntPair getChunkCoordIntPair()
    {
        return new ChunkCoordIntPair(this.xPosition, this.zPosition);
    }

    /**
     * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty
     * (true) or not (false).
     */
    public boolean getAreLevelsEmpty(int par1, int par2)
    {
        if (par1 < 0)
        {
            par1 = 0;
        }

        if (par2 >= 256)
        {
            par2 = 255;
        }

        for (int k = par1; k <= par2; k += 16)
        {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[k >> 4];

            if (extendedblockstorage != null && !extendedblockstorage.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public void setStorageArrays(ExtendedBlockStorage[] par1ArrayOfExtendedBlockStorage)
    {
        this.storageArrays = par1ArrayOfExtendedBlockStorage;
    }

    /**
     * Initialise this chunk with new binary data
     */
    @SideOnly(Side.CLIENT)
    public void fillChunk(byte[] par1ArrayOfByte, int par2, int par3, boolean par4)
    {
        Iterator iterator = chunkTileEntityMap.values().iterator();
        while(iterator.hasNext())
        {
            TileEntity tileEntity = (TileEntity)iterator.next();
            tileEntity.updateContainingBlockInfo();
            tileEntity.getBlockMetadata();
            tileEntity.getBlockType();
        }

        int k = 0;
        boolean flag1 = !this.worldObj.provider.hasNoSky;
        int l;

        for (l = 0; l < this.storageArrays.length; ++l)
        {
            if ((par2 & 1 << l) != 0)
            {
                if (this.storageArrays[l] == null)
                {
                    this.storageArrays[l] = new ExtendedBlockStorage(l << 4, flag1);
                }

                byte[] abyte1 = this.storageArrays[l].getBlockLSBArray();
                System.arraycopy(par1ArrayOfByte, k, abyte1, 0, abyte1.length);
                k += abyte1.length;
            }
            else if (par4 && this.storageArrays[l] != null)
            {
                this.storageArrays[l] = null;
            }
        }

        NibbleArray nibblearray;

        for (l = 0; l < this.storageArrays.length; ++l)
        {
            if ((par2 & 1 << l) != 0 && this.storageArrays[l] != null)
            {
                nibblearray = this.storageArrays[l].getMetadataArray();
                System.arraycopy(par1ArrayOfByte, k, nibblearray.data, 0, nibblearray.data.length);
                k += nibblearray.data.length;
            }
        }

        for (l = 0; l < this.storageArrays.length; ++l)
        {
            if ((par2 & 1 << l) != 0 && this.storageArrays[l] != null)
            {
                nibblearray = this.storageArrays[l].getBlocklightArray();
                System.arraycopy(par1ArrayOfByte, k, nibblearray.data, 0, nibblearray.data.length);
                k += nibblearray.data.length;
            }
        }

        if (flag1)
        {
            for (l = 0; l < this.storageArrays.length; ++l)
            {
                if ((par2 & 1 << l) != 0 && this.storageArrays[l] != null)
                {
                    nibblearray = this.storageArrays[l].getSkylightArray();
                    System.arraycopy(par1ArrayOfByte, k, nibblearray.data, 0, nibblearray.data.length);
                    k += nibblearray.data.length;
                }
            }
        }

        for (l = 0; l < this.storageArrays.length; ++l)
        {
            if ((par3 & 1 << l) != 0)
            {
                if (this.storageArrays[l] == null)
                {
                    k += 2048;
                }
                else
                {
                    nibblearray = this.storageArrays[l].getBlockMSBArray();

                    if (nibblearray == null)
                    {
                        nibblearray = this.storageArrays[l].createBlockMSBArray();
                    }

                    System.arraycopy(par1ArrayOfByte, k, nibblearray.data, 0, nibblearray.data.length);
                    k += nibblearray.data.length;
                }
            }
            else if (par4 && this.storageArrays[l] != null && this.storageArrays[l].getBlockMSBArray() != null)
            {
                this.storageArrays[l].clearMSBArray();
            }
        }

        if (par4)
        {
            System.arraycopy(par1ArrayOfByte, k, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            int i1 = k + this.blockBiomeArray.length;
        }

        for (l = 0; l < this.storageArrays.length; ++l)
        {
            if (this.storageArrays[l] != null && (par2 & 1 << l) != 0)
            {
                this.storageArrays[l].removeInvalidBlocks();
            }
        }

        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        this.generateHeightMap();
        List<TileEntity> invalidList = new ArrayList<TileEntity>();
        iterator = this.chunkTileEntityMap.values().iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            int x = tileentity.xCoord & 15;
            int y = tileentity.yCoord;
            int z = tileentity.zCoord & 15;
            Block block = tileentity.getBlockType();
            if (block != getBlock(x, y, z) || tileentity.blockMetadata != this.getBlockMetadata(x, y, z))
            {
                invalidList.add(tileentity);
            }
            tileentity.updateContainingBlockInfo();
        }

        for (TileEntity te : invalidList)
        {
            te.invalidate();
        }
    }

    /**
     * This method retrieves the biome at a set of coordinates
     */
    public BiomeGenBase getBiomeGenForWorldCoords(int par1, int par2, WorldChunkManager par3WorldChunkManager)
    {
        int k = this.blockBiomeArray[par2 << 4 | par1] & 255;

        if (k == 255)
        {
            BiomeGenBase biomegenbase = par3WorldChunkManager.getBiomeGenAt((this.xPosition << 4) + par1, (this.zPosition << 4) + par2);
            k = biomegenbase.biomeID;
            this.blockBiomeArray[par2 << 4 | par1] = (byte)(k & 255);
        }

        return BiomeGenBase.getBiome(k) == null ? BiomeGenBase.plains : BiomeGenBase.getBiome(k);
    }

    /**
     * Returns an array containing a 16x16 mapping on the X/Z of block positions in this Chunk to biome IDs.
     */
    public byte[] getBiomeArray()
    {
        return this.blockBiomeArray;
    }

    /**
     * Accepts a 256-entry array that contains a 16x16 mapping on the X/Z plane of block positions in this Chunk to
     * biome IDs.
     */
    public void setBiomeArray(byte[] par1ArrayOfByte)
    {
        this.blockBiomeArray = par1ArrayOfByte;
    }

    /**
     * Resets the relight check index to 0 for this Chunk.
     */
    public void resetRelightChecks()
    {
        this.queuedLightChecks = 0;
    }

    /**
     * Called once-per-chunk-per-tick, and advances the round-robin relight check index per-storage-block by up to 8
     * blocks at a time. In a worst-case scenario, can potentially take up to 1.6 seconds, calculated via
     * (4096/(8*16))/20, to re-check all blocks in a chunk, which could explain both lagging light updates in certain
     * cases as well as Nether relight
     */
    public void enqueueRelightChecks()
    {
        for (int i = 0; i < 8; ++i)
        {
            if (this.queuedLightChecks >= 4096)
            {
                return;
            }

            int j = this.queuedLightChecks % 16;
            int k = this.queuedLightChecks / 16 % 16;
            int l = this.queuedLightChecks / 256;
            ++this.queuedLightChecks;
            int i1 = (this.xPosition << 4) + k;
            int j1 = (this.zPosition << 4) + l;

            for (int k1 = 0; k1 < 16; ++k1)
            {
                int l1 = (j << 4) + k1;

                if (this.storageArrays[j] == null && (k1 == 0 || k1 == 15 || k == 0 || k == 15 || l == 0 || l == 15) || this.storageArrays[j] != null && this.storageArrays[j].getBlockByExtId(k, k1, l).getMaterial() == Material.air)
                {
                    if (this.worldObj.getBlock(i1, l1 - 1, j1).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(i1, l1 - 1, j1);
                    }

                    if (this.worldObj.getBlock(i1, l1 + 1, j1).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(i1, l1 + 1, j1);
                    }

                    if (this.worldObj.getBlock(i1 - 1, l1, j1).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(i1 - 1, l1, j1);
                    }

                    if (this.worldObj.getBlock(i1 + 1, l1, j1).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(i1 + 1, l1, j1);
                    }

                    if (this.worldObj.getBlock(i1, l1, j1 - 1).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(i1, l1, j1 - 1);
                    }

                    if (this.worldObj.getBlock(i1, l1, j1 + 1).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(i1, l1, j1 + 1);
                    }

                    this.worldObj.func_147451_t(i1, l1, j1);
                }
            }
        }
    }

    public void func_150809_p()
    {
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;

        if (!this.worldObj.provider.hasNoSky)
        {
            if (this.worldObj.checkChunksExist(this.xPosition * 16 - 1, 0, this.zPosition * 16 - 1, this.xPosition * 16 + 1, 63, this.zPosition * 16 + 1))
            {
                for (int i = 0; i < 16; ++i)
                {
                    for (int j = 0; j < 16; ++j)
                    {
                        if (!this.func_150811_f(i, j))
                        {
                            this.isLightPopulated = false;
                            break;
                        }
                    }
                }

                if (this.isLightPopulated)
                {
                    Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xPosition * 16 - 1, this.zPosition * 16);
                    chunk.func_150801_a(3);
                    chunk = this.worldObj.getChunkFromBlockCoords(this.xPosition * 16 + 16, this.zPosition * 16);
                    chunk.func_150801_a(1);
                    chunk = this.worldObj.getChunkFromBlockCoords(this.xPosition * 16, this.zPosition * 16 - 1);
                    chunk.func_150801_a(0);
                    chunk = this.worldObj.getChunkFromBlockCoords(this.xPosition * 16, this.zPosition * 16 + 16);
                    chunk.func_150801_a(2);
                }
            }
            else
            {
                this.isLightPopulated = false;
            }
        }
    }

    private void func_150801_a(int p_150801_1_)
    {
        if (this.isTerrainPopulated)
        {
            int j;

            if (p_150801_1_ == 3)
            {
                for (j = 0; j < 16; ++j)
                {
                    this.func_150811_f(15, j);
                }
            }
            else if (p_150801_1_ == 1)
            {
                for (j = 0; j < 16; ++j)
                {
                    this.func_150811_f(0, j);
                }
            }
            else if (p_150801_1_ == 0)
            {
                for (j = 0; j < 16; ++j)
                {
                    this.func_150811_f(j, 15);
                }
            }
            else if (p_150801_1_ == 2)
            {
                for (j = 0; j < 16; ++j)
                {
                    this.func_150811_f(j, 0);
                }
            }
        }
    }

    private boolean func_150811_f(int p_150811_1_, int p_150811_2_)
    {
        int k = this.getTopFilledSegment();
        boolean flag = false;
        boolean flag1 = false;
        int l;

        for (l = k + 16 - 1; l > 63 || l > 0 && !flag1; --l)
        {
            int i1 = this.func_150808_b(p_150811_1_, l, p_150811_2_);

            if (i1 == 255 && l < 63)
            {
                flag1 = true;
            }

            if (!flag && i1 > 0)
            {
                flag = true;
            }
            else if (flag && i1 == 0 && !this.worldObj.func_147451_t(this.xPosition * 16 + p_150811_1_, l, this.zPosition * 16 + p_150811_2_))
            {
                return false;
            }
        }

        for (; l > 0; --l)
        {
            if (this.getBlock(p_150811_1_, l, p_150811_2_).getLightValue() > 0)
            {
                this.worldObj.func_147451_t(this.xPosition * 16 + p_150811_1_, l, this.zPosition * 16 + p_150811_2_);
            }
        }

        return true;
    }

    /**
     * Retrieves the tile entity, WITHOUT creating it.
     * Good for checking if it exists.
     * 
     * @param x
     * @param y
     * @param z
     * @return The tile entity at the specified location, if it exists and is valid.
     */
    public TileEntity getTileEntityUnsafe(int x, int y, int z)
    {
        ChunkPosition chunkposition = new ChunkPosition(x, y, z);
        TileEntity tileentity = (TileEntity)this.chunkTileEntityMap.get(chunkposition);

        if (tileentity != null && tileentity.isInvalid())
        {
            chunkTileEntityMap.remove(chunkposition);
            tileentity = null;
        }

        return tileentity;
    }

    /**
     * Removes the tile entity at the specified position, only if it's
     * marked as invalid.
     * 
     * @param x
     * @param y
     * @param z
     */
    public void removeInvalidTileEntity(int x, int y, int z)
    {
        ChunkPosition position = new ChunkPosition(x, y, z);
        if (isChunkLoaded)
        {
            TileEntity entity = (TileEntity)chunkTileEntityMap.get(position);
            if (entity != null && entity.isInvalid())
            {
                chunkTileEntityMap.remove(position);
            }
        }
    }
}