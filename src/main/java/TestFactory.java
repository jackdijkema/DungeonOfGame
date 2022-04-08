import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;


public class TestFactory implements EntityFactory{

    @Spawns("wall")
    public Entity newWall(SpawnData data){
        return entityBuilder(data)
                .type(MapTest.EntityType.WALL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        int hp = 3;

        return entityBuilder(data)
                .type(MapTest.EntityType.PLAYER)
                //.viewWithBBox("player/player.png")
                .bbox(new HitBox(BoundingShape.box(30,30)))
                //.viewWithBBox(new Rectangle(30, 30, Color.BLUE))
                .scale(2, 2)
                .collidable()
                .with(physics)
                .with(new HealthIntComponent(hp))
                .with(new HealthBarShowComponent(hp, Color.LIGHTGREEN))
                .with(new PlayerComponent())
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        int hp = 4;

        return entityBuilder(data)
                .type(MapTest.EntityType.ENEMY)
                .viewWithBBox(new Rectangle(30, 30, Color.RED))
                .collidable()
                .with(new HealthIntComponent(hp))
                .with(new HealthBarShowComponent(hp, Color.RED))
                .with(new EnemyComponent(physics))
                .with(new HealthIntComponent(3))
                .with(physics)
                .build();
    }

}
