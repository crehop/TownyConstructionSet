package utils;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
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
		if(chunk.getWorld().getName().contains("build")){
			if(enoughRoomForBuildPlace(chunk,multiplier,player)){
				int cost = 10000 * multiplier;
				if(MoneyUtils.hasEnoughMoney(player, cost)){
					BuildPlace build = new BuildPlace(chunk.getBlock(0, 0, 0).getLocation(), multiplier, name, player.getName(), false, cost);
					build.setCost(cost);
					MoneyUtils.withdraw(player,cost);
					SaveLoad.storeData("StoredLocations.txt");
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
	public static int checkID(){
		return ID;
	}
	public static void fixID(int i){
		ID = i;
	}
	public static BuildPlace getBuildPlace(Location location){
		for(BuildPlace place:Main.placesCheck){
			if(place.withinBuildPlace(location)){
				return place;
			}
		}
		return null;
	}
}
