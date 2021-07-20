package lvhaoxuan.pathfinder;

import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.level.particle.FloatingTextParticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class AStarPathfinder {

    private final HashMap<Block, Node> open;
    private final HashSet<Node> close;
    private final Location begin;
    private final Location target;
    private final long timeLimit;
    private Node result;
    private long timeStart;

    public AStarPathfinder(Location begin, Location target, long timeLimit) {
        this.begin = begin;
        this.target = target;
        this.timeLimit = timeLimit;
        this.open = new HashMap<>();
        this.close = new HashSet<>();
    }

    //寻路
    public void find() {
        timeStart = System.currentTimeMillis();
        Node start = new Node(begin, 0, BlockUtil.getDistance(begin, target), null);
        open.put(start.block, start);
        while (!open.isEmpty()) {
            Node n = getMinFNode();
            if (n.equals(target)) {
                result = n;
                System.out.println("AStar V1.0");
                System.out.println("直线距离：" + begin.distance(target));
                System.out.println("寻路距离：" + n.g);
                System.out.println("花费时间：" + (System.currentTimeMillis() - timeStart) + "ms");
                break;
            } else {
                open.remove(n.block);
                close.add(n);
                for (Node m : n.getNextNodes(target)) {
                    if (!close.contains(m)) {
                        if (System.currentTimeMillis() - timeStart > timeLimit) {
                            break;
                        } else {
                            if (open.containsKey(m.block)) {
                                open.get(m.block).update(m);
                            } else {
                                open.put(m.block, m);
                            }
                        }
                        m.show(new DestroyBlockParticle(m.block, Block.get(Block.REDSTONE_BLOCK)));
                    }
                }
            }
        }
        if (result != null) {
            show(result);
        }
    }

    //获取结果
    public List<Node> result() {
        List<Node> ret = new ArrayList<>();
        Node node = result;
        while (node != null) {
            ret.add(0, node);
            node = node.parent;
        }
        return ret;
    }

    //展示路径
    private void show(Node node) {
        while (node != null) {
            node.show(new FloatingTextParticle(node.location, "a"));
            node = node.parent;
        }
    }

    //获取f值最小的节点
    private Node getMinFNode() {
        Double minF = Double.MAX_VALUE;
        Node ret = null;
        for (Node node : open.values()) {
            if (node.f() < minF) {
                ret = node;
                minF = node.f();
            }
        }
        return ret;
    }
}
