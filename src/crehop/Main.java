package crehop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import utils.BuildUtils;

public class Main extends JavaPlugin{
	public final Logger logger=Logger.getLogger("Minecraft");
    public static Main plugin;
	public static ArrayList<BlockQueue> activeQueues = new ArrayList<BlockQueue>();
	public static ArrayList<BuildPlace> buildPlaces = new ArrayList<BuildPlace>();
    public File configFile;
    public static boolean debug = false; 
    public static long queueSpeed = 1;
    public static int yCheckHeight = 3;
	public static Economy economy;


 
 @Override
 	public void onDisable()
 		{
	 	PluginDescriptionFile pdfFile=this.getDescription();
	 	logger.info(pdfFile.getName() + " Has Been Disabled!!");
 		}
 
 	public void onEnable(){
 	    Bukkit.getServer().getPluginManager().registerEvents(new EventListener(this), this);
 		configFile = new File(getDataFolder(), "config.yml");
 		try{
 			firstRun();
 		}
 		catch (Exception e)
 		{
 			e.printStackTrace();
 		}
 		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
 			int tick = 0;
		    @Override  
		    public void run()
		    {
		    	if(activeQueues.isEmpty() == false){
		    		tick++;
		    		if(tick > activeQueues.size() - 1){
		    			tick = 0;
		    		}
		    		activeQueues.get(tick).tickBuilder();
		    		if(debug){
		    			Bukkit.broadcastMessage(ChatColor.GREEN + "ACTIVE QUEUES FOUND: " + activeQueues.size() + " Atempting tick on queue: " + (tick + 1));
		    		}
		    	}
		    	else if(debug){
		    		//Bukkit.broadcastMessage(ChatColor.RED + "NO QUEUES" + activeQueues.size());
		    	}
		    }
		}, 0L, queueSpeed);

	 	plugin = this;
	 	PluginDescriptionFile pdfFile=this.getDescription();
	 	logger.info(pdfFile.getName() + " Has Been Enabled!!");
 	}
	private void firstRun() throws Exception{
		if(!configFile.exists())
		    {
		        configFile.getParentFile().mkdirs();
		        copy(getResource("config.yml"), configFile);
		    }
	}
	private void copy(InputStream in, File file){
	     
		try
	     {
	         OutputStream out = new FileOutputStream(file);
	         byte[] buf = new byte[1024];
	         int len;
	         while((len=in.read(buf))>0)
	         {
	             out.write(buf,0,len);
	         }
	         out.close();
	         in.close();
	     }
	     catch (Exception e)
	     {
	         e.printStackTrace();
	     }
	 
		
	}
	public boolean onCommand(CommandSender sender,Command cmd,String commandLabel, String[] args)
	{

		Player player = (Player) sender;
		if(commandLabel.equalsIgnoreCase("build")){
//			BlockQueue test = new BlockQueue(Bukkit.getWorld("world").getChunkAt(new Location(Bukkit.getWorld("world"),0,0,0)), player.getLocation().getChunk(), 1, player.getLocation().getBlockY());
//			player.sendMessage("ATTEMPTING BLOCK BUILD QUEUE, ESTIMATED TIME REMAINING = " + ((100/5) * test.getTotalBlocksInQueue()) );
			BuildUtils.setupBuildPlace(player, player.getLocation().getChunk(), 1, "TEST");
			return true;
		}
		if(commandLabel.equalsIgnoreCase("build2")){
			BlockQueue test = new BlockQueue(Bukkit.getWorld("world").getChunkAt(new Location(Bukkit.getWorld("world"),0,0,0)), player.getLocation().getChunk(), 10, player.getLocation().getBlockY());
			player.sendMessage("ATTEMPTING BLOCK BUILD QUEUE, ESTIMATED TIME REMAINING = " + ((100/5) * test.getTotalBlocksInQueue()) );
			return true;
		}
		return false;
	}
}