package xyz.lightsky.squarepet.pet.pathfinder;

import cn.nukkit.level.Location;
import xyz.lightsky.squarepet.pet.pathfinder.astar.Node;

import java.util.List;

public interface NodeProvider {

    boolean isAccesse(int[] direct, Node n, Location nextStep);

    void update(Node node);

    List<Node> getNextNodes(Location target);

}
