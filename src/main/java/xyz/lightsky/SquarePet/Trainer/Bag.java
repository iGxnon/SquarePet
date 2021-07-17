package xyz.lightsky.SquarePet.Trainer;

import cn.nukkit.utils.Config;
import lombok.Getter;
import xyz.lightsky.SquarePet.Prop.BaseProp;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Bag {

    private Trainer owner;
    private Map<Integer, Integer> contains = new HashMap<>();
    private Config config;

    public Bag(Trainer owner, Config config) {
        this.owner = owner;
        config.getAll().forEach((k, v) -> {
            int id = Integer.parseInt(k);
            int amount = Integer.parseInt(v.toString());
            put(id, amount);
        });
        this.config = config;
    }

    public void put(int propID, int value) {
        contains.merge(propID, value, Integer::sum);
    }

    public boolean remove(int id, int amount) {
        if(amount > contains.get(id)) {
            return false;
        }
        contains.put(id, contains.get(id) - amount);
        if(contains.get(id) == 0) {
            contains.remove(id);
        }
        return true;
    }

    public void save() {
        contains.forEach((k, v) -> config.set(String.valueOf(k), v));
        config.save();
    }

    public boolean contains(int id) {
        return contains.containsKey(id);
    }

    public void use(int propId) {
        BaseProp prop = BaseProp.getProp(propId);
        if(prop.work(getOwner())) {
            remove(propId, 1);
        }
    }

    public void use(int propId, String type) {
        BaseProp prop = BaseProp.getProp(propId);
        if(prop.work(getOwner(), type)) {
            remove(propId, 1);
        }
    }


}
