package crehop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import utils.BuildUtils;
import utils.MoneyUtils;

public class Main extends JavaPlugin{
	public final Logger logger=Logger.getLogger("Minecraft");
    public static Main plugin;
	public static ArrayList<BlockQueue> activeQueues = new ArrayList<BlockQueue>();
    public File configFile;
    public static boolean debug = false; 
    public static long queueSpeed = 1;
    public static int yCheckHeight = 3;
	public static Economy economy;
	public static HashMap<String,BuildPlace> buildPlaces = new HashMap<String,BuildPlace>();
    public static Permission perms = null;
    public static Chat chat = null;

 
 @Override
 	public void onDisable()
 		{
	 	PluginDescriptionFile pdfFile=this.getDescription();
	 	logger.info(pdfFile.getName() + " Has Been Disabled!!");
	 
 		}
 
 	public void onEnable(){
 	    Bukkit.getServer().getPluginManager().registerEvents(new EventListener(this), this);
 		configFile = new File(getDataFolder(), "config.yml");
 		setupEconomy();
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
		Location location = player.getLocation();
		location.setY(3);
		if(commandLabel.equalsIgnoreCase("build")){
			if(player.getWorld().getBlockAt(location).getType().toString().contains("BEDROCK")){
				player.sendMessage(ChatColor.RED + "CANNOT BUILD ON THE KINGS ROAD!");
				return false;
			}else{
				player.sendMessage("BUILD1 " + args.length + MoneyUtils.hasEnoughMoney(player, 10000));
				if(args.length == 0){
					BuildUtils.setupBuildPlace(player, player.getLocation().getChunk(), 1, ""+BuildUtils.getID());
				}
				if(args.length == 1 && args[0].equalsIgnoreCase("list")){
					player.sendMessage("BUILD2 " + args.length + args[0]);
					player.sendMessage("BUILD SIZE :" + buildPlaces.size());
				}
				return true;
			}
		}
		if(commandLabel.equalsIgnoreCase("build2")){
			if(args.length > 0){
				if(!(buildPlaces.containsKey(args[0]))){
					player.sendMessage("BUILDPLACE DOES NOT EXIST PLEASE TRY AGAIN" + buildPlaces.keySet());
				}else{
					Bukkit.broadcastMessage("SIZE" + buildPlaces.size() + " NAME" + buildPlaces.values());
					
					BlockQueue test = new BlockQueue(buildPlaces.get(args[0]).chunk,player.getLocation().getChunk(),1, player.getLocation().getBlockY() -4);
					player.sendMessage("SUCCESS, BUILDING" + buildPlaces.size());
					return true;
				}
			}
		}
		return false;
	}

	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null)
        {
            economy = economyProvider.getProvider();
            Bukkit.broadcastMessage("ECONOMY SET UP!");
        }

        return (economy != null);
    }
}