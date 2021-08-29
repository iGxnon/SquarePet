package cc.igxnon.square.pet.trainer;

import cc.igxnon.square.pet.pets.PetDescription;
import cn.nukkit.Player;

/**
 * @author iGxnon
 * @date 2021/08/27
 */
public class Trainer implements ITrainer {

    @Override
    public boolean spawnPet(String identifier) {
        return false;
    }

    @Override
    public boolean closePet(String identifier) {
        return false;
    }

    @Override
    public TrainerDescription getDescription() {
        return null;
    }

    @Override
    public int getTrainerLv() {
        return 0;
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public int getTrainerExp() {
        return 0;
    }

    @Override
    public PetDescription getPetDescription(String identifier) {
        return null;
    }
}
