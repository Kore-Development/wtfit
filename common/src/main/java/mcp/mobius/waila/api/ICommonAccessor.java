package mcp.mobius.waila.api;

import mcp.mobius.waila.api.internal.ApiSide;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * The Accessor is used to get some basic data out of the game without having to request direct access to the game engine.<br>
 * It will also return things that are unmodified by the overriding systems (like getStack).<br>
 * Common accessor for both Entity and Block/TileEntity.<br>
 * Available data depends on what it is called upon (ie : getEntity() will return null if looking at a block, etc).<br>
 */
@ApiSide.ClientOnly
@ApiStatus.NonExtendable
public interface ICommonAccessor {

    World getWorld();

    PlayerEntity getPlayer();

    Block getBlock();

    Identifier getBlockId();

    @Nullable
    BlockEntity getBlockEntity();

    @Nullable
    Entity getEntity();

    BlockPos getPosition();

    @Nullable
    Vec3d getRenderingPosition();

    NbtCompound getServerData();

    double getPartialFrame();

    @Nullable
    Direction getSide();

    ItemStack getStack();

    String getModNameFormat();

    String getBlockNameFormat();

    String getFluidNameFormat();

    String getEntityNameFormat();

    String getRegistryNameFormat();

}
