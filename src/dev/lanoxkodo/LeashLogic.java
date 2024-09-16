package dev.lanoxkodo;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class LeashLogic implements Listener {
	
	private VersaLeash versaleash;
	
	public LeashLogic(VersaLeash vl)
	{
		versaleash = vl;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent event)
	{
		if (event.getRightClicked() instanceof LivingEntity)
		{
			Player player = event.getPlayer();
			LivingEntity entity = (LivingEntity) event.getRightClicked();
			
			if (versaleash.permCheck(player, "versaleash.leash." + entity.getType(), "versaleash.leash.*"))
			{
				ItemStack itemStack = event.getHand() == EquipmentSlot.OFF_HAND ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand();
				
				if (itemStack.getType() == Material.LEAD)
				{
					if (entity.isLeashed())
					{
						if (entity.getLeashHolder().equals(player)) event.setCancelled(true);
						return;
					}
					
					leashEntity(entity, player, itemStack);
					
					if (itemStack != null && player.getGameMode() != GameMode.CREATIVE)
	            	{
	            		if (itemStack.getAmount() <= 1) player.getInventory().remove(itemStack);
	            		else itemStack.setAmount(itemStack.getAmount() - 1);
	            	}
				}
			}
		}
	}
	
	private void leashEntity(LivingEntity entity, Player player, ItemStack stack)
	{
		Bukkit.getScheduler().runTaskLater(versaleash, new Runnable()
		{
            @Override
            public void run()
            {
            	entity.setLeashHolder(player);
    			leashMonitor(entity);
            }
        }, 1L);
	}
	
	private void leashMonitor(LivingEntity entity)
	{
		Player player = (Player) entity.getLeashHolder();
		
		if (versaleash.leashedBeInvulnerable) entity.setInvulnerable(true);
		
		new BukkitRunnable()
		{	
			@Override
			public void run()
			{	
				if (!entity.isLeashed() || entity.getLeashHolder() != player)
				{
					entity.setInvulnerable(false);
					this.cancel();
					return;
                }
				
				if (player.getLocation().distance(entity.getLocation()) > 7.5) entity.teleport(entity.getLeashHolder());
			}
		}.runTaskTimer(versaleash, 0L, 10L);
	}
}
