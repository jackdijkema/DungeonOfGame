import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.awt.*;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;


public class MapTest extends GameApplication{
    private int currentLevel = 0;
    private int killCount = 0;
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

        FXGL.getInput().addAction(new UserAction("Next Level") {
            @Override
            protected void onActionBegin() {
                getGameController().startNewGame();
            }
        }, KeyCode.L);

    }

    @Override
    protected void initGame() {

        FXGL.getGameWorld().addEntityFactory(new TestFactory());
        setLevel();
        player = FXGL.getGameWorld().spawn("player", 50, 50);
        enemy = FXGL.getGameWorld().spawn("enemy", 200, 200);
        enemy2 = FXGL.getGameWorld().spawn("enemy", 200, 240);
    }

    private void setLevel() {
        currentLevel += 1;
        FXGL.inc("level", +1);
        String levelPath = String.format("map_%s.tmx", currentLevel);
        Level currentLevelData = FXGL.setLevelFromMap(levelPath);
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
                var hp = player.getComponent(HealthIntComponent.class);
                hp.damage(1);

                if (hp.isZero()){
                    player.removeFromWorld();
                    getGameController().gotoMainMenu();
                }
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
                    killCount += 1;
                    FXGL.inc("kills", killCount);
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity wall) {
                ball.removeFromWorld();
            }
        });
    }

    protected void initUI(){
        Label myText = new Label("Kills");
        myText.setTranslateX(700);
        myText.setTranslateY(700);
        myText.setScaleY(3);
        myText.setScaleX(3);
        myText.textProperty().bind(FXGL.getWorldProperties().intProperty("kills").asString());

        Label myText2 = new Label("Kills: ");
        myText2.setTranslateX(600);
        myText2.setTranslateY(700);
        myText2.setScaleY(3);
        myText2.setScaleX(3);

        Label myText3 = new Label("Level: ");
        myText3.setTranslateX(600);
        myText3.setTranslateY(630);
        myText3.setScaleY(3);
        myText3.setScaleX(3);

        Label myText4 = new Label("level");
        myText4.setTranslateX(700);
        myText4.setTranslateY(630);
        myText4.setScaleY(3);
        myText4.setScaleX(3);
        myText4.textProperty().bind(FXGL.getWorldProperties().intProperty("level").asString());

        FXGL.getGameScene().addUINode(myText);
        FXGL.getGameScene().addUINode(myText2);
        FXGL.getGameScene().addUINode(myText3);
        FXGL.getGameScene().addUINode(myText4);
    }

    protected void initGameVars(Map<String, Object> vars){
        vars.put("kills", killCount);
        vars.put("level", currentLevel);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
