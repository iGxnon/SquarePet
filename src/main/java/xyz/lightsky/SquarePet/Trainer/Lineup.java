package xyz.lightsky.SquarePet.Trainer;

import xyz.lightsky.SquarePet.Pet.Attribute;

import java.util.List;
import java.util.Objects;

public class Lineup {

    private String land;
    private String swim;
    private String fly;

    private Trainer trainer;

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
    }

    public Lineup(List<String> pets, Trainer trainer) {
        this.trainer = trainer;
        pets.forEach(this::add);
    }

    public boolean contains(String type) {
        return Objects.equals(land, type) || Objects.equals(swim, type) || Objects.equals(fly, type);
    }

    public void add(String type) {
        switch (trainer.getPetMap().get(type).getAttribute()) {
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
    }

    public void update() {

    }

}
