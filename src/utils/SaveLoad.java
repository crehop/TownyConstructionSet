package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import crehop.BuildPlace;
import crehop.Main;

public class SaveLoad
{
	private FileConfiguration DataConfig = null;
	private File data = null;
	
	private Main mb;
	private String file;
	private File thefile;
	
	public SaveLoad(Main mb, String newfile)
	{
		this.mb = mb;
		file = newfile;
		thefile = new File(mb.getDataFolder(), newfile);
		if(thefile.exists())
		{
			data = thefile;
		}
		reloadCustomConfig();
		saveCustomConfig();
	}
	
	public void reloadCustomConfig()
	{
	    if (data == null) 
	    {
	    	data = new File(mb.getDataFolder(), file);
	    	DataConfig = YamlConfiguration.loadConfiguration(data);
	    	InputStream defConfigStream = mb.getResource(file);
	    	if (defConfigStream != null) 
	    	{
	    		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	    		DataConfig.setDefaults(defConfig);
	    	}
	    	getCustomConfig().options().copyDefaults(true);
	    	mb.logger.info(file + " did not exist! Generated a new one!");
	    }
	    else
	    {
	    	DataConfig = YamlConfiguration.loadConfiguration(data);
	    }
	}

	public FileConfiguration getCustomConfig()
	{
	    if (DataConfig == null)
	    {
	        reloadCustomConfig();
	    }
	    return DataConfig;
	}

	public void saveCustomConfig()
	{
	    if (DataConfig == null || data == null)
	    {
	    	return;
	    }
	    	try
	    	{
	         getCustomConfig().save(data);
	    	}
	    	catch(IOException ex)
	    	{
	    		ex.printStackTrace();
	    		mb.getLogger().log(Level.SEVERE, "Could not save config to " + data, ex);
	    	}
	    
	}
	
	public static void readStoredData(String file)
	{
		String fileName = file;
		File dir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ").replaceAll( ".jar", ""));
		File actualFile = new File (dir, fileName);
		// This will reference one line at a time
		String line = null;
		if(!Main.placesCheck.iterator().hasNext())
		{
			Main.placesCheck.clear();
		}
		try
		{
			FileReader fileReader = new FileReader(actualFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null)
			{
				try
				{
					StringTokenizer st= new StringTokenizer(line, "/");
					int x = Integer.parseInt(st.nextToken()),
						y = Integer.parseInt(st.nextToken()),
					    z = Integer.parseInt(st.nextToken());
					World world = Bukkit.getServer().getWorld(st.nextToken());
					Location loc = new Location(world, x, y, z);
					String owner = st.nextToken();
					int cost = Integer.parseInt(st.nextToken());
					int multiplier = Integer.parseInt(st.nextToken());
					String name = st.nextToken();
					
					new BuildPlace(loc, multiplier, name, owner, true, cost);
					BuildUtils.syncID();
					if(loc.getBlock() == null)
					{
						continue;
					}
					if(loc.getBlock().getState() == null)
					{
						continue;
					}
					if(!(loc.getBlock().getState() instanceof Sign))
					{
						Block block = loc.getBlock();
						block.setType(Material.SIGN_POST);
					}
					try
					{
						Sign sign = (Sign)loc.getBlock().getState();
						sign.setLine(0, ChatColor.GREEN + name);
						sign.setLine(1, ChatColor.RED + "" + cost);
						sign.setLine(2, "");
						sign.setLine(3, owner);
						sign.update();
					}
					catch(Exception ed)
					{
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			bufferedReader.close();		
		}
		catch(FileNotFoundException ex)
		{
			Bukkit.getLogger().info("[SEVERE]:Unable to open file '" + actualFile + "'");				
		}
		catch(IOException ex)
		{
			Bukkit.getLogger().info("[SEVERE]: Error reading file " + actualFile);
		}
	}
	
	public static void storeData(String file)
	{
		String fileName = file;
		File dir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ").replaceAll( ".jar", ""));
		File actualFile = new File (dir, fileName);
		Bukkit.broadcastMessage(fileName + dir.toString() + dir.canRead() + actualFile.canRead() + actualFile.canWrite() 	+	actualFile.getAbsolutePath());
		try
		{
			FileWriter fileWriter = new FileWriter(actualFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			Iterator<BuildPlace> itr = Main.placesCheck.iterator();
			while(itr.hasNext())
			{
				BuildPlace place = itr.next();
				fileWriter.write(place.getSignLocation().getBlockX() + "/" + place.getSignLocation().getBlockY() + "/" + place.getSignLocation().getBlockZ() + "/" + place.getSignLocation().getWorld().getName() +
						"/" + place.getOwner() + "/" + place.getCost() + "/" + place.getMultiplier() + "/" + place.getID() + "\n" );
			}
			bufferedWriter.close();			
		}
		catch(FileNotFoundException ex)
		{
			Bukkit.getLogger().info("[SEVERE]:Unable to write file '" + actualFile + "'");				
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			Bukkit.getLogger().info("[SEVERE]: Error writing file " + actualFile);
			ex.printStackTrace();
		}
	}
	
	public static void storeBackupData(String name)
	{
		storeData(name);
	}
	
	public static void restoreBackupData(String name)
	{
		Main.placesCheck.clear();
		Main.buildPlaces.clear();
		readStoredData(name);
	}
}
