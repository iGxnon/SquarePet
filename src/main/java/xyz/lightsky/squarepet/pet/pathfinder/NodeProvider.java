package xyz.lightsky.squarepet.pet.pathfinder;

import java.util.List;

public interface NodeProvider {

    boolean isReachable();

    void update();

    List<NodeProvider> getNextNodes();

}
