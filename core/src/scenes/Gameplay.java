package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vaibhav.choppycopter.GameMain;

import bird.Bird;
import ground.GroundBody;
import helpers.GameInfo;
import hud.UIHud;
import pipes.Pipes;

public class Gameplay implements Screen, ContactListener {

    private final GameMain game;

    private final World world;

    private final OrthographicCamera mainCamera;
    private final Viewport gameViewport;


    private final Array<Sprite> backgrounds = new Array<>();
    private final Array<Sprite> grounds = new Array<>();

    private final Bird bird;

    private final UIHud hud;

    private boolean firstTouch;

    private final Array<Pipes> pipesArray = new Array<>();

    private final Sound scoreSound;
    private final Sound birdDiedSound;
    private final Sound birdFlapSound;

    public Gameplay(GameMain game) {
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 4f, GameInfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        hud = new UIHud(game);

        createBackgrounds();
        createGrounds();

        world = new World(new Vector2(0, -12.8f), true);
        world.setContactListener(this);

        bird = new Bird(world, GameInfo.WIDTH / 2f - 70, GameInfo.HEIGHT / 2f);

        GroundBody groundBody = new GroundBody(world, grounds.get(0));

        scoreSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Score.mp3"));
        birdDiedSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Dead.mp3"));
        birdFlapSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Fly.mp3"));
    }

    private void checkForFirstTouch() {

        if (!firstTouch) {

            moveBackgrounds();
            moveGrounds();
            bird.setAlive(true);

            if (Gdx.input.justTouched()) {
                firstTouch = true;
                bird.activateBird();
                createAllPipes();
            }
        }
    }

    private void update() {

        checkForFirstTouch();

        if (bird.getAlive()) {
            moveBackgrounds();
            movePipes();
            moveGrounds();
            birdFlap();
            updatePipes();
        }
    }

    private void createAllPipes() {

        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
/*                if(hud.getScore() > 20){
                    timeDelaySec = 1.5f;
                } else if (hud.getScore() > 15){
                    timeDelaySec = 1.6f;
                } else if (hud.getScore() > 10) {
                    timeDelaySec = 2f;
                } else if (hud.getScore() > 5) {
                    timeDelaySec = 2.3f;
                }
*/
                createPipes();
            }
        });

        SequenceAction sa = new SequenceAction();
        float timeDelaySec = 1.7f;
        sa.addAction(Actions.delay(timeDelaySec));
        sa.addAction(run);

        hud.getStage().addAction(Actions.forever(sa));
    }

    private void birdFlap() {
        if (Gdx.input.justTouched()) {
            birdFlapSound.play();
            if (bird.getY() < GameInfo.HEIGHT) {
                bird.birdFlap();
            }
        }
    }

    private void createBackgrounds() {
        for (int i = 0; i < 3; i++) {
            Sprite bg = new Sprite(new Texture("Backgrounds/bg0.png"));
            bg.setPosition(i * bg.getWidth(), 0);
            backgrounds.add(bg);
        }
    }

    private void createGrounds() {
        for (int i = 0; i < 3; i++) {
            Sprite ground = new Sprite(new Texture("Backgrounds/Ground.png"));
            ground.setPosition(i * ground.getWidth(), -ground.getHeight() / 1.5f);
            grounds.add(ground);
        }
    }

    private void drawBackgrounds(SpriteBatch batch) {
        for (Sprite s : backgrounds) {
            batch.draw(s, s.getX(), s.getY());
        }
    }

    private void drawGrounds(SpriteBatch batch) {
        for (Sprite ground : grounds) {
            batch.draw(ground, ground.getX(), ground.getY());
        }
    }

    private void moveBackgrounds() {

        for (Sprite bg : backgrounds) {
            float x1 = bg.getX() - 6.5f;
            bg.setPosition(x1, bg.getY());

            if (bg.getX() + GameInfo.WIDTH + (bg.getWidth()) <= mainCamera.position.x) {

                float x2 = bg.getX() + bg.getWidth() * 3;
                bg.setPosition(x2, bg.getY());

            }
        }
    }

    private void moveGrounds() {

        for (Sprite ground : grounds) {
            float x1 = ground.getX() - 5f;
            ground.setPosition(x1, ground.getY());

            if (ground.getX() + GameInfo.WIDTH + (ground.getWidth()) <= mainCamera.position.x) {

                float x2 = ground.getX() + ground.getWidth() * 3;
                ground.setPosition(x2, ground.getY());

            }
        }
    }

    private void createPipes() {
        int DISTANCE_BW_PIPES = 200;
        Pipes p = new Pipes(world, GameInfo.WIDTH + DISTANCE_BW_PIPES);
        p.setMainCamera(mainCamera);
        pipesArray.add(p);
    }

    private void drawPipes(SpriteBatch batch) {
        for (Pipes pipe : pipesArray) {
            pipe.drawPipes(batch);
        }
    }

    private void updatePipes() {
        for (Pipes pipe : pipesArray)
            pipe.updatePipes();
    }

    private void movePipes() {
        for (Pipes pipe : pipesArray) {
            pipe.movePipes();
        }
    }

    private void stopPipes() {
        for (Pipes pipe : pipesArray) {
            pipe.stopPipes();
        }
    }

    private void birdDied() {
        bird.setAlive(false);
        bird.birdDied();
        stopPipes();

        hud.getStage().clear();
        hud.showScore();

        //Key value pair
        Preferences prefs = Gdx.app.getPreferences("Data");
        int highscore = prefs.getInteger("Score");

        if (highscore < hud.getScore()) {
            prefs.putInteger("Score", hud.getScore());
            prefs.flush();
        }

        hud.createButtons();
        Gdx.input.setInputProcessor(hud.getStage());
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();

        drawBackgrounds(game.getBatch());

        bird.drawIdle(game.getBatch());
        bird.animateBird(game.getBatch());

        //draw pipes
        drawPipes(game.getBatch());
        drawGrounds(game.getBatch());

        game.getBatch().end();

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();

        bird.updateBird();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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

        world.dispose();

        for (Sprite bg : backgrounds) {
            bg.getTexture().dispose();
        }

        for (Sprite ground : grounds) {
            ground.getTexture().dispose();
        }

        for (Pipes pipe : pipesArray) {
            pipe.disposeAll();
        }

        scoreSound.dispose();
        birdFlapSound.dispose();
        birdDiedSound.dispose();

    }

    @Override
    public void beginContact(Contact contact) {

        Fixture body1, body2;

        if (contact.getFixtureA().getUserData() == "Bird") {
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        } else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        if (body1.getUserData() == "Bird" && body2.getUserData() == "Pipe") {
            if (bird.getAlive()) {
                birdDiedSound.play();
                birdDied();
            }
        }

        if (body1.getUserData() == "Bird" && body2.getUserData() == "Ground") {
            if (bird.getAlive()) {
                birdDiedSound.play();
                birdDied();

            }
        }

        if (body1.getUserData() == "Bird" && body2.getUserData() == "Score") {
            scoreSound.play();
            hud.incrementScore();
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
} // gameplay
