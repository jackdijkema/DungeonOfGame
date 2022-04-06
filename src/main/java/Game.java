import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Game extends GameApplication {

    private Entity player;

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
                .viewWithBBox("player/player.png")
                .scale(0.5, 0.5)
                .with(new CollidableComponent(true))
                .type(EntityTypes.PLAYER)
                .buildAndAttach();

        FXGL.getGameScene().setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void initInput(){
        FXGL.onKey(KeyCode.D, () -> {
            player.translateX(5);
        });

        FXGL.onKey(KeyCode.A, () -> {
            player.translateX(-5);
        });

        FXGL.onKey(KeyCode.W, () -> {
            player.translateY(-5);
        });

        FXGL.onKey(KeyCode.S, () -> {
            player.translateY(5);
        });

        FXGL.onBtnDown(MouseButton.PRIMARY, () ->{
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
        final int SHOOT_POS = 2;
        Point2D direction = new Point2D(FXGL.getInput().getMouseXWorld() - (player.getRightX() + player.getX())/2, FXGL.getInput().getMouseYWorld() - (player.getBottomY() + player.getY())/2);
        FXGL.entityBuilder()
                .at((player.getX() + player.getRightX()) / SHOOT_POS, (player.getY() + player.getBottomY()) / SHOOT_POS)
                .viewWithBBox(new Circle(5, Color.ORANGE))
                //.viewWithBBox("player/vuur.png")
                .with(new ProjectileComponent(direction, 500))
                .with(new CollidableComponent(true))
                .type(EntityTypes.BALL)
                .buildAndAttach();
    }

    public static void main(String[] args){
        launch(args);
    }
}
