package io.github.pzstudio.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.pzstudio.platformer.Main;

public class PlayScreen implements Screen {

    private Main game; // Riferimento al gioco principale
    private OrthographicCamera gamecam; // Telecamera per il mondo di gioco
    private Viewport gamePort; // Viewport per gestire la risoluzione

    // Per ora useremo ShapeRenderer per disegnare semplici forme
    private ShapeRenderer shapeRenderer;

    // Posizione e dimensioni del giocatore
    private float playerX, playerY;
    private float playerWidth, playerHeight;

    public PlayScreen(Main game) {
        this.game = game;

        // Inizializza la telecamera e il viewport
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, gamecam);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        shapeRenderer = new ShapeRenderer();

        // Posizione iniziale e dimensioni del giocatore
        playerX = 100;
        playerY = 100;
        playerWidth = 32;
        playerHeight = 32;
    }

    @Override
    public void show() {
        // Chiamato quando questa schermata diventa la schermata attiva
    }

    public void handleInput(float dt) {
        // Gestione degli input (implementeremo dopo)
    }

    public void update(float dt) {
        handleInput(dt); // Aggiorna input
        // Aggiorna logica di gioco (movimento, collisioni, etc.)
        gamecam.update(); // Aggiorna la telecamera
    }

    @Override
    public void render(float delta) {
        // delta è il tempo trascorso dall'ultimo frame in secondi
        update(delta);

        // Pulisci lo schermo
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1); // Colore di sfondo grigio scuro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Disegna gli elementi di gioco
        // Per ora usiamo ShapeRenderer per disegnare il giocatore
        shapeRenderer.setProjectionMatrix(gamecam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1); // Rosso
        shapeRenderer.rect(playerX, playerY, playerWidth, playerHeight);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        // Chiamato quando la finestra viene ridimensionata
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
        // Chiamato quando il gioco viene messo in pausa
    }

    @Override
    public void resume() {
        // Chiamato quando il gioco riprende dalla pausa
    }

    @Override
    public void hide() {
        // Chiamato quando questa schermata non è più la schermata attiva
    }

    @Override
    public void dispose() {
        // Libera le risorse quando la schermata viene distrutta
        shapeRenderer.dispose();
    }
}
