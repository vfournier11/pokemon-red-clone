package com.ntfournier.creaturecatcher.desktop;

import com.badlogic.gdx.backends.lwjgl.*;
import com.ntfournier.creaturecatcher.CreatureCatcher;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 16 * 20;
        config.width = 16 * 20;
        new LwjglApplication(new CreatureCatcher(), config);
    }
}
