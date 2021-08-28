package cc.igxnon.square.pet.commands;

import cn.nukkit.Server;

/**
 * @author iGxnon
 * @date 2021/08/27
 */
@SuppressWarnings("unused")
public class CommandRegistrar {

    public static final String PREFIX = "SquarePet";

    public static void init() {
        Server.getInstance().getCommandMap().register(PREFIX, new AddPetCommand("addpet"));
        Server.getInstance().getCommandMap().register(PREFIX, new DelPetCommand("delpet"));
        Server.getInstance().getCommandMap().register(PREFIX, new AddSkillCommand("addskill"));
        Server.getInstance().getCommandMap().register(PREFIX, new DelSkillCommand("delskill"));
        Server.getInstance().getCommandMap().register(PREFIX, new AddPropCommand("addprop"));
        Server.getInstance().getCommandMap().register(PREFIX, new DelPropCommand("delprop"));

    }

}
