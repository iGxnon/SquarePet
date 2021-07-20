package xyz.lightsky.squarepet.pet.pathfinder;

import cn.nukkit.math.Vector3;

import java.util.List;

public interface API {

    /**
     * return the next node of path
     * @return Vector3
     */
    Vector3 find();

}
