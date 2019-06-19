package de.jumpingpxl.labymod.autoreconnect;

import de.jumpingpxl.labymod.autoreconnect.listener.GuiOpenListener;
import de.jumpingpxl.labymod.autoreconnect.util.Settings;
import lombok.Getter;
import lombok.Setter;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.client.multiplayer.ServerData;

import java.util.List;

/**
 * @author Nico (JumpingPxl) Middendorf
 * @date 16.06.2019
 * @project LabyMod-Addon: AutoReconnect-1.12.2
 */

@Getter
public class JumpingAddon extends LabyModAddon {

	private Settings settings;
	private double version = 1.0D;
	@Setter
	private ServerData lastServer;
	private String[] parentScreenMappings = new String[]{"h", "field_146307_h", "parentScreen"};
	private String[] messageMappings = new String[]{"f", "field_146304_f", "message"};
	private String[] reasonMappings = new String[]{"a", "field_146306_a", "reason"};

	@Override
	public void onEnable() {
		this.settings = new Settings(this);
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
}
