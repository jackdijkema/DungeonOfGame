import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import static com.almasb.fxgl.dsl.FXGL.getAppCenter;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;


public class MapTest extends GameApplication{
    private Entity player;
    private Entity enemy;
    public enum EntityType {
        PLAYER, WALL, ENEMY, BALL
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
        settings.setVersion("1.0");

        settings.setTitle("Game Of Dungeon");
        settings.setMainMenuEnabled(true);


        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new DungeonOfGameMainMenu();
            }
        });
    }

    @Override
    protected void initInput(){

        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
            }
            protected void onActionEnd(){
                player.getComponent(PlayerComponent.class).leftEnd();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).down();
            }
            protected void onActionEnd(){
                player.getComponent(PlayerComponent.class).downEnd();
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
            }
            protected void onActionEnd(){
                player.getComponent(PlayerComponent.class).rightEnd();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).up();
            }
            protected void onActionEnd(){
                player.getComponent(PlayerComponent.class).upEnd();
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {player.getComponent(PlayerComponent.class).shoot(player);}
        }, MouseButton.PRIMARY);

    }

    @Override
    public void onUpdate(double tpf) {
        if (FXGLMath.randomBoolean(0.3f)) {
            enemy.getComponent(PlayerComponent.class).left();

        }
    }

    @Override
    protected void initGame() {

        FXGL.getGameWorld().addEntityFactory(new TestFactory());
        FXGL.setLevelFromMap("map_1.tmx");
        player = FXGL.getGameWorld().spawn("player", 50, 50);
        enemy = FXGL.getGameWorld().spawn("enemy", 250, 250);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0,0);
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                player.translate(-10,0);
                enemy.translate(10,0);


            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
