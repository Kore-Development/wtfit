package mcp.mobius.waila.addons.minecraft;

import com.google.common.base.Throwables;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.utils.InventoryUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.ArrayList;
import java.util.List;

public class HUDHandlerChestEntity implements IWailaEntityProvider {

    public static final IWailaEntityProvider INSTANCE = new HUDHandlerChestEntity();

    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (!config.getConfig("vanilla.horseinventory"))
            return currenttip;

        if (!accessor.getNBTData().getBoolean("chested")) {
            currenttip.add(I18n.format("hud.msg.unchested"));
            return currenttip;
        }

        if (accessor.getNBTData().hasKey("handler")) {
            int handlerSize = accessor.getNBTData().getInteger("handlerSize");
            ItemStackHandler itemHandler = new ItemStackHandler();
            itemHandler.setSize(handlerSize);
            InventoryUtils.populateInv(itemHandler, accessor.getNBTData().getTagList("handler", 10));

            boolean foundCarpet = false;
            List<ItemStack> toRender = new ArrayList<ItemStack>();
            for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                ItemStack stack = itemHandler.getStackInSlot(slot);
                if (stack.func_190926_b())
                    continue;

                if (!foundCarpet && entity instanceof EntityLlama && stack.getItem() == Item.getItemFromBlock(Blocks.CARPET)) {
                    foundCarpet = true;
                    continue;
                }

                InventoryUtils.addStack(toRender, stack);
            }

            String renderString = "";
            int drawnCount = 0;
            for (ItemStack stack : toRender) {
                String name = stack.getItem().getRegistryName().toString();
                if (drawnCount >= 5 && !accessor.getPlayer().isSneaking())
                    break;
                else if (drawnCount >= 5 && accessor.getPlayer().isSneaking()) {
                    currenttip.add(renderString);
                    renderString = "";
                    drawnCount = 0;
                }

                String nbt = "";
                if (stack.hasTagCompound())
                    nbt = stack.getTagCompound().toString();
                renderString += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(stack.func_190916_E()), String.valueOf(stack.getItemDamage()), nbt);
                drawnCount += 1;
            }

            currenttip.add(renderString);
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        ContainerHorseChest horseChest = ReflectionHelper.getPrivateValue(AbstractHorse.class, (AbstractChestHorse) ent, "horseChest", "field_110296_bG");

        IItemHandler itemHandler = new InvWrapper(horseChest);
        tag.setTag("handler", InventoryUtils.invToNBT(itemHandler));
        tag.setInteger("handlerSize", itemHandler.getSlots());
        tag.setBoolean("chested", ((AbstractChestHorse) ent).func_190695_dh());

        return tag;
    }
}
