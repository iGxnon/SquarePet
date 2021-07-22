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
import cn.nukkit.math.Vector2f;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.PlayerInputPacket;
import cn.nukkit.potion.Effect;
import xyz.lightsky.squarepet.manager.TrainerManager;
import xyz.lightsky.squarepet.pet.Attribute;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.trainer.Trainer;

public class TrainerHandlePetListener implements Listener {

    /**
     * 骑乘控制系统
     */
    @EventHandler
    public void onHandle(DataPacketReceiveEvent event) {
        Trainer trainer = TrainerManager.getTrainer(event.getPlayer().getName());

        if(event.getPacket() instanceof PlayerInputPacket) {
            /*
             * 跳跃dismount
             */
            PlayerInputPacket pk = (PlayerInputPacket) event.getPacket();
            BaseSquarePet pet = trainer.getOnRide();
            if(pk.jumping) {
                if(pet != null) {
                    pet.dismountEntity(trainer.getPlayer());
                }
            }
            /*
             * 移动 放技能
             */
            if(pet != null) {
                // 水肺药水
                if(pet.getAttribute().equals(Attribute.SWIM)) {
                    pet.getPassenger().addEffect(Effect.getEffect(Effect.WATER_BREATHING));
                }
                if(pk.motionY > 0) {
                    pet.yaw = trainer.getPlayer().yaw;
                    if(pet.getAttribute().equals(Attribute.LAND)) {
                        pet.pitch = 0;
                        //提前把转向移动包发送,降低操作延迟
                        pet.getLevel().addEntityMovement(pet, pet.x, pet.y, pet.z, pet.yaw, pet.pitch, pet.yaw);
                        Vector3f motion = pet.checkJump(new Vector2f((float) pet.getDirectionVector().x, (float) pet.getDirectionVector().z));
                        pet.move(motion.x * 0.5, motion.y, motion.z * 0.5);
                    }else {
                        pet.pitch = trainer.getPlayer().pitch;
                        //提前把转向移动包发送,降低操作延迟
                        pet.getLevel().addEntityMovement(pet, pet.x, pet.y, pet.z, pet.yaw, pet.pitch, pet.yaw);
                        pet.move(pet.getDirectionVector().x * 0.5, pet.getDirectionVector().y * 0.5, pet.getDirectionVector().z * 0.5);
                    }
                    pet.updateMovement();
                }
                /*
                 * 放技能
                 */
                if(pk.motionY < 0) {
                    // 三技能
                    if(pet.isCanSkill()) {
                        if(pet.getSkills()[2] != null) {
                            pet.skill(pet.getSkills()[2]);
                        }else {
                            // sendTip 防止重复发送信息(反正也看不出来是重复发送的tip)
                            trainer.getPlayer().sendTip(pet.getName()+": 该技能槽为空");
                        }
                    }
                }else if(pk.motionX > 0) {
                    // 一技能
                    if(pet.isCanSkill()) {
                        if(pet.getSkills()[0] != null) {
                            pet.skill(pet.getSkills()[0]);
                        }else {
                            trainer.getPlayer().sendTip(pet.getName()+": 该技能槽为空");
                        }
                    }
                }else if(pk.motionX < 0) {
                    // 二技能
                    if(pet.isCanSkill()) {
                        if(pet.getSkills()[1] != null) {
                            pet.skill(pet.getSkills()[1]);
                        }else {
                            trainer.getPlayer().sendTip(pet.getName()+": 该技能槽为空");
                        }
                    }
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
