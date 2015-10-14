package utils;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;

import crehop.Main;

public class MoneyUtils {
	private static Economy money = Main.economy;
	public static boolean hasEnoughMoney(Player player, int cost){
		if(money.getBalance(player) >= cost){
			return true;
		}
		return false;
	}
	public static void withdraw(Player player, int cost){
		if(money.getBalance(player)>=cost){
			money.withdrawPlayer(player, cost);
		}else{
			money.withdrawPlayer(player, money.getBalance(player));
		}
	}
	@SuppressWarnings("deprecation")
	public static void deposit(String name, int amount) {
		money.depositPlayer("realmtaxs", amount);
	}
}
