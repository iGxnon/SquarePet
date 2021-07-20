package xyz.lightsky.squarepet.form.api.window;

import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowCustom;
import xyz.lightsky.squarepet.form.api.WindowManager;

import java.util.List;
import java.util.function.Consumer;

public class FormCustom extends FormWindowCustom {

    public FormCustom() {
        super("");
    }

    public FormCustom(String title) {
        super(title);
    }

    public FormCustom(String title, List<Element> contents) {
        super(title, contents);
    }

    public FormCustom(String title, List<Element> contents, String icon) {
        super(title, contents, icon);
    }

    public FormCustom(String title, List<Element> contents, ElementButtonImageData icon) {
        super(title, contents, icon);
    }


    public FormCustom onResponse(Consumer<FormResponseCustom> custom) {
        WindowManager.windowsCustom.put(this, custom);
        return this;
    }

}
