package xyz.lightsky.SquarePet.Listner;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.PlayerInputPacket;
import xyz.lightsky.SquarePet.Manager.TrainerManager;
import xyz.lightsky.SquarePet.Pet.BaseSquarePet;
import xyz.lightsky.SquarePet.Trainer.Trainer;

public class TrainerHandlePetListener implements Listener {

    @EventHandler
    public void onHandle(DataPacketReceiveEvent event) {
        if(event.getPacket() instanceof PlayerInputPacket) {
            if(((PlayerInputPacket) event.getPacket()).jumping) {
                Trainer trainer = TrainerManager.getTrainer(event.getPlayer().getName());
                BaseSquarePet pet = null;
                if((pet = trainer.getOnRide()) != null) {
                    pet.dismountEntity(trainer.getPlayer());
                }
            }
        }
    }

}
