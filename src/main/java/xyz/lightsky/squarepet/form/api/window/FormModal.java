package xyz.lightsky.squarepet.form.api.window;

import cn.nukkit.form.window.FormWindowModal;
import xyz.lightsky.squarepet.form.api.WindowManager;

import java.util.function.Consumer;

public class FormModal extends FormWindowModal {

    public FormModal(String title, String content, String trueButtonText, String falseButtonText) {
        super(title, content, trueButtonText, falseButtonText);
    }

    public FormModal onResponse(Consumer<Boolean> consumer) {
        WindowManager.windowsModal.put(this, consumer);
        return this;
    }

}
