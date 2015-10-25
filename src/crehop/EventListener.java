package crehop;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import utils.BuildUtils;


public class EventListener implements Listener{

	public static Main plugin;
	public EventListener(Main instance){
		plugin = instance;
	}	

	@EventHandler	
	//event type replaces PlayerInteractEntityEvent
	public void Event1 (PlayerInteractEntityEvent event){
	}
	
	@EventHandler
	public void tele(PlayerTeleportEvent e){
		if(e.getFrom().getWorld().toString().contains("build")){
			if(!(e.getTo().getWorld().toString().equalsIgnoreCase(e.getFrom().getWorld().toString()))){
				for(BuildPlace place:Main.placesCheck){
					if(place.getOwner().equalsIgnoreCase(e.getPlayer().getName())){
						if(place.isLockedout()){
							place.unlock();
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void logoutEvent(PlayerQuitEvent e){
		for(BuildPlace place:Main.placesCheck){
			if(place.getOwner().equalsIgnoreCase(e.getPlayer().getName())){
				if(place.isLockedout()){
					place.unlock();
				}
			}
		}
	}
	@EventHandler
	public void blockBreakEvent(BlockBreakEvent event){
		if(event.getBlock().getWorld().getName().toString().contains("build") && event.getPlayer().isOp() == false){
			if(event.getBlock().getY() <= 5){
				if(event.getBlock().getType() == Material.WOOL || event.getBlock().getType() == Material.SIGN_POST){
					event.setCancelled(true);
					event.getPlayer().sendMessage("Cannot destroy wool or statnding signs at this height");
				}
			}
			if(BuildUtils.getBuildPlace(event.getBlock().getLocation()) == null){
				event.setCancelled(true);
			}else{
				BuildPlace place = BuildUtils.getBuildPlace(event.getBlock().getLocation());
				if(place.getOwner().equalsIgnoreCase(event.getPlayer().getName())){
					
				}else{
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent event){
		if(event.getBlock().getWorld().getName().toString().contains("build") && event.getPlayer().isOp() == false){
			if(event.getBlock().getY() <= 5){
				if(event.getBlock().getType() == Material.WOOL || event.getBlock().getType() == Material.SIGN_POST){
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "Cannot place wool or standing signs at this height");
				}
			}
			if(BuildUtils.getBuildPlace(event.getBlock().getLocation()) == null){
				event.setCancelled(true);
			}else{
				BuildPlace place = BuildUtils.getBuildPlace(event.getBlock().getLocation());
				if(place.getOwner().equalsIgnoreCase(event.getPlayer().getName())){
					if(event.getBlock().getLocation().getBlock().getY() > (16 * place.getMultiplier()) + 3){
						event.setCancelled(true);
						event.getPlayer().sendMessage(ChatColor.RED + "YOU HAVE REACHED THE TOP OF YOUR BUILDPLOT! CANNOT BUILD THIS HIGH!");
					}
				}else{
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void chestOpenEvent(InventoryOpenEvent e){
		if(e.getPlayer().getWorld().getName().toString().contains("build") && e.getPlayer().isOp() == false){
			if(e.getInventory().getHolder() instanceof Chest || e.getInventory().getHolder() instanceof DoubleChest || e.getInventory().getHolder() instanceof Dispenser 
				 ||e.getInventory().getHolder() instanceof Hopper){
				 e.setCancelled(true);
			}
		}
	}
}
	
		
		