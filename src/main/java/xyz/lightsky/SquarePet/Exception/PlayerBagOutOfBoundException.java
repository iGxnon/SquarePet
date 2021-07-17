package xyz.lightsky.SquarePet.Exception;

import xyz.lightsky.SquarePet.Trainer.Trainer;

public class PlayerBagOutOfBoundException extends Exception {

    public PlayerBagOutOfBoundException(Trainer trainer) {
        super(trainer.getName() + "`s bag out of bound, please check this one's bag config");
    }

}
