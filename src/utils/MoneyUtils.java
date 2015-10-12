package utils;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;

import crehop.Main;

public class MoneyUtils {
	public static boolean hasEnoughMoney(Player player, int cost){
		Economy money = Main.economy;
		if(money.getBalance(player) > cost){
			money.withdrawPlayer(player, cost);
			return true;
		}
		return false;
	}
}
