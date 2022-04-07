import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class EnemyComponent extends Component {


    protected void shoot() {
        spawn("Bullet", new SpawnData(0, 0).put("owner", getEntity()));

        play("shoot" + (int)(Math.random() * 4 + 1) + ".wav");
    }
}