

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerComponent extends Component {

    final int SHOOT_POS = 2;
    private PhysicsComponent physics;

    public void onUpdate(Entity entity, double tpf){

    }

    public void left(){
        physics.setVelocityX(-100);
    }

    public void leftEnd(){
        physics.setVelocityX(0);
    }

    public void right(){
        physics.setVelocityX(100);
    }

    public void rightEnd(){
        physics.setVelocityX(0);
    }

    public void up(){
        physics.setVelocityY(-100);
    }

    public void upEnd(){
        physics.setVelocityY(0);
    }

    public void down(){
        physics.setVelocityY(100);
   }

    public void downEnd(){
        physics.setVelocityY(0);
    }

    public void shoot(Entity player){
        Point2D direction = new Point2D(FXGL.getInput().getMouseXWorld() - (player.getRightX() + player.getX())/2, FXGL.getInput().getMouseYWorld() - (player.getBottomY() + player.getY())/2);
        FXGL.entityBuilder()
            .at((player.getX() + player.getRightX()) / SHOOT_POS, (player.getY() + player.getBottomY()) / SHOOT_POS)
            .viewWithBBox(new Circle(5, Color.ORANGE))
            //.viewWithBBox("player/vuur.png")
            .with(new ProjectileComponent(direction, 500))
            .with(new CollidableComponent(true))
            .type(MapTest.EntityType.BALL)
            .buildAndAttach();
    }




}
