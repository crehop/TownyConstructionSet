package crehop;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class BuildPlace {
	int xMin;
	int xMax;
	int zMin;
	int zMax;
	int yMin;
	int yMax;
	int multiplier;
	boolean forSale = false;
	String name;
	World world;
	Location chunkLocation;
	Chunk chunk;
	String owner;
	int cost;
	Sign sign;
	
	public BuildPlace(Location chunkLocation, int multiplier, String name, String owner, boolean load, int cost){
		if(multiplier < 1){
			this.multiplier = 1;
		}
		else{
			this.multiplier = multiplier;
		}
		this.chunkLocation = chunkLocation;
		this.owner = owner;
		this.chunk = chunkLocation.getChunk();
		xMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockX();
		xMax = xMin + (16 * this.multiplier) - 1;
		zMin = chunk.getBlock(0, Main.yCheckHeight, 0).getLocation().getBlockZ();
		zMax = zMin + (16 * this.multiplier) - 1;
		yMin = 3;
		yMax = yMin + ((16 * this.multiplier) + Main.yCheckHeight);
		this.cost = cost;
		this.world = chunk.getWorld();
		this.name = name;
		this.multiplier = multiplier;
		if(!load){
			this.buildOutline();
		}
		this.sign = this.getSign();
		this.setSetupSign();
		Main.buildPlaces.put(this.name, this);
		Main.placesCheck.add(this);
	}

	private void setSetupSign() {
		setSignLine(0,ChatColor.GREEN + this.name);
		setSignLine(1,ChatColor.RED + "" + this.cost);
		//setSignLine(2,this.forSale);
		setSignLine(3,this.owner);
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

	public String getOwner() {
		return this.owner;
	}

	public String getID() {
		return name;
	}
	public Sign getSign(){
		if(this.getSignLocation().getBlock() instanceof Sign){
			return (Sign)this.getSignLocation().getBlock().getState();
		}else{
			Block block = this.getSignLocation().getBlock();
			block.setType(Material.SIGN_POST);
			return (Sign)this.getSignLocation().getBlock().getState();
		}
	}
	public void setSignLine(int line, String string){
		Sign sign = getSign();
		sign.setLine(line, string);
		sign.update();
	}
}
