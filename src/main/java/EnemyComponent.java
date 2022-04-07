import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

public class EnemyComponent extends Component {

    private final Entity player = FXGL.getGameWorld().getSingleton(MapTest.EntityType.PLAYER);
    private final Entity enemy = FXGL.getGameWorld().getSingleton(MapTest.EntityType.ENEMY);
    private double speed;
    private PhysicsComponent physics;


}