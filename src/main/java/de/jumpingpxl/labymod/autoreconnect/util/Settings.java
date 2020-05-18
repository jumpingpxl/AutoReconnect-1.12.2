package de.jumpingpxl.labymod.autoreconnect.util;

import com.google.gson.JsonObject;
import de.jumpingpxl.labymod.autoreconnect.JumpingAddon;
import lombok.Getter;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;

import java.util.List;

@Getter
public class Settings {

	private JumpingAddon jumpingAddon;
	private boolean enabled;
	private int secondsUntilReconnect;

	public Settings(JumpingAddon jumpingAddon) {
		this.jumpingAddon = jumpingAddon;
	}

	private JsonObject getConfig() {
		return jumpingAddon.getConfig();
	}

	private void saveConfig() {
		jumpingAddon.saveConfig();
	}

	public void loadConfig() {
		enabled = !getConfig().has("enabled") || getConfig().get("enabled").getAsBoolean();
		secondsUntilReconnect = getConfig().has("secondsUntilReconnect") ?
				getConfig().get("secondsUntilReconnect").getAsInt() : 5;
	}

	public void fillSettings(List<SettingsElement> list) {
		list.add(new HeaderElement("§eAutoReconnect v" + jumpingAddon.getVersion()));
		list.add(new HeaderElement(" "));
		list.add(new BooleanElement("§6Enabled", new ControlElement.IconData(Material.LEVER), enabled -> {
			this.enabled = enabled;
			getConfig().addProperty("enabled", enabled);
			saveConfig();
		}, enabled));
		NumberElement numberElement = new NumberElement("§6Seconds until reconnect",
				new ControlElement.IconData(Material.WATCH), secondsUntilReconnect);
		numberElement.setRange(5, 60);
		numberElement.addCallback(integer -> {
			this.secondsUntilReconnect = integer;
			getConfig().addProperty("secondsUntilReconnect", integer);
			saveConfig();
		});
		list.add(numberElement);
	}

	public boolean isEnabled() {
		return enabled && (jumpingAddon.getLastServer() != null &&
				!jumpingAddon.getLastServer().serverIP.split(":")[0].toLowerCase().endsWith("labymod.net"));
	}
}
