package bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

public class Bird extends Sprite {

    private World world;
    private Body body;

    private boolean isAlive;

    private Texture birdDead;

    private TextureAtlas birdAtlas;
    private Animation animation;
    private float elapsedTime;

    public Bird(World world, float x, float y) {
        super(new Texture("Birds/bird.png"));
        birdDead = new Texture("Birds/dead.png");
        this.world = world;
        setPosition(x/3,y);
        createBody();
        createAnimation();
    }

    void createBody(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / GameInfo.PPM, getY()/GameInfo.PPM);

        body = world.createBody(bodyDef);
        body.setFixedRotation(false);

        CircleShape shape = new CircleShape();
        shape.setRadius((getHeight()/2f)/GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = GameInfo.BIRD;
        fixtureDef.filter.maskBits = GameInfo.GROUND | GameInfo.PIPE | GameInfo.SCORE;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Bird");

        shape.dispose();
        body.setActive(false);

    }

    public void activateBird(){
        isAlive = true;
        body.setActive(true);
    }

    public void birdFlap(){

        body.setLinearVelocity(0,5f);
    }

    public void drawIdle(SpriteBatch batch) {
        if (!isAlive) {
            batch.draw(this, getX() - getWidth() / 2f, getY() - getHeight() / 2f);
        }
    }

    public void animateBird(SpriteBatch batch){

        if(isAlive) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime, true), getX() - getWidth() / 2f, getY() - getHeight() / 2f);
        }
    }

    public void updateBird(){
        setPosition(body.getPosition().x * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
    }

    void createAnimation(){
        birdAtlas = new TextureAtlas("Birds/copter.atlas");
        animation = new Animation(1f/5f, birdAtlas.getRegions());

    }

    public void setAlive(boolean isAlive){
        this.isAlive = isAlive;

    }

    public boolean getAlive(){
        return isAlive;
    }

    public void birdDied(){
        this.setTexture(birdDead);

    }
}
