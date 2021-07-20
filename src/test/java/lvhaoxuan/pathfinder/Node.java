package lvhaoxuan.pathfinder;

import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.Particle;
import xyz.lightsky.squarepet.pet.pathfinder.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public Location location;
    public double g;
    public double h;
    public Node parent;
    public Block block;

    public Node(Location loc, double g, double h, Node parent) {
        this.location = loc;
        this.g = g;
        this.h = h;
        this.parent = parent;
        this.block = loc.getLevelBlock();
    }

    //显示节点
    public void show(Particle type) {
        location.getLevel().addParticle(type);
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
    public List<Node> getNextNodes(Location target) {
        List<Node> nodes = new ArrayList<>();
        for (int[] direct : Util.directions) {
            Location nextStep = location.clone().add(direct[0], direct[1], direct[2]);
            Node m = new Node(nextStep, g + getAddG(direct), BlockUtil.getDistance(nextStep, target), this);
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
                return 1.4142135623731;
            case 3:
                return 1.7320508075689;
            default:
                return 0;
        }
    }

    //是否可到达 todo height self updated
    private boolean isAccesse(int[] direct, Node n, Location nextStep) {
        if (!(BlockUtil.isPassable(nextStep.getLevelBlock()) && (BlockUtil.isPassable(nextStep.getLevel().getBlock(nextStep.add(0, 1, 0))) || BlockUtil.isPassable(nextStep.getLevel().getBlock(nextStep.add(0, -1, 0)))))) {
            return false;
            // 往上一格
        } else if (direct[1] == 1 && !BlockUtil.isUpable(n.block)) {
            return false;
            // 与父节点同平面的八个方向 如果当前节点下方不可站就返回false
        } else if ((direct[0] != 0 || direct[2] != 0) && direct[1] == 0 && !BlockUtil.isStandable(n.location.getLevel().getBlock(n.location.add(0, -1, 0)))) {
            return false;
            // 减枝掉向下对角线移动
        } else if ((direct[0] != 0 || direct[2] != 0) && direct[1] == -1) {
            return false;
        } else if(direct[0] != 0 && direct[2] != 0 && BlockUtil.isPassable(nextStep.getLevel().getBlock((int)(n.location.x + direct[0]), 0, (int)(n.location.z + direct[2])))) {
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
