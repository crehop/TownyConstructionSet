package Default;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;


public class EventListener implements Listener{

		public static Main plugin;
		public EventListener(Main instance){
			plugin = instance;
		}	

	@EventHandler	
	//event type replaces PlayerInteractEntityEvent
	public void Event1 (PlayerInteractEntityEvent event){
	//code goes here
	}
	
}
	
		
		