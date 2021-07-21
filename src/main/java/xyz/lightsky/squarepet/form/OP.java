package xyz.lightsky.squarepet.form;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementDropdown;
import xyz.lightsky.squarepet.form.api.window.FormCustom;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;

import java.util.List;
import java.util.stream.Collectors;

public class OP {

    public static void OP_SETTING(Player player) {
        FormModal form = new FormModal("OP设置", "", "编辑玩家", "编辑市场");
        form.onResponse(s -> {
            if(s) {
                OP_PLAYER_EDIT(player);
            }else {
                OP_MARKET_EDIT(player);
            }
        });
    }

    public static void OP_PLAYER_EDIT(Player player) {
        List<String> players = Server.getInstance().getOnlinePlayers().values()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        FormCustom form = new FormCustom();
        form.addElement(new ElementDropdown("选择编辑的玩家", players));
        player.showFormWindow(form.onResponse(s->{
            String target = s.getDropdownResponse(0).getElementContent();
            OP_PLAYER_EDIT_SWITCH(player, target);
        }));
    }

    public static void OP_PLAYER_EDIT_SWITCH(Player op, String target) {
        FormSimple form = new FormSimple(target, "");
        form.addButton("添加宠物");
        form.addButton("删除宠物");
        form.addButton("添加道具");
        form.addButton("添加技能");
        form.addButton("修改称号");
        form.addButton("修改等级");
    }

    public static void OP_PLAYER_EDIT_ADD_PET() {

    }

    public static void OP_PLAYER_EDIT_REMOVE_PET() {

    }

    public static void OP_PLAYER_EDIT_ADD_PROP() {

    }

    public static void OP_PLAYER_EDIT_ADD_SKILL() {

    }

    public static void OP_MARKET_EDIT(Player player) {
        FormSimple form = new FormSimple("选择需要编辑的市场", "");
        form.addButton("宠物市场");
        form.addButton("技能石市场");
        form.addButton("基础道具市场");
        form.addButton("返回");
        player.showFormWindow(form.onClick(s->{
            switch (s) {
                case 0:
                    OP_MARKET_PET_EDIT(player);
                    break;
                case 1:
                    OP_MARKET_SKILL_STONE_EDIT(player);
                    break;
                case 2:
                    OP_MARKET_BASE_PROP_EDIT(player);
                    break;
                case 3:
                    OP_SETTING(player);
                    break;
                default:
                    break;
            }
        }));
    }

    public static void OP_MARKET_PET_EDIT(Player player) {

    }

    public static void OP_MARKET_SKILL_STONE_EDIT(Player player) {

    }

    public static void OP_MARKET_BASE_PROP_EDIT(Player player) {

    }

}
