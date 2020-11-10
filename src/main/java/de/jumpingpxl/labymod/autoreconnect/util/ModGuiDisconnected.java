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

public class ModGuiDisconnected extends GuiScreen {

	private static final String[] PARENT_SCREEN_MAPPINGS = new String[]{"h", "field_146307_h",
			"parentScreen"};
	private static final String[] MESSAGE_MAPPINGS = new String[]{"f", "field_146304_f", "message"};
	private static final String[] REASON_MAPPINGS = new String[]{"a", "field_146306_a", "reason"};

	private final JumpingAddon jumpingAddon;
	private final GuiScreen parentScreen;
	private final ITextComponent message;
	private final String reason;
	private GuiButton reconnectButton;
	private Timer timer;
	private int secondsLeft;
	private int reasonHeight;
	private List<String> multilineMessage;

	public ModGuiDisconnected(JumpingAddon jumpingAddon, GuiDisconnected guiDisconnected)
			throws IllegalAccessException {
		this.jumpingAddon = jumpingAddon;

		parentScreen = (GuiScreen) ReflectionHelper.findField(GuiDisconnected.class,
				PARENT_SCREEN_MAPPINGS).get(guiDisconnected);
		message = (ITextComponent) ReflectionHelper.findField(GuiDisconnected.class, MESSAGE_MAPPINGS)
				.get(guiDisconnected);
		reason = (String) ReflectionHelper.findField(GuiDisconnected.class, REASON_MAPPINGS).get(
				guiDisconnected);
		secondsLeft = jumpingAddon.getSettings().getSecondsUntilReconnect();

		if (message.getUnformattedText().equals(I18n.format("disconnect.loginFailedInfo",
				I18n.format("disconnect" + ".loginFailedInfo.invalidSession")))) {
			throw new IllegalStateException();
		}
	}

	@Override
	public void initGui() {
		buttonList.clear();
		multilineMessage = fontRenderer.listFormattedStringToWidth(message.getFormattedText(),
				width - 50);
		reasonHeight = multilineMessage.size() * fontRenderer.FONT_HEIGHT;
		buttonList.add(
				new GuiButton(0, width / 2 - 10, height / 2 + reasonHeight / 2 + fontRenderer.FONT_HEIGHT,
						125, 20, I18n.format("gui.toMenu")));
		reconnectButton = new GuiButton(1, width / 2 - 115,
				height / 2 + reasonHeight / 2 + fontRenderer.FONT_HEIGHT, 100, 20,
				"Reconnect in: §a" + secondsLeft + "s");
		buttonList.add(reconnectButton);

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
		drawDefaultBackground();
		drawCenteredString(fontRenderer, reason, width / 2,
				height / 2 - reasonHeight / 2 - fontRenderer.FONT_HEIGHT * 2, 11184810);
		int i = height / 2 - reasonHeight / 2;
		if (multilineMessage != null) {
			for (Object object : multilineMessage) {
				drawCenteredString(fontRenderer, String.valueOf(object), width / 2, i, 16777215);
				i += fontRenderer.FONT_HEIGHT;
			}
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
		if (secondsLeft == 0) {
			Minecraft.getMinecraft().displayGuiScreen(
					new GuiConnecting(LabyModCore.getMinecraft().getCustomMainMenu(),
							Minecraft.getMinecraft(), jumpingAddon.getLastServer()));
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		timer.cancel();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			timer.cancel();
			mc.displayGuiScreen(parentScreen);
		}

		if (button.id == 1) {
			timer.cancel();
			Minecraft.getMinecraft().displayGuiScreen(
					new GuiConnecting(LabyModCore.getMinecraft().getCustomMainMenu(),
							Minecraft.getMinecraft(), jumpingAddon.getLastServer()));
		}
	}
}
