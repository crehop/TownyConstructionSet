package crehop;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class BlockQueue {
	Chunk newChunk;
	Chunk copyChunk;
	boolean active;
	int sizeModifier;
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

	
	public BlockQueue(Chunk copyChunk, Chunk newChunk, int sizeModifier){
		this.sizeModifier = sizeModifier;
		this.copyChunk = copyChunk;
		this.newChunk = newChunk;
		xCopyChunk = (int) this.copyChunk.getBlock(0, 1, 0).getLocation().getX();
		zCopyChunk = (int) this.copyChunk.getBlock(0, 1, 0).getLocation().getZ();
		yCopyChunk = 0;
		xNewChunk = (int) this.newChunk.getBlock(0, 1, 0).getLocation().getX();
		zNewChunk = (int) this.newChunk.getBlock(0, 1, 0).getLocation().getZ();
		yNewChunk = 0;
		
		Bukkit.broadcastMessage("COPYCHUNK X = " +  xCopyChunk + " Z = " + zCopyChunk);
		Bukkit.broadcastMessage("NEWCHUNK X = " +  xNewChunk + " Z = " + zNewChunk);
		this.resetCopyChunkX();
		this.resetCopyChunkY();
		this.resetCopyChunkZ();
		this.resetNewChunkX();
		this.resetNewChunkY();
		this.resetNewChunkZ();
	   ZCopySize = this.ZCopyChunk.size();
		XCopySize = this.XCopyChunk.size();
		ZNewSize = this.ZNewChunk.size();
		XNewSize = this.XNewChunk.size();
		Bukkit.broadcastMessage(this.getTotalBlocksInQueue() + " TOTAL BLOCKS ADDED");
		Main.activeQueues.add(this);
	}
	public void tickBuilder(){
		if(Main.debug){
			Bukkit.broadcastMessage("QUEUE TICK ATTEMPED");
		}
		Block toBeCopied = null;
		Block toBePasted = null;
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
				utils.BlockUtils.build(toBeCopied, toBePasted);
			}
		}
		else{
			Main.activeQueues.remove(this);
			if(Main.debug){
				Bukkit.broadcastMessage("QUEUE CLOSED!");
			}
		}

	}
	
	//COPYCHUNK RESETERS
	public void resetCopyChunkX(){
		int xCopySize = (xCopyChunk + 16 + sizeModifier);
		for(int x = xCopyChunk; x < xCopySize ;x++){
			XCopyChunk.add(x);
		}
	}
	public void resetCopyChunkZ(){
		int zCopySize = (zCopyChunk + 16 + sizeModifier);
		for(int z = zCopyChunk; z < zCopySize; z++){
			ZCopyChunk.add(z);
		}
	}
	public void resetCopyChunkY(){
		for(int y = yCopyChunk; y < 256; y++){
			YCopyChunk.add(y);
		}
	}
	//PASTECHUNK RESETERS
	public void resetNewChunkX(){
		int xNewSize = (xNewChunk + 16 + sizeModifier);
		for(int x = xNewChunk; x < xNewSize ;x++){
			XNewChunk.add(x);
		}
	}
	
	public void resetNewChunkZ(){
		int zNewSize = (zNewChunk + 16 + sizeModifier);
		for(int z = zNewChunk; z < zNewSize; z++){
			ZNewChunk.add(z);
		}
	}
	
	public void resetNewChunkY(){
		for(int y = yNewChunk; y < 256; y++){
			YNewChunk.add(y);
		}
	}
	
	public int getTotalBlocksInQueue() {
		return (128 * this.XCopyChunk.size() * this.ZCopyChunk.size());
	}
}