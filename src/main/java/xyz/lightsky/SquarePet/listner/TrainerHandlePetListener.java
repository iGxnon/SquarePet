package xyz.lightsky.SquarePet.listner;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.PlayerInputPacket;
import xyz.lightsky.SquarePet.manager.TrainerManager;
import xyz.lightsky.SquarePet.pet.BaseSquarePet;
import xyz.lightsky.SquarePet.trainer.Trainer;

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

    @EventHandler
    public void onLockTarget(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
                Trainer trainer = TrainerManager.getTrainer(((EntityDamageByEntityEvent) event).getDamager().getName());
                trainer.onDamage(event.getEntity());
            }

            if(event.getEntity() instanceof Player) {
                Trainer trainer = TrainerManager.getTrainer(event.getEntity().getName());
                trainer.onAttack((EntityDamageByEntityEvent) event);
            }

        }
    }

}
