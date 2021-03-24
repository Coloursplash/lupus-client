/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens;

import com.g00fy2.versioncompare.Version;
import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.gui.widgets.WHorizontalSeparator;
import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.gui.widgets.WTable;
import net.minecraft.util.Util;

public class NewUpdateScreen extends WindowScreen {
    public NewUpdateScreen(Version latestVer) {
        super("New Update", true);

        add(new WLabel("A new version of Lupus has been released."));
        row();

        add(new WHorizontalSeparator());

        WTable versionsT = add(new WTable()).getWidget();
        versionsT.add(new WLabel("Your version:"));
        versionsT.add(new WLabel(Config.get().version.getOriginalString()));
        versionsT.row();
        versionsT.add(new WLabel("Latest version"));
        versionsT.add(new WLabel(latestVer.getOriginalString()));
        row();

        add(new WHorizontalSeparator());

        WTable buttonsT = add(new WTable()).getWidget();
        buttonsT.add(new WButton("Download " + latestVer.getOriginalString())).fillX().expandX().getWidget().action = () -> Util.getOperatingSystem().open("http://meteorclient.com/");
        buttonsT.add(new WButton("OK")).fillX().expandX().getWidget().action = this::onClose;
    }
}
