

package coloursplash.lupusclient.modules.fun;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.gui.widgets.*;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.IntSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class Spam extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
            .name("delay")
            .description("The delay between specified messages in seconds.")
            .defaultValue(2)
            .min(0)
            .sliderMax(20)
            .build()
    );

    private final Setting<Boolean> random = sgGeneral.add(new BoolSetting.Builder()
            .name("random")
            .description("Selects a random message from your spam message list.")
            .defaultValue(false)
            .build()
    );

    private final List<String> messages = new ArrayList<>();
    private int timer;
    private int messageI;

    public Spam() {
        super(Categories.Fun, "spam", "Spams specified messages in chat.");
    }

    @Override
    public void onActivate() {
        timer = delay.get() * 20;
        messageI = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (messages.isEmpty()) return;

        if (timer <= 0) {
            int i;
            if (random.get()) {
                i = Utils.random(0, messages.size());
            } else {
                if (messageI >= messages.size()) messageI = 0;
                i = messageI++;
            }

            mc.player.sendChatMessage(messages.get(i));

            timer = delay.get() * 20;
        } else {
            timer--;
        }
    }

    @Override
    public WWidget getWidget() {
        messages.removeIf(String::isEmpty);

        WTable table = new WTable();
        fillTable(table);
        return table;
    }

    private void fillTable(WTable table) {
        table.add(new WHorizontalSeparator("Messages"));

        // Messages
        for (int i = 0; i < messages.size(); i++) {
            int msgI = i;
            String message = messages.get(i);

            WTextBox textBox = table.add(new WTextBox(message, 100)).fillX().expandX().getWidget();
            textBox.action = () -> messages.set(msgI, textBox.getText());

            WMinus minus = table.add(new WMinus()).getWidget();
            minus.action = () -> {
                messages.remove(msgI);

                table.clear();
                fillTable(table);
            };

            table.row();
        }

        // New Message
        WPlus plus = table.add(new WPlus()).fillX().right().getWidget();
        plus.action = () -> {
            messages.add("");

            table.clear();
            fillTable(table);
        };
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = super.toTag();

        messages.removeIf(String::isEmpty);
        ListTag messagesTag = new ListTag();

        for (String message : messages) messagesTag.add(StringTag.of(message));
        tag.put("messages", messagesTag);

        return tag;
    }

    @Override
    public Module fromTag(CompoundTag tag) {
        messages.clear();

        if (tag.contains("messages")) {
            ListTag messagesTag = tag.getList("messages", 8);
            for (Tag messageTag : messagesTag) messages.add(messageTag.asString());
        } else {
            messages.add("Lupus on Crack!");
        }

        return super.fromTag(tag);
    }
}
