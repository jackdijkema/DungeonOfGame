import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class DungeonButton extends StackPane {
    private String name;
    private Runnable action;
    private Text text;
    private Rectangle selector;

    public DungeonButton(String name, Runnable action) {
        this.name = name;
        this.action = action;

        text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 17.0);

        setOnMouseClicked(e -> action.run());

        getChildren().addAll(text);
    }
}
