import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Game extends GameApplication {

    private Entity player;
    private char richtingx;
    private char richtingy;
    private static int teller = 0;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("GameSEDungeon");
        settings.setVersion("1.0");

    }

    @Override
    protected void initGame(){
        player = FXGL.entityBuilder()
                .at(400, 400)
                .viewWithBBox(new Rectangle(30, 30, Color.RED))
                .with(new CollidableComponent(true))
                .type(EntityTypes.PLAYER)
                .buildAndAttach();

        FXGL.getGameScene().setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void initInput(){
        FXGL.onKey(KeyCode.D, () -> {
            player.translateX(5);
            richtingx = 'D';
        });

        FXGL.onKey(KeyCode.A, () -> {
            player.translateX(-5);
            richtingx = 'A';
        });

        FXGL.onKey(KeyCode.W, () -> {
            player.translateY(-5);
            richtingy = 'W';
        });

        FXGL.onKey(KeyCode.S, () -> {
            player.translateY(5);
            richtingy = 'S';
        });

        FXGL.onKey(KeyCode.SPACE, () -> {
            shoot();
        });
    }

    @Override
    protected void initPhysics(){
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.PLAYER, EntityTypes.STAR) {
            @Override
            protected void onCollision(Entity player, Entity star) {
                star.removeFromWorld();
            }
        });
    }

    protected void shoot(){
        int xrichting = 0;
        int yrichting = 0;
        if(richtingx == 'A')
            xrichting = -1;
        else if (richtingx == 'D')
            xrichting = 1;
        if (richtingy == 'W')
            yrichting = -1;
        else if (richtingy == 'S')
            yrichting = 1;
        Point2D direction = new Point2D(xrichting, yrichting);
        FXGL.entityBuilder()
                .at(player.getRightX(), player.getBottomY())
                .viewWithBBox(new Circle(5, Color.WHITE))
                .with(new ProjectileComponent(direction, 500))
                .buildAndAttach();
    }

    public static void main(String[] args){
        launch(args);
    }
}
