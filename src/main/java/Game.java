import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.components.RandomMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Game extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
    };

    @Override
    protected void initInput() {
        onKey(KeyCode.D, () -> {
            player.translateX(5); // move right 5 pixels
            inc("pixelsMoved", +5);
        });

        onKey(KeyCode.A, () -> {
            player.translateX(-5); // move left 5 pixels
            inc("pixelsMoved", -5);
        });

        onKey(KeyCode.W, () -> {
            player.translateY(-5); // move up 5 pixels
            inc("pixelsMoved", +5);
        });

        onKey(KeyCode.S, () -> {
            player.translateY(5); // move down 5 pixels
            inc("pixelsMoved", +5);
        });
    }

    protected void onPreInit() {
        loopBGM("test.mp3");
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    private Entity player;
    private Entity enemy;

    @Override
    protected void initGame() {
        player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(300, 300)
                .viewWithBBox(new Rectangle(30, 30, Color.RED))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        entityBuilder()
                .type(EntityType.WALL)
                .at(560, 0)
                .viewWithBBox(new Rectangle(40, 600, Color.BROWN))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        enemy = entityBuilder()
                .type(EntityType.ENEMY)
                .at(300, 300)
                .viewWithBBox(new Rectangle(30, 30, Color.BLUE))
                .with(new CollidableComponent(true))
                .with(new RandomMoveComponent(new Rectangle2D(0, 0, getAppWidth() - 100, getAppHeight()), 100))
                .buildAndAttach();

        //Viewport viewport = getGameScene().getViewport();
        //viewport.bindToEntity(player, getAppWidth() / 2);
        //viewport.setLazy(true);
    }


    /*@Override
    protected void initInput(){
        getInput().addAction(new UserAction("up") {
            @Override
            protected void onAction() {
                super.onAction();
                player.getComponent(Player.class).up();
            }

            @Override
            protected void onActionEnd() {
                super.onActionEnd();
                player.getComponent(Player.class).stopYMovement();
            }
        } ;{
        })
    }*/
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
    }


    public static void main(String[] args) {
        launch(args);

    }
}