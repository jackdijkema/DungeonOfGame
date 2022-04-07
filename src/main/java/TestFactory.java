import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;


public class TestFactory implements EntityFactory{

    @Spawns("wall")
    public Entity newWall(SpawnData data){
        return entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return entityBuilder(data)
                .type(MapTest.EntityType.PLAYER)
                //.viewWithBBox("player/player.png")
                .viewWithBBox(new Rectangle(30, 30, Color.BLUE))
                //.scale(0.5, 0.5)
                .collidable()
                .with(new HealthIntComponent(3))
                .with(physics)
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        //Point2D directionEnemy = new Point2D(500, 500);

        return entityBuilder(data)
                .type(MapTest.EntityType.ENEMY)
                .viewWithBBox(new Rectangle(30, 30, Color.RED))
                .collidable()
                .with(new HealthIntComponent(3))
                //.with(new ProjectileComponent(directionEnemy, 100))
                .with(physics)
                .buildAndAttach();
    }

}
