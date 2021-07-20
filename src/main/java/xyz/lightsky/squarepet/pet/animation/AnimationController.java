package xyz.lightsky.squarepet.pet.animation;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import xyz.lightsky.squarepet.pet.BaseSquarePet;
import xyz.lightsky.squarepet.pet.animation.protocol.AnimatePetPacket;

import java.net.ServerSocket;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnimationController {

    public static void sendAnimate(String animateType, BaseSquarePet... pet) {
        AnimatePetPacket pk = new AnimatePetPacket();
        pk.setAnimation(animateType);
        pk.setEntityRuntimeIds(Stream.of(pet).map(Entity::getId).collect(Collectors.toList()));
        Server.getInstance().getOnlinePlayers().values().forEach(s-> s.dataPacket(pk));
    }


}
