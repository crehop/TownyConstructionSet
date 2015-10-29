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

import com.palmergames.bukkit.towny.object.TownyUniverse;

import utils.BuildUtils;
import utils.MoneyUtils;
import utils.SaveLoad;
import utils.TownyUtils;

public class Main extends JavaPlugin{
	public final Logger logger=Logger.getLogger("Minecraft");
    public static Main plugin;
	public static ArrayList<BlockQueue> activeQueues = new ArrayList<BlockQueue>();
	public static ArrayList<BuildPlace> placesCheck = new ArrayList<BuildPlace>();
    public File configFile;
    public static boolean debug = false; 
    public static long queueSpeed = 1;
    public static int yCheckHeight = 3;
	public static Economy economy;
	public static HashMap<String,BuildPlace> buildPlaces = new HashMap<String,BuildPlace>();
    public static Permission perms = null;
    public static Chat chat = null;
    public static Location teleport;
    public static Location test;


 
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
		    		}
		    	}
		    	else if(debug){
		    	}
		    }
		}, 0L, queueSpeed);

	 	plugin = this;
	 	PluginDescriptionFile pdfFile=this.getDescription();
	 	logger.info(pdfFile.getName() + " Has Been Enabled!!");
	 	SaveLoad.readStoredData("StoredLocations.txt");
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
			if(args.length == 0){
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/build place" + ChatColor.AQUA + " in the build world builds a plot");
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/construct #" + ChatColor.AQUA + " builds the plot at number #");
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/build lockout" + ChatColor.AQUA + " makes your lot private/kicks people out");
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/build lockout" + ChatColor.AQUA + " if your plot is locked, unlocks it!");
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/build tp" + ChatColor.AQUA + " in the build world tps to your plot");
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/build tp #" + ChatColor.AQUA + " in the build world tps a plot at #");
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/build addbuddy name" + ChatColor.AQUA + " Add somone to help you build!");
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/build removebuddy name" + ChatColor.AQUA + " Remove somone from your build!");
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/build clearbuddies" + ChatColor.AQUA + " Remove all build buddies!");
				player.sendMessage(ChatColor.YELLOW + "[Build]:" + ChatColor.GREEN + "/build destroy" + ChatColor.RED + " DESTROYS YOUR PLOT!");
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("place")){
				if(player.getWorld().getBlockAt(location).getType().toString().contains("BEDROCK")){
					player.sendMessage(ChatColor.RED + "CANNOT BUILD ON THE KINGS ROAD!");
					return false;
				}
				for(BuildPlace place:placesCheck){
					if(player.isOp()){
					}else if(place.getOwner().equals(player.getName())){
						player.sendMessage(ChatColor.RED + "YOU CAN ONLY HAVE 1 BUILD PLOT FOR NOW!!");
						return true;
					}
				}
				BuildUtils.setupBuildPlace(player, player.getLocation().getChunk(), 2, ""+BuildUtils.getID());
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("list")){
				player.sendMessage("BUILD SIZE :" + buildPlaces.size());
				player.sendMessage("" + buildPlaces.values());
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("destroy")){
				for(BuildPlace place:placesCheck){
					if(place.owner.equalsIgnoreCase(player.getName())){
						place.destroy();
						player.sendMessage(ChatColor.RED + "BUILD DESTROYED!");
						break;
					}
				}
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("tp")){
				if(player.getWorld().toString().contains("build")){
					for(BuildPlace place:placesCheck){
						if(place.owner.equalsIgnoreCase(player.getName())){
							teleport = place.chunkLocation.getChunk().getBlock(0, 0, 0).getLocation().add(-10,5,0);
							player.teleport(teleport);
							return true;
						}
					}
					player.sendMessage(ChatColor.RED + "Sorry " + args[0] + " not found, are you sure the name was correct?");
				}
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("clearbuddies")){
				if(player.getWorld().toString().contains("build")){
					for(BuildPlace place:placesCheck){
						if(place.owner.equalsIgnoreCase(player.getName())){
							player.sendMessage(place.clearBuddies());
							return true;
						}
					}
				}
			}
			if(args.length == 2 && args[0].equalsIgnoreCase("addbuddy")){
				if(player.getWorld().toString().contains("build")){
					for(BuildPlace place:placesCheck){
						if(place.owner.equalsIgnoreCase(player.getName())){
							player.sendMessage(place.addBuddy(args[1]));
							return true;
						}
					}
				}
			}
			if(args.length == 2 && args[0].equalsIgnoreCase("removebuddy")){
				if(player.getWorld().toString().contains("build")){
					for(BuildPlace place:placesCheck){
						if(place.owner.equalsIgnoreCase(player.getName())){
							player.sendMessage(place.removeBuddy(args[1]));
							return true;
						}
					}
					player.sendMessage(ChatColor.RED + "Sorry " + args[0] + " not found, are you sure the name was correct?");
				}
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("lockout")){
				for(BuildPlace place:placesCheck){
					if(place.getOwner().equalsIgnoreCase(player.getName())){
						if(place.isLockedout()){
							place.unlock();
							player.sendMessage(ChatColor.RED + "PLOT UNLOCKED!");
						}else{
							place.lock();
							player.sendMessage(ChatColor.RED + "PLOT LOCKED DOWN AND PLAYERS KICKED!");
							for(Player player2:Bukkit.getOnlinePlayers()){
								try{
								if(BuildUtils.getBuildPlace(player2.getLocation()).getID().equalsIgnoreCase(place.getID()) && (!(place.owner.equalsIgnoreCase(player2.getName()))) && (!(place.checkBuddy(player2.getName())))){
									teleport = place.chunkLocation.getChunk().getBlock(0, 0, 0).getLocation().add(-10,5,0);
									player2.teleport(teleport);
									player2.sendMessage(ChatColor.RED + "" + place.getOwner() + " Has removed you and locked the build plot!");
									if(!(place.owner.equalsIgnoreCase(player.getName())))player.sendMessage("" + ChatColor.RED + "" + player.getName() + " has beed removed from your plot!");
								}
								}catch(NullPointerException e){
									
								}
							}
						}
						break;
					}
				}
			}	
			if(args.length == 2 && args[0].equalsIgnoreCase("tp")){
				if(player.getWorld().toString().contains("build")){
					for(BuildPlace place:placesCheck){
						if(place.name.equalsIgnoreCase(args[1])){
							teleport = place.chunkLocation.getChunk().getBlock(0, 0, 0).getLocation().add(-10,5,0);
							player.teleport(teleport);
							return true;
						}
					}
					player.sendMessage(ChatColor.RED + "Sorry " + args[0] + " not found, are you sure the name was correct?");
				}
			}
			if(args.length == 2 && args[0].equalsIgnoreCase("destroy")){
				if(player.isOp()){
					for(BuildPlace place:placesCheck){
						player.sendMessage("REQUESTED:" + args[1] + " PLACE:" + place.getID());
						if(place.getID().equalsIgnoreCase(args[1])){
							player.sendMessage("confirmed!");
							place.destroy();
							break;
						}
					}
					player.sendMessage(ChatColor.RED + "BUILD DESTROYED!");
				}
			}
			return true;
		}
		if(commandLabel.equalsIgnoreCase("construct")){
			if(args.length > 0){
				if(!(buildPlaces.containsKey(args[0]))){
					player.sendMessage("BUILDPLACE DOES NOT EXIST PLEASE TRY AGAIN" + buildPlaces.keySet());
				}else{
					if(player.getLocation().getWorld().toString().contains("test")){
						if(MoneyUtils.hasEnoughMoney(player, buildPlaces.get(args[0]).cost)){
							if(TownyUniverse.isWilderness(location.getBlock())){
								player.sendMessage(ChatColor.RED + "YOU MUST BE IN A TOWN TO BUILD!");
								return true;
							}
							test = player.getLocation().getBlock().getChunk().getBlock(0, 0, 0).getLocation();
							if(TownyUtils.isTownOwner(TownyUtils.getTownAtLocation(test), player )) {
								test.setX(test.getX() + 16 * buildPlaces.get(args[0]).getMultiplier() - 1);
								if(TownyUtils.isTownOwner(TownyUtils.getTownAtLocation(test), player )) {
									test.setZ(test.getZ() + 16 * buildPlaces.get(args[0]).getMultiplier() - 1);
									if(TownyUtils.isTownOwner(TownyUtils.getTownAtLocation(test), player )) {
										float split = (buildPlaces.get(args[0]).cost/2);
										MoneyUtils.withdraw(player, buildPlaces.get(args[0]).cost);
										MoneyUtils.deposit("realmtaxs", (int)split);
										MoneyUtils.deposit(buildPlaces.get(args[0]).owner, (int)split);
										new BlockQueue(buildPlaces.get(args[0]).chunk,player.getLocation().getChunk(),buildPlaces.get(args[0]).getMultiplier(), player.getLocation().getBlockY() -4);
										return true;
									}else{
										player.sendMessage(ChatColor.RED + "YOU MUST BE TO OWNER OF THE TOWN TO PLACE A BUILD! (PART OF THE BUILD MAY BE OUTSIDE THE TOWN!)");
									}
								}else{
									player.sendMessage(ChatColor.RED + "YOU MUST BE TO OWNER OF THE TOWN TO PLACE A BUILD! (PART OF THE BUILD MAY BE OUTSIDE THE TOWN!)");
								}
							}else{
								player.sendMessage(ChatColor.RED + "YOU MUST BE TO OWNER OF THE TOWN TO PLACE A BUILD! (PART OF THE BUILD MAY BE OUTSIDE THE TOWN!)");
							}
						}else{
							player.sendMessage(ChatColor.RED + "YOU MUST BE TO OWNER OF THE TOWN TO PLACE A BUILD! (PART OF THE BUILD MAY BE OUTSIDE THE TOWN!)");
						}	
					}else{
						player.sendMessage(ChatColor.RED + "YOU MUST BE TO OWNER OF THE TOWN TO PLACE A BUILD! (PART OF THE BUILD MAY BE OUTSIDE THE TOWN!)");
					}
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
        }

        return (economy != null);
    }
}