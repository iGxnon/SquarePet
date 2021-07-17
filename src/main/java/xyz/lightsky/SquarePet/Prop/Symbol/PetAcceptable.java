package xyz.lightsky.SquarePet.Prop.Symbol;

import xyz.lightsky.SquarePet.Trainer.Trainer;

public interface PetAcceptable {

    boolean onUseToPet(Trainer trainer, String petType);

}
