package xyz.lightsky.squarepet.listner;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import xyz.lightsky.squarepet.manager.TrainerManager;
import xyz.lightsky.squarepet.trainer.Trainer;

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
