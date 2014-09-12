package crehop;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockQueue {
	Chunk newChunk;
	Chunk copyChunk;
	boolean active;
	int sizeMultiplier;
	ArrayList<Integer> XCopyChunk = new ArrayList<Integer>();
	ArrayList<Integer> ZCopyChunk = new ArrayList<Integer>();
	ArrayList<Integer> YCopyChunk = new ArrayList<Integer>();
	int xCopyChunk;
	int yCopyChunk;
	int zCopyChunk;
	ArrayList<Integer> XNewChunk = new ArrayList<Integer>();
	ArrayList<Integer> ZNewChunk = new ArrayList<Integer>();
	ArrayList<Integer> YNewChunk = new ArrayList<Integer>();
	int xNewChunk;
	int yNewChunk;
	int zNewChunk;
	int ZCopySize;
	int XCopySize;
	int ZNewSize;
	int XNewSize;
	ArrayList<Material> PlaceLast = new ArrayList<Material>();
	ArrayList<Block> LastBlocksCopied = new ArrayList<Block>();
	ArrayList<Block> LastBlocksPasted = new ArrayList<Block>();
	
	public BlockQueue(Chunk copyChunk, Chunk newChunk, int sizeMultiplier, int yLevelPasteLocation){
		if(sizeMultiplier == 0){
			sizeMultiplier = 1;
		}
		this.sizeMultiplier = sizeMultiplier;
		this.copyChunk = copyChunk;
		this.newChunk = newChunk;
		xCopyChunk = (int) this.copyChunk.getBlock(0, 1, 0).getLocation().getX();
		zCopyChunk = (int) this.copyChunk.getBlock(0, 1, 0).getLocation().getZ();
		yCopyChunk = 0;
		xNewChunk = (int) this.newChunk.getBlock(0, 1, 0).getLocation().getX();
		zNewChunk = (int) this.newChunk.getBlock(0, 1, 0).getLocation().getZ();
		yNewChunk = yLevelPasteLocation;
		
		Bukkit.broadcastMessage("COPYCHUNK X = " +  xCopyChunk + " Z = " + zCopyChunk);
		Bukkit.broadcastMessage("NEWCHUNK X = " +  xNewChunk + " Z = " + zNewChunk);
		this.resetCopyChunkX();
		this.resetCopyChunkY();
		this.resetCopyChunkZ();
		this.resetNewChunkX();
		this.resetNewChunkY();
		this.resetNewChunkZ();
		this.startUp();
	   ZCopySize = this.ZCopyChunk.size();
		XCopySize = this.XCopyChunk.size();
		ZNewSize = this.ZNewChunk.size();
		XNewSize = this.XNewChunk.size();
		Bukkit.broadcastMessage(this.getTotalBlocksInQueue() + " TOTAL BLOCKS ADDED");
		Main.activeQueues.add(this);
	}
	private void startUp() {
		PlaceLast.add(Material.REDSTONE_TORCH_OFF);
		PlaceLast.add(Material.REDSTONE_TORCH_ON);
		PlaceLast.add(Material.WATER);
		PlaceLast.add(Material.LAVA);
		PlaceLast.add(Material.SIGN);
		PlaceLast.add(Material.ITEM_FRAME);
		PlaceLast.add(Material.PAINTING);
		PlaceLast.add(Material.LEVER);
		PlaceLast.add(Material.STONE_BUTTON);
		PlaceLast.add(Material.WOOD_BUTTON);
		PlaceLast.add(Material.LADDER);
		PlaceLast.add(Material.VINE);
		PlaceLast.add(Material.SKULL_ITEM);
		PlaceLast.add(Material.SKULL);
		PlaceLast.add(Material.TORCH);
		PlaceLast.add(Material.TRAP_DOOR);
		PlaceLast.add(Material.TRIPWIRE_HOOK);
		PlaceLast.add(Material.COCOA);
		PlaceLast.add(Material.STATIONARY_LAVA);
		PlaceLast.add(Material.STATIONARY_WATER);
		PlaceLast.add(Material.SAND);
		PlaceLast.add(Material.GRAVEL);
	}
	public void tickBuilder(){
		if(Main.debug){
		}
		Block toBeCopied = null;
		Block toBePasted = null;
		int maxCount = 1000;
		if(XCopyChunk.size() > 0 && ZCopyChunk.size() > 0 && YCopyChunk.size() > 0)
		{
			for(int count = 0; count <= maxCount; count ++){
				if(XCopyChunk.size() > 0 && ZCopyChunk.size() > 0 && YCopyChunk.size() > 0)
				{
					toBeCopied = copyChunk.getWorld().getBlockAt(XCopyChunk.get(0),YCopyChunk.get(0),ZCopyChunk.get(0));
					//Bukkit.broadcastMessage("COPYBLOCK LOCATION = X " + XCopyChunk.get(0) + ",Y " + YCopyChunk.get(0) + ",Z " + ZCopyChunk.get(0));
					if(this.YCopyChunk.size() > 0)
					{
						if(ZCopyChunk.size() > 1)
						{
							this.ZCopyChunk.remove(0);
						}
						else if(ZCopyChunk.size() == 1 && XCopyChunk.size() != 1)
						{
							this.XCopyChunk.remove(0);
							this.resetCopyChunkZ();
						}
						else if(ZCopyChunk.size() == 1 && XCopyChunk.size() == 1)
						{
							this.YCopyChunk.remove(0);
							this.resetCopyChunkZ();
							this.resetCopyChunkX();
						}
					}
					else{
						this.ZCopyChunk.clear();
						this.XCopyChunk.clear();
					}
					toBePasted = newChunk.getWorld().getBlockAt(XNewChunk.get(0),YNewChunk.get(0),ZNewChunk.get(0));
					//Bukkit.broadcastMessage("NEWBLOCK LOCATION = X " + XNewChunk.get(0) + ",Y " + YNewChunk.get(0) + ",Z " + ZNewChunk.get(0));
					if(this.YNewChunk.size() > 0){
						if(ZNewChunk.size() > 1){
							this.ZNewChunk.remove(0);
						}
						else if(ZNewChunk.size() == 1 && XNewChunk.size() != 1){
							this.XNewChunk.remove(0);
							this.resetNewChunkZ();
						}
						else if(ZNewChunk.size() == 1 && XNewChunk.size() == 1){
							this.YNewChunk.remove(0);
							this.resetNewChunkZ();
							this.resetNewChunkX();
						}
					}
					else{
						this.ZNewChunk.clear();
						this.XNewChunk.clear();
					}
					if(toBeCopied != null && toBePasted != null){
						if(this.isMorphic(toBeCopied)){
							this.LastBlocksCopied.add(toBeCopied);
							this.LastBlocksPasted.add(toBePasted);
							return;
						}
						else{
							if(toBePasted.getType() == Material.CHEST || toBePasted.getType() == Material.SIGN || toBePasted.getType() == Material.SIGN_POST){
								return;
							}
							else{
								utils.BlockUtils.build(toBeCopied, toBePasted);
							}
						}
					}
				}
			}
		}
		else{
			for(int count = 0; count <= maxCount; count ++){
				if(this.LastBlocksCopied.isEmpty() == false){
					utils.BlockUtils.build(this.LastBlocksCopied.get(0), this.LastBlocksPasted.get(0));
					this.LastBlocksCopied.remove(0);
					this.LastBlocksPasted.remove(0);
				}
				else{
					Main.activeQueues.remove(this);
					if(Main.debug){
						Bukkit.broadcastMessage("QUEUE CLOSED!");
					}
					break;
				}
			}
		}

	}
	
	//COPYCHUNK RESETERS
	public void resetCopyChunkX(){
		int xCopySize = (xCopyChunk + (16 * sizeMultiplier));
		for(int x = xCopyChunk; x < xCopySize ;x++){
			XCopyChunk.add(x);
		}
	}
	public void resetCopyChunkZ(){
		int zCopySize = (zCopyChunk + (16 * sizeMultiplier));
		for(int z = zCopyChunk; z < zCopySize; z++){
			ZCopyChunk.add(z);
		}
	}
	public void resetCopyChunkY(){
		for(int y = yCopyChunk; y < yCopyChunk + (12 * sizeMultiplier); y++){
			YCopyChunk.add(y);
		}
	}
	//PASTECHUNK RESETERS
	public void resetNewChunkX(){
		int xNewSize = (xNewChunk + (16 * sizeMultiplier));
		for(int x = xNewChunk; x < xNewSize ;x++){
			XNewChunk.add(x);
		}
	}
	
	public void resetNewChunkZ(){
		int zNewSize = (zNewChunk + (16 * sizeMultiplier));
		for(int z = zNewChunk; z < zNewSize; z++){
			ZNewChunk.add(z);
		}
	}
	
	public void resetNewChunkY(){
		for(int y = yNewChunk; y < yNewChunk + (12 * sizeMultiplier); y++){
			YNewChunk.add(y);
		}
	}
	
	public int getTotalBlocksInQueue() {
		return ((16 * sizeMultiplier) * this.XCopyChunk.size() * this.ZCopyChunk.size());
	}
	
	public int getMultiplier(){
		return this.sizeMultiplier;
	}
	public boolean isMorphic(Block Block){
		if(PlaceLast.contains(Block.getType())){
			return true;
		}
		return false;
	}
}