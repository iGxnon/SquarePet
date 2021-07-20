package xyz.lightsky.squarepet.listner;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.PlayerInputPacket;
import xyz.lightsky.squarepet.manager.TrainerManager;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.trainer.Trainer;

public class TrainerHandlePetListener implements Listener {

    /**
     * 骑乘控制系统
     */
    @EventHandler
    public void onHandle(DataPacketReceiveEvent event) {
        Trainer trainer = TrainerManager.getTrainer(event.getPlayer().getName());
        /*
         * 跳跃dismount
         */
        if(event.getPacket() instanceof PlayerInputPacket) {
            if(((PlayerInputPacket) event.getPacket()).jumping) {
                BaseSquarePet pet = null;
                if((pet = trainer.getOnRide()) != null) {
                    pet.dismountEntity(trainer.getPlayer());
                }
            }
        }

        /*
         * 潜行dismount
         */
        if(event.getPacket() instanceof PlayerActionPacket) {
            if(((PlayerActionPacket) event.getPacket()).action == 11) {
                BaseSquarePet pet = null;
                if((pet = trainer.getOnRide()) != null) {
                    pet.dismountEntity(trainer.getPlayer());
                }
            }
        }
    }

    /**
     * 喂食系统
     */
    @EventHandler
    public void onFeed(DataPacketReceiveEvent event) {
        Trainer trainer = TrainerManager.getTrainer(event.getPlayer().getName());
        if(event.getPacket() instanceof InventoryTransactionPacket) {
            InventoryTransactionPacket pk = (InventoryTransactionPacket) event.getPacket();
            if(pk.transactionType == 3) {
                UseItemOnEntityData data = (UseItemOnEntityData) pk.transactionData;
                long rid = data.entityRuntimeId;
                Item itemInHand = data.itemInHand;
                int actionType = data.actionType;
                Entity entity = event.getPlayer().getLevel().getEntity(rid);
                if(itemInHand.isTool()) return;
                if(itemInHand.isArmor()) return;
                if(actionType == 0) {
                    if(entity instanceof BaseSquarePet && trainer.hasSpawnedPet(((BaseSquarePet) entity).getType())) {
                        if(((BaseSquarePet) entity).feed(itemInHand)) {
                            itemInHand.setCount(itemInHand.getCount() - 1);
                            trainer.getPlayer().getInventory().setItemInHand(itemInHand);
                        }
                    }
                }
            }
        }
    }

    /**
     * 目标锁定判定
     */
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
