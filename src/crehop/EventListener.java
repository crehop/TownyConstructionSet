package crehop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

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
	public void blockBreakEvent(BlockBreakEvent event){
		if(event.getBlock().getWorld().getName().toString().contains("build")){
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
				Bukkit.broadcastMessage("PLACE CONFIRMED!" + place.getOwner());
			}
		}
	}
	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent event){
		if(event.getBlock().getWorld().getName().toString().contains("build")){
			if(event.getBlock().getY() <= 5){
				if(event.getBlock().getType() == Material.WOOL || event.getBlock().getType() == Material.SIGN_POST){
					event.setCancelled(true);
					event.getPlayer().sendMessage("Cannot place wool or standing signs at this height");
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
				Bukkit.broadcastMessage("PLACE CONFIRMED!" + place.getOwner());
			}
		}
	}
}
	
		
		