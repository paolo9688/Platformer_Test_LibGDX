package io.github.pzstudio.platformer;

import com.badlogic.gdx.Game;
import io.github.pzstudio.platformer.screens.PlayScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    // Possiamo usare un larghezza e altezza virtuale per il gioco,
    // che poi verrà scalata alla risoluzione dello schermo
    public static final int V_WIDTH = 800;
    public static final int V_HEIGHT = 480;

    @Override
    public void create() {
        // Quando il gioco inizia, impostiamo la schermata di gioco
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        // Il metodo render del Game chiama il render della Screen attiva
        super.render();
    }

    @Override
    public void dispose() {
        // Qui potremmo liberare risorse globali se ne avessimo
        super.dispose();
    }
}
