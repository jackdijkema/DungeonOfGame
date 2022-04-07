import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class DungeonOfGameMainMenu extends FXGLMenu {

    Pane contentBox = new Pane();

    public DungeonOfGameMainMenu() {
        super(MenuType.MAIN_MENU);

        var background = texture("main-menu/background.png",getAppWidth(), getAppHeight());
        var title = texture("main-menu/title.png");

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
        menuItems.setAlignment(Pos.CENTER);

        getContentRoot().getChildren().addAll(
                background,
                contentBox,
                title,
                menuItems
        );
    }
}