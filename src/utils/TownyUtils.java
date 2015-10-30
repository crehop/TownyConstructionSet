package utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import crehop.BuildPlace;
import crehop.Main;


public class TownyUtils {
	private static Chunk chunk;
	private static Location location = new Location(null,0,0,0);
	public static Town getTownAtLocation(Location loc){
		Town town = null;
		try {
			town = TownyUniverse.getTownBlock(loc).getTown();
		} catch (NotRegisteredException e) {
		}catch(NullPointerException e2){
		}
		return town;
	}
	
	public static boolean isTownOwner(Town town, Player player){
		boolean canbuild = false;
		boolean isResident = false;
		Resident resident = null;
		try{
		for(Resident res:town.getResidents()){
			try {
				if(TownyUniverse.getPlayer(res).getName().equals(player.getName())){
					isResident = true;
					resident = res;
				}
			} catch (TownyException e) {
				player.sendMessage(ChatColor.RED + "YOU MUST BE IN A TOWN TO BUILD!");
			}
		}
		if(isResident){
			if(town.getMayor().getName().equals(resident.getName())){
				canbuild = true;
			}
		}
		return canbuild;
		}catch(NullPointerException e2){
			return false;
		}
	}
	public static boolean isPlotOwner(Town town, Location location, Player player){
		for(TownBlock plot:town.getTownBlocks()){
			try {
				if(plot.getResident().getName().equalsIgnoreCase(player.getName())){
					location.setWorld(Bukkit.getWorld(plot.getWorld().getName()));
					location.setX(plot.getWorldCoord().getX());
					location.setZ(plot.getWorldCoord().getZ());
					
				}
			} catch (NotRegisteredException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return true;
	}
	
	public static boolean isKing(Player player){
		 Resident resident = new Resident(player.getName());
		 if(resident.isKing()){
			 return true;
		 }
		 return false;
	}
	public static boolean confirmEnoughRoom(BuildPlace build, Chunk chunk, Player player){
		Location loc = chunk.getBlock(0, 1, 0).getLocation();
		if(isTownOwner(getTownAtLocation(loc),player)){
			World world = chunk.getWorld();
			int xMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockX();
			int xMax = xMin + (16 * build.getMultiplier()) -1;
			int zMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockZ();
			int zMax = zMin + (16 * build.getMultiplier()) -1;		
			for(int x = xMin; x <= xMax; x++){
				for(int z = zMin; z <= zMax; z++){
					if(x == xMin || x == xMax || z == zMin || z == zMax){
						Block block = world.getBlockAt(x, Main.yCheckHeight ,z);
						if(isTownOwner(getTownAtLocation(block.getLocation()),player) == false){
							return false;
						}
					}
				}
			}
			return true;
		}
		else{
			player.sendMessage(ChatColor.RED + "You do not own this town, You cannot build here!");
			return false;
		}
	}
}
