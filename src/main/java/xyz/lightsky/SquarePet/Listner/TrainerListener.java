package xyz.lightsky.SquarePet.Listner;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import xyz.lightsky.SquarePet.Manager.TrainerManager;
import xyz.lightsky.SquarePet.Trainer.Trainer;

public class TrainerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Trainer trainer = new Trainer(event.getPlayer());
        TrainerManager.trainerMap.put(event.getPlayer().getName(), trainer);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TrainerManager.trainerMap.get(event.getPlayer().getName()).close();
        TrainerManager.trainerMap.remove(event.getPlayer().getName());
    }

}
