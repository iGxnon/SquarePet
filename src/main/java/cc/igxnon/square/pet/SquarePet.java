package cc.igxnon.square.pet;

import cc.igxnon.square.pet.commands.CommandRegistrar;
import cn.nukkit.plugin.PluginBase;

/**
 * @author iGxnon
 * @date 2021/08/27
 * SquarePet 2.0.0
 */
public class SquarePet extends PluginBase {

    private static SquarePet instance;

    public static SquarePet getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        CommandRegistrar.init();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
