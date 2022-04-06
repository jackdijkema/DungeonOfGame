

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerComponent extends Component {

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


}
