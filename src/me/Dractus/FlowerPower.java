package me.Dractus;

import org.bukkit.plugin.java.JavaPlugin;

public class FlowerPower extends JavaPlugin {

	@Override
	public void onEnable()
	{
		getLogger().info("Dractus' FlowerPower Plugin Enabled***");
		new FlowerListener(this);
	}
	
	@Override
	public void onDisable()
	{
		
	}
}
