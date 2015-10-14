package utils;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import crehop.Main;

public class BlockUtils {
	static WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

	/**
	 * This method is used by:
	 * This method uses:
	 * 
	 * @param toBeCopied
	 * @param toBePasted
	 */
	public static void build(Block toBeCopied, Block toBePasted){	
		if(toBeCopied.getChunk().isLoaded() == false){
			toBeCopied.getChunk().load();
		}
		if(toBePasted.getChunk().isLoaded() == false){
			toBePasted.getChunk().load();
		}
		EditSession es = new EditSession(com.sk89q.worldedit.bukkit.BukkitUtil.getLocalWorld(toBeCopied.getWorld()),Integer.MAX_VALUE);
	    Vector bvmin = BukkitUtil.toVector(toBeCopied.getLocation()).toBlockPoint();
	    Vector bvmax = BukkitUtil.toVector(toBeCopied.getLocation()).toBlockPoint();
	    Vector pos = bvmax;
	    CuboidClipboard clipboard = new CuboidClipboard(bvmax.subtract(bvmin).add(new Vector(1, 1, 1)),bvmin, bvmin.subtract(pos));
	    clipboard.copy(es);
	    es = new EditSession(com.sk89q.worldedit.bukkit.BukkitUtil.getLocalWorld(toBePasted.getWorld()),Integer.MAX_VALUE);
	    try {
			clipboard.paste(es, BukkitUtil.toVector(toBePasted), false);
			if(Main.debug){
				
			}
		} catch (MaxChangedBlocksException e) {
			if(Main.debug){
			}
		} 
		
	}
}
