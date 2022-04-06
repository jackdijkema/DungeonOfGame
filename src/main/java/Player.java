import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.awt.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;


public class Player extends Component {
    private String direction = "down";
    private PhysicsComponent physics;
    private AnimatedTexture texture;
    private int movementSpeed = 200;

    public Player(){
        AnimationChannel initialAnimation = new AnimationChannel(
                image("player_idle_" + direction + ".png"),
                2,
                72,
                72,
                Duration.seconds(1),
                0,
                1
        );
        texture = new AnimatedTexture(initialAnimation);
        texture.loop();
    }

    public Image getAnimationImageBasedOnMovement(){
        String animationName = "player_";

        // of je krijgt move of idle animatie
        animationName += physics.isMovingX() || physics.isMovingY() ? "move_" : "idle_";

        animationName += direction;

        return image(animationName + ".png");
    }

    public void left(){
        direction="left";
        physics.setVelocityX(-movementSpeed);
    }

    public void right(){
        direction="right";
        physics.setVelocityX(movementSpeed);
    }

    public void up(){
        direction="up";
        physics.setVelocityY(-movementSpeed);
    }
    public void down(){
        direction="down";
        physics.setVelocityY(movementSpeed);
    }

}
