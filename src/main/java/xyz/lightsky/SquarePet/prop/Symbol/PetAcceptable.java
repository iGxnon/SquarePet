package xyz.lightsky.SquarePet.prop.Symbol;

import xyz.lightsky.SquarePet.trainer.Trainer;

public interface PetAcceptable {

    boolean onUseToPet(Trainer trainer, String petType);

}
