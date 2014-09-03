package crehop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import utils.BlockUtils;


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
	
	public BlockQueue(Chunk copyChunk, Chunk newChunk, int sizeModifier){
		this.sizeModifier = sizeModifier;
		this.copyChunk = copyChunk;
		this.newChunk = newChunk;
		xCopyChunk = this.copyChunk.getX();
		zCopyChunk = this.copyChunk.getZ();
		yCopyChunk = 0;
		xNewChunk = this.newChunk.getX();
		zNewChunk = this.newChunk.getZ();
		yNewChunk = 0;
		for(int x = xCopyChunk; x > x+15+sizeModifier;x++){
			XCopyChunk.add(x);
			for(int z = zCopyChunk; z > z+15+sizeModifier; z++){
				ZCopyChunk.add(z);
				for(int y = yCopyChunk; y > 128; y++){
					YCopyChunk.add(y);
				}
			}
		}	
		xNewChunk = this.newChunk.getX();
		zNewChunk = this.newChunk.getZ();
		yNewChunk = 0;
		for(int x = xNewChunk; x > x + 15 + sizeModifier;x++){
			XNewChunk.add(x);
			for(int z = zNewChunk; z > z + 15 + sizeModifier; z++){
				ZNewChunk.add(z);
				for(int y = yNewChunk; y > 128; y++){
					YNewChunk.add(y);
				}
			}
		}
		Main.activeQueues.add(this);
	}
	public void tickBuilder(){
		Block toBeCopied = null;
		Block toBePasted = null;
		if(XCopyChunk.size() > 0 && ZCopyChunk.size() > 0){
			toBeCopied = copyChunk.getWorld().getBlockAt(XCopyChunk.get(XCopyChunk.size() - 1),YCopyChunk.get(YCopyChunk.size() - 1),ZCopyChunk.get(ZCopyChunk.size() - 1));
			if(YCopyChunk.size() > 0){
				YCopyChunk.remove(YCopyChunk.size() - 1);
			}
			if(YCopyChunk.size() == 0){
				XCopyChunk.remove(XCopyChunk.size() - 1);
				ZCopyChunk.remove(ZCopyChunk.size() - 1);
				for(int y = yCopyChunk; y > 128; y++){
					YCopyChunk.add(y);
				}
			}
			toBePasted = newChunk.getWorld().getBlockAt(XNewChunk.get(XNewChunk.size() - 1),YNewChunk.get(YNewChunk.size() - 1),ZNewChunk.get(ZNewChunk.size() - 1));
			if(YNewChunk.size() > 0){
				YNewChunk.remove(YNewChunk.size() - 1);
			}
			if(YNewChunk.size() == 0){
				XNewChunk.remove(XNewChunk.size() - 1);
				ZNewChunk.remove(ZNewChunk.size() - 1);
				for(int y = yNewChunk; y > 128; y++){
					YNewChunk.add(y);
				}
			}
			if(toBeCopied != null && toBePasted != null){
				utils.BlockUtils.build(toBeCopied, toBePasted);
			}
		}
		else{
			Main.activeQueues.remove(this);
		}

	}
}