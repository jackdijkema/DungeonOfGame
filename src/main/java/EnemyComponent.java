import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

public class EnemyComponent extends Component {

//    private Entity player;
//    private final Entity enemy = FXGL.getGameWorld().getSingleton(MapTest.EntityType.ENEMY);
    private double speed;
    private PhysicsComponent physics;

    public EnemyComponent(PhysicsComponent physics) {
        this.physics = physics;
    }

    @Override
    public void onAdded() {
        super.onAdded();

//        this.player =
    }

    @Override
    public void onUpdate(double tpf) {

        Entity player = FXGL.getGameWorld().getSingleton(MapTest.EntityType.PLAYER);

        //double richting = (player.getY() - physics.getEntity().getY() / player.getX() - player.getX());

        physics.setLinearVelocity(player.getX() - physics.getEntity().getX(), player.getY() - physics.getEntity().getY());

    }
}