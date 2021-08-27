package cc.igxnon.square.pet.events.trainer;

import cc.igxnon.square.pet.trainer.Trainer;

/**
 * @author iGxnon
 * @date 2021/08/27
 */
public abstract class TrainerEvent {

    protected Trainer trainer;

    public Trainer getTrainer() {
        return trainer;
    }
}
