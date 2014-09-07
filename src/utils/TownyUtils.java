package utils;

import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.command.TownyAdminCommand;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyObject;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.TownyWorld;

public class TownyUtils {
	public static Town getTownWhereStanding(Player player){
		Town town = null;
		try {
			town = TownyUniverse.getTownBlock(player.getLocation()).getTown();
		} catch (NotRegisteredException e) {
		}
		return town;
	}
	
	public static boolean isTownOwner(Town town, Player player){
		boolean canbuild = false;
		boolean isResident = false;
		Resident resident = null;
		for(Resident res:town.getResidents()){
			try {
				if(TownyUniverse.getPlayer(res).getName().equals(player.getName())){
					isResident = true;
					resident = res;
				}
			} catch (TownyException e) {
			}
		}
		if(isResident){
			if(town.getMayor().getName().equals(resident.getName())){
				canbuild = true;
			}
		}
		return canbuild;
	}
	
	public static boolean isKing(Player player){
		 Resident resident = new Resident(player.getName());
		 if(resident.isKing()){
			 return true;
		 }
		 return false;
	}
}
