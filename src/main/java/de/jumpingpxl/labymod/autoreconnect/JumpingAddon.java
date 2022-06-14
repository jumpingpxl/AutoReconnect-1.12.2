package de.jumpingpxl.labymod.autoreconnect;

import de.jumpingpxl.labymod.autoreconnect.listener.GuiOpenListener;
import de.jumpingpxl.labymod.autoreconnect.util.Settings;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.client.multiplayer.ServerData;

import java.util.List;

public class JumpingAddon extends LabyModAddon {

	public static final int VERSION = 4;

	private Settings settings;
	private ServerData lastServer;

	@Override
	public void onEnable() {
		settings = new Settings(this);

		getApi().registerForgeListener(new GuiOpenListener(this));
	}

	@Override
	public void loadConfig() {
		settings.loadConfig();
	}

	@Override
	protected void fillSettings(List<SettingsElement> settingsElements) {
		settings.fillSettings(settingsElements);
	}

	public Settings getSettings() {
		return settings;
	}

	public ServerData getLastServer() {
		return lastServer;
	}

	public void setLastServer(ServerData lastServer) {
		this.lastServer = lastServer;
	}
	
}
