package xyz.lightsky.squarepet.form.api;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import xyz.lightsky.squarepet.form.api.window.FormCustom;
import xyz.lightsky.squarepet.form.api.window.FormModal;
import xyz.lightsky.squarepet.form.api.window.FormSimple;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WindowManager implements Listener {

    public static final Map<FormSimple, Consumer<Integer>> windowsSimple = new HashMap<>();
    public static final Map<FormModal, Consumer<Boolean>> windowsModal = new HashMap<>();
    public static final Map<FormCustom, Consumer<FormResponseCustom>> windowsCustom = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onResponse(PlayerFormRespondedEvent event) {

        FormWindow window = event.getWindow();

        if(window instanceof FormSimple) {
            FormResponseSimple responseSimple = (FormResponseSimple) event.getResponse();
            if(responseSimple == null) return;
            int id = responseSimple.getClickedButtonId();
            Consumer<Integer> intConsumer = windowsSimple.get(window);
            if(intConsumer != null) {
                intConsumer.accept(id);
                windowsSimple.remove(window);
            }
        }

        if(window instanceof FormCustom) {
            FormResponseCustom responseCustom = (FormResponseCustom) event.getResponse();
            if(responseCustom == null) return;
            Consumer<FormResponseCustom> customConsumer = windowsCustom.get(window);
            if(customConsumer != null) {
                customConsumer.accept(responseCustom);
                windowsCustom.remove(window);
            }
        }

        if(window instanceof FormModal) {
            FormResponseModal responseModal = (FormResponseModal) event.getResponse();
            if(responseModal == null) return;
            Consumer<Boolean> booleanConsumer = windowsModal.get(window);
            if(booleanConsumer != null) {
                booleanConsumer.accept(responseModal.getClickedButtonId() == 0);
                windowsModal.remove(window);
            }
        }

    }

}
