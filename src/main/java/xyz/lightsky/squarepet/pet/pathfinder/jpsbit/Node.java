package xyz.lightsky.squarepet.pet.pathfinder.jpsbit;

import cn.nukkit.block.Block;
import cn.nukkit.level.Position;

//todo jps Node
public class Node {

    public Position position;
    public double g;
    public double h;
    public Node parent;
    public Block block;

    public Node(Position pos, double g, double h, Node parent) {
        this.position = pos;
        this.g = g;
        this.h = h;
        this.parent = parent;
        this.block = pos.getLevelBlock();
    }

    /**
     * 判断并更新已经在openlist中的本节点
     * @param node
     */
    public void update(Node node) {
        if (this.g > node.g) {
            this.position = node.position;
            this.g = node.g;
            this.h = node.h;
            this.parent = node.parent;
            this.block = position.getLevelBlock();
        }
    }

    public boolean equals(Position loc) {
        return loc.getLevelBlock().equals(block);
    }

}
