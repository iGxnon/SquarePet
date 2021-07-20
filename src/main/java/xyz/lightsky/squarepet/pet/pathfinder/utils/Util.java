package xyz.lightsky.squarepet.pet.pathfinder.utils;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockID;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

/**
 * @author lvhaoxuan
 * @author iGxnon
 */
public class Util {

    /**
     * 除自身外26个方向
     */
    public final static int[][] directions = {
            {-1, 0, 0},
            {1, 0, 0},
            {0, -1, 0},
            {0, 1, 0},
            {-1, -1, 0},
            {1, -1, 0},
            {-1, 1, 0},
            {1, 1, 0},
            {0, 0, 1},
            {-1, 0, 1},
            {1, 0, 1},
            {0, -1, 1},
            {0, 1, 1},
            {-1, -1, 1},
            {1, -1, 1},
            {-1, 1, 1},
            {1, 1, 1},
            {0, 0, -1},
            {-1, 0, -1},
            {1, 0, -1},
            {0, -1, -1},
            {0, 1, -1},
            {-1, -1, -1},
            {1, -1, -1},
            {-1, 1, -1},
            {1, 1, -1}
    };


    /**
     * 获取曼哈顿距离
     * @param start
     * @param target
     * @return
     */
    public static double MHDistance(Vector3 start, Vector3 target) {
        return Math.abs(start.x - target.x) + Math.abs(start.y - target.y) + Math.abs(start.z - target.z);
    }

    /**
     * 是否可透过 todo add more
     * @return
     */
    public static boolean isPermeable(Block block) {
        return !block.isSolid()
                || block.getId() == Block.END_ROD
                || block.getId() == Block.DOUBLE_PLANT
                || block.getId() == Block.VINE
                || block.getId() == Block.BROWN_MUSHROOM
                || block.getId() == Block.TORCH
                || block.getId() == Block.LADDER
                || block.getId() == Block.SNOW
                || block.getId() == Block.TRIPWIRE_HOOK
                || block.getId() == Block.DEAD_BUSH
                || block.getId() == Block.FLOWER
                || block.getId() == Block.LEVER
                || block.getId() == Block.STONE_BUTTON
                || block.getId() == Block.POWERED_RAIL
                || block.getId() == Block.RED_MUSHROOM
                || block.getId() == Block.REDSTONE_BLOCK
                || block.getId() == Block.DETECTOR_RAIL
                || block.getId() == Block.SAPLING
                || block.getId() == Block.SNOW_LAYER
                || block.getId() == Block.ACTIVATOR_RAIL;
    }

    /**
     * 是否可以立足
     * @param block
     * @return
     */
    public static boolean canStand(Block block) {
        return !isPermeable(block)
                || block.getId() == Block.VINE
                || block.getId() == Block.LADDER;
    }


    /**
     * 是否可以垂直上升 [爬梯子,脚手架]
     * @param block
     * @return
     */
    public static boolean checkUpAccessed(Block block) {
        return canStand(block.getLevel().getBlock(block.add(0, -1, 0)))
                || block.getId() == Block.VINE
                || block.getId() == Block.LADDER;
    }

    public static Block getNearBlock(Block block, BlockFace face) {
        switch (face) {
            case UP:
                return block.getLevel().getBlock(block.add(0, 1, 1));
            case DOWN:
                return block.getLevel().getBlock(block.add(0, -1, 0));
            case EAST:
                return block.getLevel().getBlock(block.add(1, 0, 0));
            case WEST:
                return block.getLevel().getBlock(block.add(-1, 0, 0));
            case NORTH:
                return block.getLevel().getBlock(block.add(0, 0, -1));
            case SOUTH:
                return block.getLevel().getBlock(block.add(0, 0, 1));
            default:
                return new BlockAir();
        }
    }



}
