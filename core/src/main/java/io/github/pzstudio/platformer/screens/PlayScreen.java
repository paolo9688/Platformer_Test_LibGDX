package io.github.pzstudio.platformer.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.pzstudio.platformer.Main; // Assicurati che questo import sia corretto

public class PlayScreen implements Screen {

    private Main game; // Riferimento al gioco principale
    private OrthographicCamera gamecam; // Telecamera per il mondo di gioco
    private Viewport gamePort; // Viewport per gestire la risoluzione

    private ShapeRenderer shapeRenderer;

    private float playerX, playerY;
    private float playerWidth, playerHeight;
    private Rectangle playerBounds;

    private float playerVelocityY;
    private float gravity = -500f;
    private boolean onGround;

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
    }

    @Override
    public void show() {
        // Log iniziale per i valori del mondo e della viewport
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

        onGround = false;

        playerVelocityY += gravity * dt;
        playerY += playerVelocityY * dt;

        playerBounds.setPosition(playerX, playerY);

        if (playerY <= 100) {
            playerY = 100;
            playerVelocityY = 0;
            onGround = true;
        }

        // --- Aggiorna la telecamera per seguire il giocatore ---
        float targetCamX = playerX + playerWidth / 2;
        float targetCamY = playerY + playerHeight / 2;

        gamecam.position.x = targetCamX;
        gamecam.position.y = targetCamY;

        // --- Limita la telecamera ai bordi del mondo di gioco ---
        float camHalfWidth = gamePort.getWorldWidth() / 2;
        float camHalfHeight = gamePort.getWorldHeight() / 2;

        // Log di debug per i limiti della telecamera
        Gdx.app.log("CAMERA_LIMIT_DEBUG",
            "camHalfWidth: " + camHalfWidth +
                ", Main.WORLD_WIDTH - camHalfWidth: " + (Main.WORLD_WIDTH - camHalfWidth));
        Gdx.app.log("CAMERA_LIMIT_DEBUG",
            "camX before clamp: " + gamecam.position.x);

        // Limite X (sinistro)
        if (gamecam.position.x < camHalfWidth) {
            gamecam.position.x = camHalfWidth;
        }
        // Limite X (destro)
        if (gamecam.position.x > Main.WORLD_WIDTH - camHalfWidth) {
            gamecam.position.x = Main.WORLD_WIDTH - camHalfWidth;
        }

        // Limite Y (inferiore)
        if (gamecam.position.y < camHalfHeight) {
            gamecam.position.y = camHalfHeight;
        }
        // Limite Y (superiore)
        if (gamecam.position.y > Main.WORLD_HEIGHT - camHalfHeight) {
            gamecam.position.y = Main.WORLD_HEIGHT - camHalfHeight;
        }

        // Log di debug per la posizione della telecamera dopo il clamping
        Gdx.app.log("CAMERA_LIMIT_DEBUG",
            "camX after clamp: " + gamecam.position.x);

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
