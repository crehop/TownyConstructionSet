package utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import crehop.BuildPlace;
import crehop.Main;

public class BuildUtils {
	public static boolean enoughRoomForBuildPlace(Chunk chunk, int multiplier){
		if(multiplier == 0){
			multiplier = 1;
		}
		World world = chunk.getWorld();
		int xMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockX();
		int xMax = xMin + (16 * multiplier);
		int zMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockZ();
		int zMax = zMin + (16 * multiplier);		
		Bukkit.broadcastMessage("XMIN = " + xMin + " XMAX = " + xMax + "ZMIN = " + zMin + " ZMAX = " + zMax + "YCHECK = " + Main.yCheckHeight);
		for(int x = xMin; x <= xMax; x++){
			for(int z = zMin; z <= zMax; z++){
				if(x == xMin || x == xMax || z == zMin || z == zMax){
					Block block = world.getBlockAt(x, Main.yCheckHeight ,z);
					if(block.getType() == Material.WOOL){
						return false;
					}
				}
			}
		}
		return true;
	}
	public static boolean setupBuildPlace(Player player, Chunk chunk, int multiplier,String name){
		if(chunk.getWorld().getName().equalsIgnoreCase("build")){
			if(enoughRoomForBuildPlace(chunk,multiplier)){
				int cost = 100000 * multiplier;
				if(enoughMoneyToBuild(player, multiplier)){
					Bukkit.broadcastMessage(ChatColor.RED + "MULTIPLIER INITIAL SET" + multiplier);
					BuildPlace build = new BuildPlace(chunk, multiplier, name, player);
					Main.buildPlaces.add(build);
					return true;
				}
				else{
					player.sendMessage(ChatColor.RED + "NOT ENOUGH MONEY: please find a spot further away from other builds (must be 17 blocks away)");
					return false;
				}
			}else{
				player.sendMessage(ChatColor.RED + "NOT ENOUGH ROOM: please find a spot further away from other builds (must be 17 blocks away)");
				return false;
			}
		}else{
			player.sendMessage(ChatColor.RED + "You can only setup a build location in the build world, Please enter the portal at" + ChatColor.GREEN + "/spawn");
			return false;
		}
		return false;
	}
	public static boolean enoughMoneyToBuild(Player player, int multiplier){
		//TODO add economy integration here
		return true;
	}
}
