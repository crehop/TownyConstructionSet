package crehop;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BuildPlace {
	int xMin;
	int xMax;
	int zMin;
	int zMax;
	int yMin;
	int yMax;
	int multiplier;
	World world;
	String name;
	Chunk chunk;
	Player owner;
	int cost;
	
	public BuildPlace(Chunk chunk, int multiplier, String name, Player owner){
		if(multiplier < 1){
			this.multiplier = 1;
		}
		else{
			this.multiplier = multiplier;
			Bukkit.broadcastMessage("MULTIPLIER SET TO = " + multiplier);
		}
		Bukkit.broadcastMessage("MULTIPLIER = " + this.multiplier);
		this.owner = owner;
		xMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockX();
		xMax = xMin + (16 * this.multiplier) - 1;
		zMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockZ();
		zMax = zMin + (16 * this.multiplier) - 1;
		yMin = 3;
		yMax = yMin + (16 * this.multiplier) - 1;
		this.world = chunk.getWorld();
		this.name = name;
		this.chunk = chunk;
		this.multiplier = multiplier;
		this.buildOutline();
		Main.buildPlaces.put(this.name, this);
		Bukkit.broadcastMessage("BUILDPLACE " + name + " CREATED!");
	}

	public Location getSignLocation() {
		return new Location(world,xMin,(Main.yCheckHeight + 1),zMin);
	}
	
	public boolean withinBuildPlace(Location loc){
		int x = loc.getBlockX();
		int z = loc.getBlockZ();
		int y = loc.getBlockY();
		String world = loc.getWorld().getName();
		if(world.equals(this.getWorld().getName()))
		if(x >= xMin && x <= xMax){
			if(z >= zMin && z <= zMax ){
				if(y >= yMin && y <= yMax){
					return true;
				}
			}
		}
		return false;
	}

	private World getWorld() {
		return world;
	}
	
	@SuppressWarnings("deprecation")
	public void buildOutline(){
		for(int x = xMin; x <= xMax; x++){
			for(int z = zMin; z <= zMax; z++){
				if(x == xMin || x == xMax || z == zMin || z == zMax){
					Block block = world.getBlockAt(x, Main.yCheckHeight ,z);
					block.setType(Material.WOOL);
					block.setData(DyeColor.RED.getData());
				}
			}
		}
		for(int y = yMin; y <= yMax; y++){
			Block block = world.getBlockAt(xMax, y ,zMax);
			block.setType(Material.WOOL);
			block.setData(DyeColor.RED.getData());
		}
	}
	public int getMultiplier(){
		return this.multiplier;
	}

	public void setCost(int cost2) {
		this.cost = cost2;
	}
	public int getCost(){
		return this.cost;
	}
}
