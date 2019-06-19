package de.jumpingpxl.labymod.autoreconnect.util;

import de.jumpingpxl.labymod.autoreconnect.JumpingAddon;
import net.labymod.core.LabyModCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Nico (JumpingPxl) Middendorf
 * @date 16.06.2019
 */

public class ModGuiDisconnected extends GuiScreen {

	private JumpingAddon jumpingAddon;
	private GuiButton reconnectButton;
	private Timer timer;
	private GuiScreen parentScreen;
	private ITextComponent message;
	private String reason;
	private int secondsLeft;
	private int reasonHeight;
	private List<String> multilineMessage;

	public ModGuiDisconnected(JumpingAddon jumpingAddon, GuiDisconnected guiDisconnected) throws IllegalAccessException {
		this.jumpingAddon = jumpingAddon;
		this.parentScreen = (GuiScreen) ReflectionHelper.findField(GuiDisconnected.class,
				jumpingAddon.getParentScreenMappings()).get(guiDisconnected);
		this.message = (ITextComponent) ReflectionHelper.findField(GuiDisconnected.class,
				jumpingAddon.getMessageMappings()).get(guiDisconnected);
		this.reason = (String) ReflectionHelper.findField(GuiDisconnected.class,
				jumpingAddon.getReasonMappings()).get(guiDisconnected);
		this.secondsLeft = jumpingAddon.getSettings().getSecondsUntilReconnect();
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		this.multilineMessage = this.fontRenderer.listFormattedStringToWidth(this.message.getFormattedText(),
				this.width - 50);
		this.reasonHeight = this.multilineMessage.size() * this.fontRenderer.FONT_HEIGHT;
		this.buttonList.add(new GuiButton(0, this.width / 2 - 10, this.height / 2 + this.reasonHeight / 2 +
				this.fontRenderer.FONT_HEIGHT, 125, 20, I18n.format("gui.toMenu")));
		this.reconnectButton = new GuiButton(1, this.width / 2 - 115, this.height / 2 + this.reasonHeight / 2 +
				this.fontRenderer.FONT_HEIGHT, 100, 20, "Reconnect in: §a" + this.secondsLeft + "s");
		this.buttonList.add(reconnectButton);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				secondsLeft--;
				String color = "§a";
				switch (secondsLeft) {
					case 9:
					case 8:
					case 7:
					case 6:
						color = "§e";
						break;
					case 5:
					case 4:
						color = "§c";
						break;
					case 3:
					case 2:
					case 1:
						color = "§4";
						break;
					case 0:
						color = "§4";
						timer.cancel();
						break;
				}
				reconnectButton.displayString = "Reconnect in: " + color + secondsLeft + "s";
			}
		}, 1000L, 1000L);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.reason, this.width / 2,
				this.height / 2 - this.reasonHeight / 2 - this.fontRenderer.FONT_HEIGHT * 2, 11184810);
		int i = this.height / 2 - this.reasonHeight / 2;
		if (this.multilineMessage != null)
			for (Object object : this.multilineMessage) {
				this.drawCenteredString(this.fontRenderer, String.valueOf(object), this.width / 2, i, 16777215);
				i += this.fontRenderer.FONT_HEIGHT;
			}
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (secondsLeft == 0) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(LabyModCore.getMinecraft().getCustomMainMenu(),
					Minecraft.getMinecraft(), jumpingAddon.getLastServer()));
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		this.timer.cancel();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			this.timer.cancel();
			this.mc.displayGuiScreen(this.parentScreen);
		}
		if (button.id == 1) {
			this.timer.cancel();
			Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(LabyModCore.getMinecraft().getCustomMainMenu(),
					Minecraft.getMinecraft(), jumpingAddon.getLastServer()));
		}
	}
}
