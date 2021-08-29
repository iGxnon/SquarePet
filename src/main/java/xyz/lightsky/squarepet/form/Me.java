package xyz.lightsky.squarepet.form;

import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.language.Lang;
import xyz.lightsky.squarepet.prop.BaseProp;
import xyz.lightsky.squarepet.prop.symbol.TrainerAcceptable;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.List;

public class Me {

    public static void ME_INFO(Trainer trainer) {
        FormModal form = new FormModal(trainer.getName(),trainer.getInfo(),
                Lang.translate("%ui.me.info.openbag%"),
                Lang.translate("%ui.me.back%"));
        trainer.getPlayer().showFormWindow(form.onResponse(s->{
            if(s) {
                ME_BAG(trainer);
            }else {
                Menu.MAIN(trainer.getPlayer());
            }
        }));
    }

    public static void ME_BAG(Trainer trainer) {
        List<String> contains = trainer.getBag().getContainNames();
        FormSimple form = new FormSimple(Lang.translate("%ui.me.bag.title%").replace("{trainer}", trainer.getName()), "");
        contains.forEach(form::addButton);
        trainer.getPlayer().showFormWindow(form.onClick(s->{
            String select = contains.get(s);
            int id = BaseProp.getID(select);
            BaseProp prop = BaseProp.getProp(id);
            int count = trainer.getBag().getContains().get(id);
            assert prop != null;
            PROP_INFO(prop, trainer, count);
        }));
    }

    public static void PROP_INFO(BaseProp prop, Trainer trainer, int count) {
        String info = Lang.translate("%ui.me.bag.prop.info%")
                .replace("{count}", String.valueOf(count))
                .replace("{description}", prop.getInfo())
                .replace("{propName}", prop.getName())
                .replace("{n}", "\n");
        FormModal form = new FormModal(prop.getName(),
                info,
                Lang.translate("%ui.me.bag.prop.use%"),
                Lang.translate("%ui.me.back%"));
        trainer.getPlayer().showFormWindow(form.onResponse(s -> {
            if(s) {
                Menu.CONFIRM(trainer.getPlayer(), bool->{
                    if(bool) {
                        if(prop instanceof TrainerAcceptable) {
                            trainer.usePropToSelf(prop.getId());
                        }else {
                            Pet.PET_LIST(trainer, type-> trainer.usePropToPet(prop.getId(), type), Lang.translate("%ui.me.bag.prop.choosepettouse%"));
                        }
                    }else {
                        PROP_INFO(prop, trainer, count);
                    }
                }, "", Lang.translate("%ui.me.bag.prop.use.confirm%").replace("{propName}", prop.getName()));
            } else {
                ME_BAG(trainer);
            }
        }));
    }

}
