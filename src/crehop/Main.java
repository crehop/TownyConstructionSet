package crehop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	public final Logger logger=Logger.getLogger("Minecraft");
    public static Main plugin;
	public static ArrayList<BlockQueue> activeQueues = new ArrayList<BlockQueue>();
    public File configFile;
    public static boolean debug = true; 
    


 
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
		    	}
		    	else if(debug){
		    		Bukkit.broadcastMessage("NO QUEUES");
		    	}
		    }
		}, 0L, 5L);

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
	
}