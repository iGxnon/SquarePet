package lvhaoxuan.pathfinder;


import cn.nukkit.block.Block;
import cn.nukkit.level.Location;

public class BlockUtil {

    //获取预测距离
    public static double getDistance(Location begin, Location target) {
        int x1 = (int) begin.getX();
        int y1 = (int) begin.getY();
        int z1 = (int) begin.getZ();
        int x2 = (int) target.getX();
        int y2 = (int) target.getY();
        int z2 = (int) target.getZ();
        return Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2);
    }

    //是否可站立
    public static boolean isStandable(Block block) {
        return !isPassable(block)
                || block.getId() == Block.VINE
                || block.getId() == Block.LADDER;
    }

    //是否可通过
    public static boolean isPassable(Block block) {
        return !block.isSolid()
                || block.getId() == Block.DOUBLE_PLANT
                || block.getId() == Block.SAPLING
                || block.getId() == Block.DEAD_BUSH
                || block.getId() == Block.BROWN_MUSHROOM
                || block.getId() == Block.RED_MUSHROOM
                || block.getId() == Block.TORCH
                || block.getId() == Block.LADDER
                || block.getId() == Block.SNOW
                || block.getId() == Block.VINE
                || block.getId() == Block.END_ROD
                || block.getId() == Block.LEVER
                || block.getId() == Block.STONE_BUTTON
                || block.getId() == Block.TRIPWIRE_HOOK
                || block.getId() == Block.REDSTONE_BLOCK
                || block.getId() == Block.POWERED_RAIL
                || block.getId() == Block.DETECTOR_RAIL
                || block.getId() == Block.ACTIVATOR_RAIL;
    }

    //是否可向上
    public static boolean isUpable(Block block) {
        return isStandable(block.getLevel().getBlock(block.add(0, -1, 0)))
                || block.getId() == Block.VINE
                || block.getId() == Block.LADDER;
    }
}
