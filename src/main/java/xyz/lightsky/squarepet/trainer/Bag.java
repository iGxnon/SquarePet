package xyz.lightsky.squarepet.trainer;

import cn.nukkit.utils.Config;
import lombok.Getter;
import lombok.Setter;
import xyz.lightsky.squarepet.prop.BaseProp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
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

    public List<String> getContainNames() {
        return contains.keySet().stream()
                .map(s-> Objects.requireNonNull(BaseProp.getProp(s)).getName())
                .collect(Collectors.toList());
    }

    public void put(int propID, int value) {
        contains.merge(propID, value, Integer::sum);
    }

    public void remove(int id, int amount) {
        if(amount > contains.get(id)) {
            return;
        }
        contains.put(id, contains.get(id) - amount);
        if(contains.get(id) == 0) {
            contains.remove(id);
        }
    }

    public void save() {
        contains.forEach((k, v) -> config.set(String.valueOf(k), v));
        config.save();
    }

    public boolean contains(int id) {
        return contains.containsKey(id);
    }

    /** propName should not be 'SkillStone'*/
    public void use(String propName) {
        use(BaseProp.getID(propName));
    }

    /** propName should not be 'SkillStone'*/
    public void use(String propName, String type) {
        use(BaseProp.getID(propName), type);
    }

    public void use(int propId) {
        if(contains(propId)) {
            BaseProp prop = BaseProp.getProp(propId);
            assert prop != null;
            if (prop.work(getOwner())) {
                remove(propId, 1);
            }
        }else {
            getOwner().sendMessage("你没有该道具!");
        }
    }

    public void use(int propId, String type) {
        if(contains(propId)){
            BaseProp prop = BaseProp.getProp(propId);
            assert prop != null;
            if(prop.work(getOwner(), type)) {
                remove(propId, 1);
            }
        }else {
            getOwner().sendMessage("你没有该道具!");
        }
    }

    @Override
    public String toString() {
        return "Bag{" +
                "contains=" + contains +
                '}';
    }
}
