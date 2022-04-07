import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class DungeonOfGameMainMenu extends FXGLMenu {

    Pane contentBox = new Pane();

    public DungeonOfGameMainMenu() {
        super(MenuType.MAIN_MENU);

        Texture background = texture("main-menu/background.png",getAppWidth(), getAppHeight());
        Texture title = texture("main-menu/title.png");

        var settings = getSettings();

        var btnPlay = new DungeonButton("Play Game", this::fireNewGame);
//      TODO: Make options menu work.
        var btnOptions = new DungeonButton("Options", () -> {});
        var btnExit = new DungeonButton("Exit", this::fireExit);

        var menuItems = new VBox(15,
            btnPlay, btnOptions, btnExit,
                new Separator(Orientation.HORIZONTAL),
                getUIFactoryService().newText("Developed with luv by Luuk, Rick, Hassan & Jack :)"),
                getUIFactoryService().newText("Version: " + settings.getVersion())
        );

        //Center Title image
        title.setTranslateY(getAppHeight() / 6.0);
        title.setTranslateX((getAppWidth() - title.getWidth())/ 2 + (35 / 2.00));

        // Center Menu TODO: Center better
        menuItems.setTranslateX(800);
        menuItems.setTranslateY(450);

        // Menu TEXT ALIGNMENT
        menuItems.setAlignment(Pos.CENTER_LEFT);

        getContentRoot().getChildren().addAll(
            background,
            contentBox,
            title,
            menuItems
        );
    }

    private static class DungeonButton extends StackPane {
        private final String name;
        private Runnable action;
        private Text text;
        private  Rectangle selector;

        public DungeonButton(String name, Runnable action) {
            this.name = name;
            this.action = action;

            text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 17.0);

            selector = new Rectangle(7, 17, Color.WHITE);

            setOnMouseClicked(e -> action.run());

            getChildren().addAll(selector, text);
        }
    }
}