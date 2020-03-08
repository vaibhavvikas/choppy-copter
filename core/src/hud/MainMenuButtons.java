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

    private Label scoreLabel, titleLabel, footerLabel;

    public MainMenuButtons(GameMain game){

        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        createAndPositionButtons();
        createTitleLabel();
        createFooterLabel();

        stage.addActor(titleLabel);
        stage.addActor(footerLabel);

        stage.addActor(playBtn);
        stage.addActor(scoreBtn);

    }

    private void createTitleLabel(){

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;

        BitmapFont font = generator.generateFont(parameter);

        //issue
        titleLabel = new Label("Choppy Copter", new Label.LabelStyle(font, Color.WHITE));
        titleLabel.setPosition(GameInfo.WIDTH / 2f - titleLabel.getPrefWidth()/2f, (GameInfo.HEIGHT / 2f) + (GameInfo.HEIGHT / 5f));

    }

    private void createFooterLabel(){

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;

        BitmapFont font = generator.generateFont(parameter);

        //issue
        footerLabel = new Label("(c) Vaibhav Vikas", new Label.LabelStyle(font, Color.WHITE));
        footerLabel.setPosition(GameInfo.WIDTH / 2f - footerLabel.getPrefWidth()/2f, (GameInfo.HEIGHT / 2f) - (GameInfo.HEIGHT/ 2.5f));

    }

    private void createAndPositionButtons(){

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

    private void showScore(){

        if(scoreLabel != null) {
            return;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;

        BitmapFont font = generator.generateFont(parameter);

        scoreLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));

        Preferences prefs = Gdx.app.getPreferences("Data");
        scoreLabel = new Label("High Score: " + prefs.getInteger("Score"), new Label.LabelStyle(font, Color.WHITE));

        scoreLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT/2f - GameInfo.HEIGHT/4f, Align.center);

        stage.addActor(scoreLabel);

    }

    public Stage getStage(){
        return this.stage;
    }

}




