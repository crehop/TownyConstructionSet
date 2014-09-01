package Default;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class Utils {
	
	
	public static List<Block> getChunkBlocks(Chunk copyChunk, BlockQueue queue) {
        int X = copyChunk.getChunkSnapshot().getX() * 16;
        int Z = copyChunk.getChunkSnapshot().getZ() * 16;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y++) {
                    queue.add(copyChunk.getWorld().getBlockAt(X+x, y, Z+z));
                }
            }
        }
    }
}
