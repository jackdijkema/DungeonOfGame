import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.onCollision;


public class MapTest extends GameApplication{
    private Entity player;
    private Entity enemy;
    private Entity enemy2;
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
    protected void initGame() {

        int currentLevel = 2;
        FXGL.getGameWorld().addEntityFactory(new TestFactory());
        setLevel(currentLevel);
        player = FXGL.getGameWorld().spawn("player", 50, 50);
        enemy = FXGL.getGameWorld().spawn("enemy", 200, 200);
        enemy2 = FXGL.getGameWorld().spawn("enemy", 200, 240);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0,0);
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.WALL) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                player.translate(-10,0);
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.ENEMY, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity wall) {
                enemy.translate(-10,0);
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                player.translate(-10,0);
                enemy.translate(10,0);
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity enemy) {
                ball.removeFromWorld();

                var hp = enemy.getComponent(HealthIntComponent.class);
                hp.damage(1);

                if (hp.isZero()){
                    enemy.removeFromWorld();
                }
            }
        });
//
//        onCollision(EntityType.BALL, EntityType.WALL, (Ball, Wall) -> {
//            Ball.
//        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity wall) {
                ball.removeFromWorld();
            }
        });
    }

    private void setLevel(int level){
        GameScene gameScene = FXGL.getGameScene();
        String levelPath = String.format("map_%s.tmx", level);
        Level currentLevelData = FXGL.setLevelFromMap(levelPath);
        gameScene.getViewport().setBounds(0, 0, currentLevelData.getWidth(), currentLevelData.getHeight());
        gameScene.getViewport().setZoom(gameScene.getViewport().getHeight() / currentLevelData.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
