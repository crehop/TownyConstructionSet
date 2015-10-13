package crehop;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;


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
		Bukkit.broadcastMessage("EVENT" + event.getBlock().getType());
	}
	
}
	
		
		