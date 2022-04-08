import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.view.ChildViewComponent;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.scene.paint.Color;

public class HealthBarShowComponent extends ChildViewComponent {

    private HealthIntComponent health;
    private ProgressBar hpBar;
    private int hp;

    public HealthBarShowComponent(int hp, Color barColor) {
        super(0, 64, false);
        this.hp = hp;
        health = new HealthIntComponent(hp);

        health.setValue(hp);

        hpBar = new ProgressBar(false);
        hpBar.setWidth(60);
        hpBar.setHeight(10);
        hpBar.setFill(barColor);
        hpBar.setLabelVisible(false);

        getViewRoot().getChildren().add(hpBar);
    }


    @Override
    public void onAdded() {
        super.onAdded();

        hpBar.maxValueProperty().bind(health.maxValueProperty());
        hpBar.currentValueProperty().bind(health.valueProperty());
    }

}
