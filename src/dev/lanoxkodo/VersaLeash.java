package dev.lanoxkodo;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class VersaLeash extends JavaPlugin {
	
	private FileHandler fileHandler;
	protected boolean usePermissions;
	protected boolean leashedBeInvulnerable;
	
	@Override
	public void onEnable()
	{
		fileHandler = new FileHandler(this);
		usePermissions = fileHandler.isEnabled("usePermissions");
		leashedBeInvulnerable = fileHandler.isEnabled("leashedInvulnerable");
		
		getServer().getPluginManager().registerEvents(new LeashLogic(this), this);
		getLogger().info("VersaLeash has been enabled.");
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info("VersaLeash has been disabled.");
	}
	
	protected boolean permsUsed()
	{
		return usePermissions;
	}
	
	protected boolean permCheck(Player player, String permissionA, String permissionB)
	{
		boolean value = false;
		if (player.isOp() || !permsUsed() || (permsUsed() && (player.hasPermission(permissionA) || player.hasPermission(permissionB)))) value = true;
		
		return value;
	}
}