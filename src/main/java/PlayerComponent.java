import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.awt.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class PlayerComponent extends Component {

    final int SHOOT_POS = 2;

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk, animWalk2;

    private int jumps = 2;

    public PlayerComponent() {
        animIdle = new AnimationChannel(image("Character_Down.png"), 4, 128/4, 42, Duration.seconds(1), 1, 1);
        animWalk = new AnimationChannel(image("Character_Right.png"), 4, 128/4, 42, Duration.seconds(0.66), 0, 3);
        animWalk2 = new AnimationChannel(image("Character_Left.png"), 4, 128/4, 42, Duration.seconds(0.66), 0, 3);
        private int speed = 150;
        public void onUpdate(Entity entity, double tpf){

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }


    @Override
    public void onUpdate(double tpf) {
        if (physics.getVelocityX() > 0.0d) {
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {

            if(physics.getVelocityX() < 0.0d){
                if(texture.getAnimationChannel() != animWalk2){
                    texture.loopAnimationChannel(animWalk2);
                }
            }
            else{
                if (texture.getAnimationChannel() != animIdle) {
                    texture.loopAnimationChannel(animIdle);
                }
            }
        }
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);
    }

    public void left(){
        physics.setVelocityX(-speed);
    }

    public void leftEnd(){
        physics.setVelocityX(0);
    }

    public void right(){
        physics.setVelocityX(speed);
    }

    public void rightEnd(){
        physics.setVelocityX(0);
    }

    public void up(){
        physics.setVelocityY(-speed);
    }

    public void upEnd(){
        physics.setVelocityY(0);
    }

    public void down(){
        physics.setVelocityY(speed);
   }

    public void downEnd(){
        physics.setVelocityY(0);
    }

    public void shoot(Entity player){
        Point2D direction = new Point2D(FXGL.getInput().getMouseXWorld() - (player.getRightX() + player.getX())/2, FXGL.getInput().getMouseYWorld() - (player.getBottomY() + player.getY())/2);
        FXGL.entityBuilder()
            .at((player.getX() + player.getRightX()) / SHOOT_POS, (player.getY() + player.getBottomY()) / SHOOT_POS)
            .viewWithBBox(new Circle(5, Color.ORANGE))
            .with(new ProjectileComponent(direction, 500))
            .with(new CollidableComponent(true))
            .type(MapTest.EntityType.BALL)
            .buildAndAttach();
    }


}
