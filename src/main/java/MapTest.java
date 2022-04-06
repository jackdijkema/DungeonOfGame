import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;


public class MapTest extends GameApplication{
    private Entity player;
    public enum EntityType{PLAYER, ENEMY}

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(500);
        settings.setWidth(500);
    }

    @Override
    protected void initInput(){

        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onActionBegin() {
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

    }

    @Override
    protected void initGame() {

        FXGL.getGameWorld().addEntityFactory(new TestFactory());
        FXGL.setLevelFromMap("map_1.tmx");
        player = FXGL.getGameWorld().spawn("player", 50, 50);
    }

    @Override
    protected void initPhysics(){
        FXGL.getPhysicsWorld().setGravity(0,0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
