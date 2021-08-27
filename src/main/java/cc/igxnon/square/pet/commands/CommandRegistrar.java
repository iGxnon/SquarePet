package cc.igxnon.square.pet.commands;

import cn.nukkit.Server;

/**
 * @author iGxnon
 * @date 2021/08/27
 */
@SuppressWarnings("unused")
public class CommandRegistrar {

    public static void init() {
        Server.getInstance().getCommandMap().register("squarepet", new AddPetCommand("addpet"));
    }

}
