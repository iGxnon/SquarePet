package xyz.lightsky.squarepet.pet.pathfinder.astar;

import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import xyz.lightsky.squarepet.pet.pathfinder.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvhaoxuan
 * @author iGxnon
 */
public class Node {

    public Position location;
    public double g;
    public double h;
    public Node parent;
    public Block block;

    public Node(Position loc, double g, double h, Node parent) {
        this.location = loc;
        this.g = g;
        this.h = h;
        this.parent = parent;
        this.block = loc.getLevelBlock();
    }


    //判断从起点到达是否更近，并更新节点
    public void update(Node node) {
        if (this.g > node.g) {
            this.location = node.location;
            this.g = node.g;
            this.h = node.h;
            this.parent = node.parent;
            this.block = location.getLevelBlock();
        }
    }

    //获取节点f值
    public double f() {
        return g + h;
    }

    //获取子节点
    public List<Node> getNextNodes(Position target) {
        List<Node> nodes = new ArrayList<>();
        for (int[] direct : Util.directions) {
            Position nextStep = location.add(direct[0], direct[1], direct[2]);
            Node m = new Node(nextStep, g + getAddG(direct), Util.MHDistance(nextStep, target), this);
            if (isAccesse(direct, this, nextStep)) {
                nodes.add(m);
            }
        }
        return nodes;
    }

    //获取移动的耗费g
    private double getAddG(int[] direct) {
        int count = 0;
        for (int val : direct) {
            if (val != 0) {
                count++;
            }
        }
        switch (count) {
            case 1:
                return 1;
            case 2:
                return 1.4142D;
            case 3:
                return 1.7320D;
            default:
                return 0;
        }
    }

    //是否可到达
    public boolean isAccesse(int[] direct, Node n, Position nextStep) {
        // todo 优化
        if (!(Util.isPermeable(nextStep.getLevelBlock())
                && (Util.isPermeable(Util.getNearBlock(nextStep.getLevelBlock(), BlockFace.UP)) || Util.isPermeable(Util.getNearBlock(nextStep.getLevelBlock(), BlockFace.DOWN))
        ))) {
            return false;
        } else if (direct[1] == 1 && !Util.checkUpAccessed(n.block)) {
            return false;
        } else if ((direct[0] != 0 || direct[2] != 0) && direct[1] == 0 && !Util.canStand(Util.getNearBlock(n.block, BlockFace.DOWN))) {
            return false;
        } else if ((direct[0] != 0 || direct[2] != 0) && direct[1] == -1) {
            return false;
        }
        return true;
//            //对顶角格挡
//        }else if(direct[0] != 0 && direct[2] != 0 && direct[1] != 0) {
//            return Util.isPermeable(n.location.getLevel().getBlock(n.location.add(direct[0], direct[1]))) && Util.isPermeable(n.location.getLevel().getBlock(n.location.add(0, direct[1], direct[2])));
//            //对角线格挡
//        } else{
//            return direct[0] == 0 || direct[2] == 0 || Util.isPermeable(n.location.getLevel().getBlock(n.location.add(direct[0]))) || Util.isPermeable(n.location.getLevel().getBlock(n.location.add(0, 0, direct[2])));
//        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node node = (Node) obj;
            return node.block.equals(block);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.block.hashCode();
    }

    public boolean equals(Position loc) {
        return loc.getLevelBlock().equals(block);
    }
}
