package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.version.vaibhverty.GameMain;

import helpers.GameInfo;
import scenes.Gameplay;

public class MainMenuButtons {

    private GameMain game;

    private Stage stage;
    private Viewport gameViewport;

    private ImageButton playBtn, scoreBtn;

    private Label scoreLabel, titleLabel;

    public MainMenuButtons(GameMain game){

        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        createAndPositionButtons();
        createTitleLabel();

        stage.addActor(titleLabel);

        stage.addActor(playBtn);
        stage.addActor(scoreBtn);

    }

    void createTitleLabel(){

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;

        BitmapFont font = generator.generateFont(parameter);

        //issue
        titleLabel = new Label("Choppy Copter", new Label.LabelStyle(font, Color.WHITE));
        titleLabel.setPosition(GameInfo.WIDTH / 2f - titleLabel.getPrefWidth()/2f, (GameInfo.HEIGHT / 2f) + (GameInfo.HEIGHT / 5f));

    }

    void createAndPositionButtons(){

        playBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Play.png"))));
        scoreBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Score.png"))));

        playBtn.setPosition(GameInfo.WIDTH / 2f - GameInfo.WIDTH / 8f, GameInfo.HEIGHT / 2f - playBtn.getWidth() / 2f, Align.center);
        scoreBtn.setPosition(GameInfo.WIDTH / 2f + GameInfo.WIDTH / 8f, GameInfo.HEIGHT / 2f - scoreBtn.getWidth() / 2f, Align.center);

        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Gameplay(game));
                stage.dispose();
            }
        });

        scoreBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showScore();
            }
        });

    }

    void showScore(){

        if(scoreLabel != null) {
            return;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;

        BitmapFont font = generator.generateFont(parameter);

        scoreLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));

        Preferences prefs = Gdx.app.getPreferences("Data");
        scoreLabel = new Label("High Score: " + String.valueOf(prefs.getInteger("Score")), new Label.LabelStyle(font, Color.WHITE));

        scoreLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT/2f - GameInfo.HEIGHT/4f, Align.center);

        stage.addActor(scoreLabel);

    }

    public Stage getStage(){
        return this.stage;
    }

}




