package xyz.lightsky.squarepet.prop.symbol;

import xyz.lightsky.squarepet.trainer.Trainer;

public interface PetAcceptable {

    boolean onUseToPet(Trainer trainer, String petType);

}
