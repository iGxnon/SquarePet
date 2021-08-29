package xyz.lightsky.squarepet.form;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.manager.ConfigManager;
import xyz.lightsky.squarepet.manager.TrainerManager;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.function.Consumer;

public class Menu {

    public static void MAIN(Player player) {
        FormSimple form = new FormSimple(Lang.translate("%ui.menu.main.title%"),
                Lang.translate("%ui.menu.main.content%"));

        form.addButton(new ElementButton(Lang.translate("%ui.menu.main.mypet%"), new ElementButtonImageData(Lang.translate("%ui.menu.main.mypet.icon%").split(":")[0], Lang.translate("%ui.menu.main.mypet.icon%").split(":")[1])));
        form.addButton(new ElementButton(Lang.translate("%ui.menu.main.lineup%"), new ElementButtonImageData(Lang.translate("%ui.menu.main.lineup.icon%").split(":")[0], Lang.translate("%ui.menu.main.lineup.icon%").split(":")[1])));
        form.addButton(new ElementButton(Lang.translate("%ui.menu.main.givepet%"), new ElementButtonImageData(Lang.translate("%ui.menu.main.givepet.icon%").split(":")[0], Lang.translate("%ui.menu.main.givepet.icon%").split(":")[1])));
        form.addButton(new ElementButton(Lang.translate("%ui.menu.main.editpet%"), new ElementButtonImageData(Lang.translate("%ui.menu.main.editpet.icon%").split(":")[0], Lang.translate("%ui.menu.main.editpet.icon%").split(":")[1])));
        form.addButton(new ElementButton(Lang.translate("%ui.menu.main.market%"), new ElementButtonImageData(Lang.translate("%ui.menu.main.market.icon%").split(":")[0], Lang.translate("%ui.menu.main.market.icon%").split(":")[1])));
        form.addButton(new ElementButton(Lang.translate("%ui.menu.main.me%"), new ElementButtonImageData(Lang.translate("%ui.menu.main.me.icon%").split(":")[0], Lang.translate("%ui.menu.main.me.icon%").split(":")[1])));
        if (ConfigManager.isOP(player)) {
            form.addButton(new ElementButton(Lang.translate("%ui.menu.main.op%"), new ElementButtonImageData(Lang.translate("%ui.menu.main.op.icon%").split(":")[0], Lang.translate("%ui.menu.main.op.icon%").split(":")[1])));
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
                        Pet.PET_LIST(trainer, s -> Pet.PET_GIVE(trainer, s), Lang.translate("%ui.menu.main.petchoose.give%"));
                        break;
                    case 3:
                        Pet.PET_LIST(trainer, s -> Pet.PET_EDIT(trainer, s), Lang.translate("%ui.menu.main.petchoose.edit%"));
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
        CONFIRM(player, consumer, "", Lang.translate("%ui.menu.confirm.content%"));
    }

    public static void CONFIRM(Player player, Consumer<Boolean> consumer, String title, String content) {
        CONFIRM(player, consumer, title, content, Lang.translate("%ui.menu.confirm.true%"), Lang.translate("%ui.menu.confirm.false%"));
    }

    public static void CONFIRM(Player player, Consumer<Boolean> consumer, String title, String content, String trueButton, String falseButton) {
        FormModal form = new FormModal(title, content, trueButton, falseButton);
        player.showFormWindow(form.onResponse(consumer));
    }

}
