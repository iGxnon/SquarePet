package xyz.lightsky.squarepet.form;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.utils.TextFormat;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.manager.ConfigManager;
import xyz.lightsky.squarepet.manager.TrainerManager;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.function.Consumer;

public class Menu {

    public static void MAIN(Player player) {
        FormSimple form = new FormSimple(TextFormat.BLUE + "方块宠物", TextFormat.GREEN + "请执行您的操作");
        form.addButton(new ElementButton("我的宠物", new ElementButtonImageData("path", "textures/items/villagebell.png")));
        form.addButton(new ElementButton("宠物阵容", new ElementButtonImageData("path", "textures/items/diamond_sword.png")));
        form.addButton(new ElementButton("赠送宠物", new ElementButtonImageData("path", "textures/ui/gift_square.png")));
        form.addButton(new ElementButton("编辑宠物", new ElementButtonImageData("path", "textures/ui/anvil_icon.png")));
        form.addButton(new ElementButton("市场", new ElementButtonImageData("path", "textures/ui/icon_hangar.png")));
        form.addButton(new ElementButton("我", new ElementButtonImageData("path", "textures/ui/icon_panda.png")));
        if (ConfigManager.isOP(player)) {
            form.addButton(new ElementButton("OP系统", new ElementButtonImageData("path", "textures/ui/icon_sign.png")));
        }

        Trainer trainer = TrainerManager.getTrainer(player.getName());
        player.showFormWindow(form.onClick((id) -> {
            if (id != -1) {
                switch(id) {
                    case 0:
                        Pet.PET_LIST(trainer, s -> Pet.PET_INFO(s, trainer), "");
                        break;
                    case 1:
                        Pet.PET_LINEUP(trainer.getLineup(), trainer);
                        break;
                    case 2:
                        Pet.PET_LIST(trainer, s -> Pet.PET_GIVE(trainer, s), "请选择要赠送的宠物");
                        break;
                    case 3:
                        Pet.PET_LIST(trainer, s -> Pet.PET_EDIT(trainer, s), "请选择要编辑的宠物");
                        break;
                    case 4:
                        Market.SWITCH(trainer);
                        break;
                    case 5:
                        Me.ME_INFO(trainer);
                        break;
                    case 6:
                        OP.OP_SETTING(player);
                        break;
                    default:
                        break;
                }
            }
        }));
    }


    public static void CONFIRM(Player player, Consumer<Boolean> consumer) {
        CONFIRM(player, consumer, "确认界面", "确定要这么做吗?");
    }

    public static void CONFIRM(Player player, Consumer<Boolean> consumer, String title, String content) {
        CONFIRM(player, consumer, title, content, "确认", "取消");
    }

    public static void CONFIRM(Player player, Consumer<Boolean> consumer, String title, String content, String trueButton, String falseButton) {
        FormModal form = new FormModal(title, content, trueButton, falseButton);
        player.showFormWindow(form.onResponse(consumer));
    }

}
