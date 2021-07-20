package xyz.lightsky.squarepet.pet.pathfinder.astar;

import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.Vector3;
import xyz.lightsky.squarepet.pet.pathfinder.API;
import xyz.lightsky.squarepet.pet.pathfinder.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lvhaoxuan
 * @author iGxnon
 */
public class AstarPathfinder implements API {

    private final HashMap<Block, Node> open;
    private final HashSet<Node> close;
    private final Position begin;
    private final Position target;
    private final long timeLimit;
    private Node result;

    // 1 tick time limit
    public AstarPathfinder(Position begin, Position target) {
        this(begin, target, 50);
    }

    public AstarPathfinder(Position begin, Position target, long timeLimit) {
        this.begin = begin;
        this.target = target;
        this.timeLimit = timeLimit;
        this.open = new HashMap<>();
        this.close = new HashSet<>();
    }

    //寻路
    public Vector3 find() {
        long timeStart = System.currentTimeMillis();
        Node start = new Node(begin, 0, Util.MHDistance(begin, target), null);
        open.put(start.block, start);
        while (!open.isEmpty()) {
            Node n = getMinFNode();
            if (n.equals(target)) {
                result = n;
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
                    }
                }
            }
        }
        if (result != null) {
            if(result().size() == 1) return null;
            return result().get(1).location;
        }
        return null;
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


    //获取f值最小的节点
    private Node getMinFNode() {
        double minF = Double.MAX_VALUE;
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
