package crehop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import utils.SaveLoad;

public class BuildPlace {
	private int xMin;
	private int xMax;
	private int zMin;
	private int zMax;
	private int yMin;
	private int yMax;
	private int multiplier;
	boolean forSale = false;
	String name;
	World world;
	Location chunkLocation;
	Chunk chunk;
	String owner;
	int cost;
	Sign sign;
	private boolean lockout;
	
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
				Block block = world.getBlockAt(x, Main.yCheckHeight ,z);
				if(x == xMin || x == xMax || z == zMin || z == zMax){
					block = world.getBlockAt(x, Main.yCheckHeight ,z);
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
		SaveLoad.storeData("StoredLocations.txt");
	}
	public void destroy(){
		for(int x = xMax; x >= xMin; x--){
			for(int z = zMax; z >= zMin; z--){
				for(int y = yMax; y >= yMin - 3; y--){
					Block block = world.getBlockAt(x, y ,z);
					if(y <= 3){
						block.setType(Material.GRASS);
					}else if(y == 0){
						block.setType(Material.BEDROCK);
					}else if (y > 3){
						block.setType(Material.AIR);
					}
				}
			}			
		}
		Main.buildPlaces.remove(this.getID());
		Main.placesCheck.remove(this);
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

	public boolean isLockedout() {
		return lockout;
	}

	public void setLockout(boolean lockout) {
		this.lockout = lockout;
	}
	public void unlock() {
		this.setLockout(false);
		for(int x = xMax; x >= xMin; x--){
			for(int z = zMax; z >= zMin; z--){
				for(int y = yMax; y >= yMin -3; y--){
					Block block = world.getBlockAt(x, y ,z);
					if(block.getType() == Material.SIGN_POST || block.getType() == Material.WOOL){
					}
					else if(y <= 3 && x == xMax || y <= 3 && x == xMin || y <= 3 && z == zMax || y <= 3 && z == zMin){
						block.setType(Material.GRASS);
					}else if(y == 0){
						block.setType(Material.BEDROCK);
					}else if(x == xMax || x == xMin || z == zMax || z == zMin || y == yMax){
						block.setType(Material.AIR);
					}
				}
			}			
		}
	}
	public void lock() {
		this.setLockout(true);
		for(int x = xMax; x >= xMin; x--){
			for(int z = zMax; z >= zMin; z--){
				for(int y = yMax; y >= yMin -3; y--){
					Block block = world.getBlockAt(x, y ,z);
					if(block.getType() == Material.SIGN_POST || block.getType() == Material.WOOL){
					}
					else if(y <= 3 && x == xMax || y <= 3 && x == xMin || y <= 3 && z == zMax || y <= 3 && z == zMin){
						block.setType(Material.GRASS);
					}else if(y == 0){
						block.setType(Material.BEDROCK);
					}else if(x == xMax || x == xMin || z == zMax || z == zMin || y == yMax){
						block.setType(Material.BARRIER);
					}
				}
			}			
		}
	}

	public int getxMin() {
		return xMin;
	}

	public void setxMin(int xMin) {
		this.xMin = xMin;
	}

	public int getxMax() {
		return xMax;
	}

	public void setxMax(int xMax) {
		this.xMax = xMax;
	}

	public int getzMin() {
		return zMin;
	}

	public void setzMin(int zMin) {
		this.zMin = zMin;
	}

	public int getzMax() {
		return zMax;
	}

	public void setzMax(int zMax) {
		this.zMax = zMax;
	}

	public int getyMin() {
		return yMin;
	}

	public void setyMin(int yMin) {
		this.yMin = yMin;
	}

	public int getyMax() {
		return yMax;
	}

	public void setyMax(int yMax) {
		this.yMax = yMax;
	} 
	
}
