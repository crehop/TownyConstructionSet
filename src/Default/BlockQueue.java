package Default;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class BlockQueue {
	Chunk newChunk;
	Chunk copyChunk;
	int x;
	int y;
	int z;
	
	public BlockQueue(Chunk copyChunk, Chunk newChunk){
		this.copyChunk = copyChunk;
		this.newChunk = newChunk;
		x = this.copyChunk.getX();
		z = this.copyChunk.getZ();
		y = 0;
	}
	
	public boolean builder(){
		//cody to lay blocks and incriment x and y then z until full chunk built. returns true if built false if done
		return false;
	}
}