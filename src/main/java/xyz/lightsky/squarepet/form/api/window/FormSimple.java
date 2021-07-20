package xyz.lightsky.squarepet.form.api.window;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import xyz.lightsky.squarepet.form.api.WindowManager;

import java.util.List;
import java.util.function.Consumer;

public class FormSimple extends FormWindowSimple {

    public FormSimple() {
        super("", "");
    }

    public FormSimple(String title, String content) {
        super(title, content);
    }

    public FormSimple(String title, String content, List<ElementButton> buttons) {
        super(title, content, buttons);
    }

    public void addButton(String text) {
        super.addButton(new ElementButton(text));
    }

    public void addButton(String text, ElementButtonImageData data) {
        super.addButton(new ElementButton(text, data));
    }

    public FormSimple onClick(Consumer<Integer> consumer) {
        WindowManager.windowsSimple.put(this, consumer);
        return this;
    }

}
