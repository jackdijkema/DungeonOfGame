import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;


public class MapTest extends GameApplication{
    private int currentLevel = 1;
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
            @NotNull
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
                killCount = 0;
                currentLevel += 1;
                getGameController().startNewGame();
            }
        }, KeyCode.L);

    }

    @Override
    protected void initGame() {

        FXGL.getGameWorld().addEntityFactory(new TestFactory());
        setLevel();
        player = FXGL.getGameWorld().spawn("player", 50, 50);
        FXGL.getGameWorld().spawn("enemy", 200, 200);
        FXGL.getGameWorld().spawn("enemy", 200, 240);
    }

    private void setLevel() {
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
//                player.removeFromWorld();
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

                Sound hurt = FXGL.getAssetLoader().loadSound("hurt.wav");
                getAudioPlayer().playSound(hurt);

                if (hp.isZero()){

                    Sound death = FXGL.getAssetLoader().loadSound("Game_Over.wav");

                    getAudioPlayer().playSound(death);

                    player.removeFromWorld();

                    deathHandle();
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
                    FXGL.inc("kills", +1);
                    killCount += 1;
                }

                if (killCount == 2){
                    killCount = 0;
                    currentLevel += 1;
                    getGameController().startNewGame();
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


    public void deathHandle() {
        var title = texture("main-menu/title.png");

        var gameOverText = new Text("Why soooo bad? :( ");

        Button btnRestart = getUIFactoryService().newButton("Restart");
        btnRestart.setOnMouseClicked(e -> getGameController().startNewGame());
        btnRestart.setPrefWidth(300);

        Button btnMainMenu = getUIFactoryService().newButton("Main Menu");
        btnMainMenu.setOnMouseClicked(e -> getGameController().gotoMainMenu());
        btnMainMenu.setPrefWidth(300);

        Button btnExit = getUIFactoryService().newButton("Exit");
        btnExit.setOnMouseClicked(e -> getGameController().exit());
        btnExit.setPrefWidth(300);

        VBox menuItems = new VBox(10,
                title,
                gameOverText,
                btnMainMenu,
                btnRestart,
                btnExit
        );


        menuItems.setAlignment(Pos.CENTER);

        getDialogService().showBox("GAME OVER, YOU DIED!", menuItems);
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
        vars.put("kills", 0);
        vars.put("level", currentLevel);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
