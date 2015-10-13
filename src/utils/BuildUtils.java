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
	private static int ID = 0;
	private static boolean check = false;
	public static boolean enoughRoomForBuildPlace(Chunk chunk, int multiplier, Player player){
		if(multiplier == 0){
			multiplier = 1;
		}
		World world = chunk.getWorld();
		int xMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockX() - 16;
		int xMax = xMin + (16 * multiplier) + 16;
		int zMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockZ() - 16;
		int zMax = zMin + (16 * multiplier) + 16;	
		Bukkit.broadcastMessage("XMIN = " + xMin + " XMAX = " + xMax + "ZMIN = " + zMin + " ZMAX = " + zMax + "YCHECK = " + Main.yCheckHeight);
		check = false;
		for(int x = xMin; x <= xMax; x++){
			for(int z = zMin; z <= zMax; z++){
					Block block = world.getBlockAt(x, Main.yCheckHeight ,z);
					if(block.getType() == Material.WOOL){
						check = true;
				}
			}
		}
		if(check == true){
			player.sendMessage(ChatColor.RED + "NOT FAR ENOUGH: please find a spot further away from other builds (must be 17 blocks away)");
			return false;
		}
		check = false;
		for(int x = xMin - 17; x <= xMax + 17; x++){
			for(int z = zMin; z <= zMax; z++){
					Block block = world.getBlockAt(x, Main.yCheckHeight ,z);
					if(block.getType() == Material.WOOL){
						check = true;
				}
			}
		}
		for(int x = xMin; x <= xMax; x++){
			for(int z = zMin - 17; z <= zMax + 17; z++){
					Block block = world.getBlockAt(x, Main.yCheckHeight ,z);
					if(block.getType() == Material.WOOL){
						check = true;
				}
			}
		}
		if(check == false){
			player.sendMessage(ChatColor.RED + "TOO FAR!: please find a spot closer to other builds (must be 17 blocks away)");
			return false;
		}
		return true;
	}
	public static boolean setupBuildPlace(Player player, Chunk chunk, int multiplier,String name){
		if(chunk.getWorld().getName().equalsIgnoreCase("build")){
			if(enoughRoomForBuildPlace(chunk,multiplier,player)){
				int cost = 10000 * multiplier;
				if(MoneyUtils.hasEnoughMoney(player, cost)){
					Bukkit.broadcastMessage(ChatColor.RED + "MULTIPLIER INITIAL SET" + multiplier);
					BuildPlace build = new BuildPlace(chunk, multiplier, name, player);
					build.setCost(cost);
					MoneyUtils.withdraw(player,cost);
					return true;
				}else{
					player.sendMessage(ChatColor.RED + "NOT ENOUGH MONEY: please find more Money, you need: $" + cost + " to have a buildspace!");
					return false;
				}
			}else{
				return false;
			}
		}else{
			player.sendMessage(ChatColor.RED + "You can only setup a build location in the build world, Please enter the portal at" + ChatColor.GREEN + "/spawn");
			return false;
		}
	}
	public static int getID() {
		ID = (ID + 1);
		return ID;
	}
	public void syncID(int newID){
		this.ID = newID;
	}
}
