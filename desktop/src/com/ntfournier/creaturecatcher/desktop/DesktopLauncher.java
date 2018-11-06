package com.ntfournier.creaturecatcher.desktop;

import com.badlogic.gdx.backends.lwjgl.*;
import com.ntfournier.creaturecatcher.Constants;
import com.ntfournier.creaturecatcher.CreatureCatcher;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = (int) (Constants.TILE_SIZE * Constants.DEFAULT_ZOOM * 9);
        config.width = (int) (Constants.TILE_SIZE * Constants.DEFAULT_ZOOM * 10);
        new LwjglApplication(new CreatureCatcher(), config);
    }
}
