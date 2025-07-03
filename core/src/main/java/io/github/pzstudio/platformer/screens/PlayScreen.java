package io.github.pzstudio.platformer.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle; // Import necessario per Rectangle
import com.badlogic.gdx.utils.Array; // Import per Array (lista dinamica)
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.pzstudio.platformer.Main;

public class PlayScreen implements Screen {

    private Main game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    private ShapeRenderer shapeRenderer;

    private float playerX, playerY;
    private float playerWidth, playerHeight;
    private Rectangle playerBounds;

    private float playerVelocityY;
    private float gravity = -500f;
    private boolean onGround;

    // Piattaforme: Un array di rettangoli per rappresentare le piattaforme
    private Array<Rectangle> platforms;

    public PlayScreen(Main game) {
        this.game = game;

        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, gamecam);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        shapeRenderer = new ShapeRenderer();

        playerX = 100;
        playerY = 100;
        playerWidth = 32;
        playerHeight = 32;
        playerBounds = new Rectangle(playerX, playerY, playerWidth, playerHeight);

        playerVelocityY = 0;
        onGround = true;

        // --- Inizializzazione delle piattaforme ---
        platforms = new Array<Rectangle>();
        // Piattaforma iniziale (il "suolo") che copre l'intera larghezza del mondo
        // La Y è 80 per dare spazio sotto il giocatore e testare la caduta
        platforms.add(new Rectangle(0, 80, Main.WORLD_WIDTH, 20));

        // Alcune piattaforme aggiuntive per testare il salto e lo scrolling
        platforms.add(new Rectangle(300, 150, 100, 20)); // Piattaforma a destra della partenza
        platforms.add(new Rectangle(500, 220, 150, 20)); // Piattaforma più alta
        platforms.add(new Rectangle(800, 100, 200, 20)); // Piattaforma più lontana
        platforms.add(new Rectangle(1200, 180, 100, 20)); // Ultima piattaforma
        // --- Fine inizializzazione piattaforme ---
    }

    @Override
    public void show() {
        Gdx.app.log("INIT_DEBUG", "Main.V_WIDTH: " + Main.V_WIDTH + ", Main.V_HEIGHT: " + Main.V_HEIGHT);
        Gdx.app.log("INIT_DEBUG", "Main.WORLD_WIDTH: " + Main.WORLD_WIDTH + ", Main.WORLD_HEIGHT: " + Main.WORLD_HEIGHT);
        Gdx.app.log("INIT_DEBUG", "gamePort.getWorldWidth(): " + gamePort.getWorldWidth() + ", gamePort.getWorldHeight(): " + gamePort.getWorldHeight());
    }

    public void handleInput(float dt) {
        float playerHorizontalSpeed = 150;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerX -= playerHorizontalSpeed * dt;
            Gdx.app.log("INPUT_DEBUG", "Tasto Sinistro premuto. playerX: " + playerX);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerX += playerHorizontalSpeed * dt;
            Gdx.app.log("INPUT_DEBUG", "Tasto Destro premuto. playerX: " + playerX);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && onGround) {
            playerVelocityY = 250;
            onGround = false;
            Gdx.app.log("INPUT_DEBUG", "Tasto UP premuto. Salto. playerVelocityY: " + playerVelocityY);
        }
    }

    public void update(float dt) {
        handleInput(dt);

        // --- Limita la posizione del giocatore all'interno del mondo ---
        if (playerX < 0) {
            playerX = 0;
        }
        if (playerX > Main.WORLD_WIDTH - playerWidth) {
            playerX = Main.WORLD_WIDTH - playerWidth;
        }
        // --- Fine limiti giocatore ---

        onGround = false; // Presupponiamo che non sia a terra all'inizio del frame

        // Applica la gravità e aggiorna la posizione Y
        playerVelocityY += gravity * dt;
        playerY += playerVelocityY * dt;

        // Aggiorna la posizione del rettangolo di collisione del giocatore
        playerBounds.setPosition(playerX, playerY);

        // --- Gestione delle collisioni con le piattaforme ---
        for (Rectangle platform : platforms) {
            if (playerBounds.overlaps(platform)) {
                // Se il giocatore sta cadendo e collisiona con la parte superiore della piattaforma
                if (playerVelocityY < 0) {
                    // Riporta il giocatore sul bordo superiore della piattaforma
                    playerY = platform.y + platform.height;
                    playerBounds.y = playerY; // Aggiorna anche il bounds
                    playerVelocityY = 0; // Ferma la caduta
                    onGround = true; // Il giocatore è a terra
                    Gdx.app.log("COLLISION_DEBUG", "Collisione con piattaforma. PlayerY: " + playerY);
                }
                // TODO: Aggiungere logica per collisioni laterali o dal basso se necessario
            }
        }

        // Se il giocatore cade sotto il mondo (senza collisioni con piattaforme)
        if (playerY < 0) {
            playerY = 0;
            playerVelocityY = 0;
            onGround = true;
            Gdx.app.log("COLLISION_DEBUG", "Caduto sotto il mondo. PlayerY: " + playerY);
        }

        // --- Aggiorna la telecamera per seguire il giocatore ---
        float targetCamX = playerX + playerWidth / 2;
        float targetCamY = playerY + playerHeight / 2;

        gamecam.position.x = targetCamX;
        gamecam.position.y = targetCamY;

        // --- Limita la telecamera ai bordi del mondo di gioco ---
        float camHalfWidth = gamePort.getWorldWidth() / 2;
        float camHalfHeight = gamePort.getWorldHeight() / 2;

        if (gamecam.position.x < camHalfWidth) {
            gamecam.position.x = camHalfWidth;
        }
        if (gamecam.position.x > Main.WORLD_WIDTH - camHalfWidth) {
            gamecam.position.x = Main.WORLD_WIDTH - camHalfWidth;
        }

        if (gamecam.position.y < camHalfHeight) {
            gamecam.position.y = camHalfHeight;
        }
        if (gamecam.position.y > Main.WORLD_HEIGHT - camHalfHeight) {
            gamecam.position.y = Main.WORLD_HEIGHT - camHalfHeight;
        }

        gamecam.update();

        Gdx.app.log("GAME_STATE_DEBUG", "onGround: " + onGround +
            ", playerY: " + playerY +
            ", playerVelocityY: " + playerVelocityY +
            ", playerX: " + playerX +
            ", camX: " + gamecam.position.x);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(gamecam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(playerX, playerY, playerWidth, playerHeight);

        // --- Disegna le piattaforme ---
        shapeRenderer.setColor(0, 1, 0, 1); // Colore verde per le piattaforme
        for (Rectangle platform : platforms) {
            shapeRenderer.rect(platform.x, platform.y, platform.width, platform.height);
        }
        // --- Fine disegno piattaforme ---

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
