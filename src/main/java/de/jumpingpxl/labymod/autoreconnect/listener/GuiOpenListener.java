package de.jumpingpxl.labymod.autoreconnect.listener;

import de.jumpingpxl.labymod.autoreconnect.JumpingAddon;
import de.jumpingpxl.labymod.autoreconnect.util.ModGuiDisconnected;
import net.labymod.core.LabyModCore;
import net.labymod.gui.GuiRefreshSession;
import net.labymod.gui.ModGuiMultiplayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiOpenListener {

	private final JumpingAddon jumpingAddon;

	public GuiOpenListener(JumpingAddon jumpingAddon) {
		this.jumpingAddon = jumpingAddon;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onGuiOpen(GuiOpenEvent event) {
		GuiScreen guiScreen = LabyModCore.getForge().getGuiOpenEventGui(event);
		if (guiScreen instanceof GuiConnecting) {
			jumpingAddon.setLastServer(Minecraft.getMinecraft().getCurrentServerData());
		}

		if (guiScreen instanceof GuiDisconnected && jumpingAddon.getSettings().isEnabled()) {
			try {
				guiScreen = new ModGuiDisconnected(jumpingAddon, (GuiDisconnected) guiScreen);
			} catch (IllegalStateException e) {
				guiScreen = new GuiRefreshSession(new ModGuiMultiplayer(null));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		LabyModCore.getForge().setGuiOpenEventGui(event, guiScreen);
	}
}
