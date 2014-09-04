package utils;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import crehop.Main;

public class BlockUtils {
	WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

	public static void build(Block toBeCopied, Block toBePasted){
	    EditSession es = new EditSession((LocalWorld) toBeCopied.getWorld(),Integer.MAX_VALUE);
	    Vector bvmin = BukkitUtil.toVector(toBeCopied.getLocation()).toBlockPoint();
	    Vector bvmax = BukkitUtil.toVector(toBeCopied.getLocation()).toBlockPoint();
	    Vector pos = bvmax;
	    CuboidClipboard clipboard = new CuboidClipboard(bvmax.subtract(bvmin).add(new Vector(1, 1, 1)),bvmin, bvmin.subtract(pos));
	    clipboard.copy(es);
	    es = new EditSession((LocalWorld) toBePasted.getWorld(),Integer.MAX_VALUE);
	    try {
			clipboard.paste(es, BukkitUtil.toVector(toBePasted), false);
			if(Main.debug){
				Bukkit.broadcastMessage("PASTE ATTEMPTED AT:" + toBePasted.getLocation());
			}
		} catch (MaxChangedBlocksException e) {
			if(Main.debug){
				Bukkit.broadcastMessage("PASTE FAILED");
			}
		} 
		
	}
}
