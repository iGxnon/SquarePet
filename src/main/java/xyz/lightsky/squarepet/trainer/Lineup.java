package xyz.lightsky.squarepet.trainer;

import lombok.Getter;
import xyz.lightsky.squarepet.manager.PetManager;
import xyz.lightsky.squarepet.pet.Attribute;

import java.util.*;

@Getter
public class Lineup {

    private String land;
    private String swim;
    private String fly;

    private final Trainer trainer;

    private float scaleAdd;
    private double defenceRateAdd;
    private int attackAdd;
    private double critTimeRateAdd;
    private double critRateAdd;

    public Lineup(Trainer trainer, String... pets) {
        this.trainer = trainer;
        switch (pets.length) {
            case 0:
            default:
                break;
            case 1:
                add(pets[0]);
                break;
            case 2:
                add(pets[0]);
                add(pets[1]);
                break;
            case 3:
                add(pets[0]);
                add(pets[1]);
                add(pets[2]);
                break;
        }
        registerLineupAdd();
    }

    public void spawnAll() {
        getTrainer().spawnPet(land);
        getTrainer().spawnPet(swim);
        getTrainer().spawnPet(fly);
    }

    public String getInfo() {
        return "阵容加成: " + "\n"
                + "大小: +" + scaleAdd + "\n"
                + "防御: +" + defenceRateAdd + "\n"
                + "攻击: +" + attackAdd + "\n"
                + "暴击率: +" + critRateAdd + "\n"
                + "暴击倍率: +" + critTimeRateAdd;
    }

    public void registerLineupAdd() {
        List<String> landAdd = PetManager.getLineupAdd(land);
        List<String> swimAdd = PetManager.getLineupAdd(swim);
        List<String> flyAdd = PetManager.getLineupAdd(fly);
        List<String> add = new ArrayList<>(flyAdd);
        add.addAll(swimAdd);
        add.addAll(landAdd);
        add.forEach(s->{
            String key = s.split("-")[0];
            double value = Double.parseDouble(s.split("-")[1]);
            switch (key) {
                case "加防御":
                    defenceRateAdd = Math.max(defenceRateAdd, value);
                    break;
                case "加攻击":
                    attackAdd = (int) Math.max(attackAdd, value);
                    break;
                case "加暴击率":
                    critRateAdd = Math.max(critRateAdd, value);
                    break;
                case "加暴击倍率":
                    critTimeRateAdd = Math.max(critTimeRateAdd, value);
                    break;
                case  "加大小":
                    scaleAdd = (float) Math.max(scaleAdd, value);
                    break;
            }
        });
    }

    public Lineup(List<String> pets, Trainer trainer) {
        this.trainer = trainer;
        pets.forEach(this::add);
    }

    public boolean contains(String type) {
        return Objects.equals(land, type) || Objects.equals(swim, type) || Objects.equals(fly, type);
    }

    public void add(String type) {
        if(type == null) return;
        switch (PetManager.getAttribute(type)) {
            case FLY:
                fly = type;
                break;
            case SWIM:
                swim = type;
                break;
            case LAND:
                land = type;
                break;
        }
    }

    public void set(Attribute attribute, String type) {
        if(type == null) return;
        switch (attribute) {
            case FLY:
                fly = type;
                break;
            case SWIM:
                swim = type;
                break;
            case LAND:
                land = type;
                break;
        }
        update();
    }

    public void remove(Attribute attribute) {
        switch (attribute) {
            case SWIM:
                swim = null;
                break;
            case FLY:
                fly = null;
                break;
            case LAND:
                land = null;
                break;
        }
        update();
    }

    public void update() {
        registerLineupAdd();
    }

    public void save() {
        trainer.getCfg().set("宠物阵容", toArray());
    }

    public List<String> toArray() {
        ArrayList<String> list = new ArrayList<>();;
        if(land != null) list.add(land);
        if(swim != null) list.add(swim);
        if(fly != null) list.add(fly);
        return list;
    }

    @Override
    public String toString() {
        return "Lineup{" + toArray().toString() +
                "}";
    }

}
