package xyz.lightsky.squarepet.pet.pathfinder.astar;

import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.Particle;
import xyz.lightsky.squarepet.pet.pathfinder.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public Location loc;
    public double g;
    public double h;
    public Node parent;
    public Block block;

    public Node(Location loc, double g, double h, Node parent) {
        this.loc = loc;
        this.g = g;
        this.h = h;
        this.parent = parent;
        this.block = loc.getLevelBlock();
    }

    //显示节点
    public void show(Particle type) {
        loc.getLevel().addParticle(type);
    }

    //判断从起点到达是否更近，并更新节点
    public void update(Node node) {
        if (this.g > node.g) {
            this.loc = node.loc;
            this.g = node.g;
            this.h = node.h;
            this.parent = node.parent;
            this.block = loc.getLevelBlock();
        }
    }

    //获取节点f值
    public double f() {
        return g + h;
    }

    //获取子节点
    public List<Node> getNextNodes(Location target) {
        List<Node> nodes = new ArrayList<>();
        for (int[] direct : Util.directions) {
            Location nextStep = loc.add(direct[0], direct[1], direct[2]);
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

    //是否可到达 todo ?
    private boolean isAccesse(int[] direct, Node n, Location nextStep) {
        if (!(Util.isPermeable(nextStep.getLevelBlock()) && (Util.isPermeable(nextStep.getLevel().getBlock(nextStep.add(0, 1, 0))) || Util.isPermeable(nextStep.getLevel().getBlock(nextStep.add(0, -1, 0)))))) {
            return false;
        } else if (direct[1] == 1 && !Util.checkUpAccessed(n.block)) {
            return false;
        } else if ((direct[0] != 0 || direct[2] != 0) && direct[1] == 0 && !Util.checkUpAccessed(n.loc.getLevel().getBlock(n.loc.add(0, -1, 0)))) {
            return false;
        } else if ((direct[0] != 0 || direct[2] != 0) && direct[1] == -1) {
            return false;
        }
        return true;
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

    public boolean equals(Location loc) {
        return loc.getLevelBlock().equals(block);
    }
}
