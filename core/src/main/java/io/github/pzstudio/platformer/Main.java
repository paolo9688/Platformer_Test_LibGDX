package io.github.pzstudio.platformer;

import com.badlogic.gdx.Game;
import io.github.pzstudio.platformer.screens.PlayScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    // Larghezza e altezza virtuale della viewport (quello che vedi sullo schermo)
    public static final int V_WIDTH = 800;
    public static final int V_HEIGHT = 480;

    // Larghezza e altezza del mondo di gioco (il livello intero)
    // È FONDAMENTALE che WORLD_WIDTH sia MAGGIORE di V_WIDTH per lo scrolling orizzontale
    public static final int WORLD_WIDTH = 1600; // Esempio: il doppio della larghezza della viewport
    public static final int WORLD_HEIGHT = 480; // Per ora, stessa altezza della viewport

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
