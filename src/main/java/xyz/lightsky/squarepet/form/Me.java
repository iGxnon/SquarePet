package xyz.lightsky.squarepet.form;

import com.google.gson.internal.$Gson$Types;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;
import xyz.lightsky.squarepet.prop.BaseProp;
import xyz.lightsky.squarepet.prop.symbol.TrainerAcceptable;
import xyz.lightsky.squarepet.trainer.Bag;
import xyz.lightsky.squarepet.trainer.Trainer;

import java.util.List;

//todo Me ui system
public class Me {

    public static void ME_INFO(Trainer trainer) {
        FormModal form = new FormModal(trainer.getName(),trainer.getInfo(), "打开背包", "返回");
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
        FormSimple form = new FormSimple(trainer.getName() + " 的背包", "");
        contains.forEach(form::addButton);
        trainer.getPlayer().showFormWindow(form.onClick(s->{
            String select = contains.get(s);
            int id = BaseProp.getID(select);
            BaseProp prop = BaseProp.getProp(id);
            int count = trainer.getBag().getContains().get(id);
            PROP_INFO(prop, trainer, count);
        }));
    }

    public static void PROP_INFO(BaseProp prop, Trainer trainer, int count) {
        FormModal form = new FormModal(prop.getName(), "数量: " + count + " 个\n" + prop.getInfo(), "使用", "返回");
        trainer.getPlayer().showFormWindow(form.onResponse(s->{
            if(s) {
                Menu.CONFIRM(trainer.getPlayer(), bool->{
                    if(bool) {
                        if(prop instanceof TrainerAcceptable) {
                            trainer.usePropToSelf(prop.getId());
                        }else {
                            Pet.PET_LIST(trainer, type-> trainer.usePropToPet(prop.getId(), type), "请选择宠物使用道具");
                        }
                    }else {
                        PROP_INFO(prop, trainer, count);
                    }
                }, "", "确认要使用道具 " + prop.getName() + " 吗?");
            }else {
                ME_BAG(trainer);
            }
        }));
    }

}
