package mcp.mobius.waila.fabric;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.WailaClient;
import mcp.mobius.waila.api.WailaConstants;
import mcp.mobius.waila.api.event.WailaRenderEvent;
import mcp.mobius.waila.api.event.WailaTooltipEvent;
import mcp.mobius.waila.overlay.DataAccessor;
import mcp.mobius.waila.overlay.Tooltip;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class FabricWailaClient extends WailaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        keyBindingBuilder = (id, key) ->
            KeyBindingHelper.registerKeyBinding(new KeyBinding("key.waila." + id, InputUtil.Type.KEYSYM, key, WailaConstants.MOD_NAME));

        init();

        Waila.packet.initClient();

        HudRenderCallback.EVENT.register(Tooltip::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> onClientTick());

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> onJoinServer());

        ItemTooltipCallback.EVENT.register((stack, ctx, tooltip) ->
            onItemTooltip(stack, tooltip));

        Tooltip.onCreate = texts ->
            WailaTooltipEvent.WAILA_HANDLE_TOOLTIP.invoker().onTooltip(new WailaTooltipEvent(texts, DataAccessor.INSTANCE));

        Tooltip.onPreRender = rect -> {
            WailaRenderEvent.Pre event = new WailaRenderEvent.Pre(DataAccessor.INSTANCE, rect);
            return WailaRenderEvent.WAILA_RENDER_PRE.invoker().onPreRender(event) ? null : event.getPosition();
        };

        Tooltip.onPostRender = position ->
            WailaRenderEvent.WAILA_RENDER_POST.invoker().onPostRender(new WailaRenderEvent.Post(position));

        FabricTickHandler.registerListener();
    }

}
