import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public abstract class GameLoop {
    private Scene gameSurface;
    private Group sceneNodes;
    private static Timeline gameLoop;
    private final int framesPerSecond;
    private final String windowTitle;
    private final SpriteManager spriteManager = new SpriteManager();
    
    /************* GETTERS ************/

   public int getFPS() {
        return framesPerSecond;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public static Timeline getLoop() {
        return gameLoop;
    }

    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    public Scene getGameSurface() {
        return gameSurface;
    }

    public Group getSceneNodes() {
        return sceneNodes;
    }

    /************* SETTERS ************/

    public static void setGameLoop(Timeline gameLoop) {
        GameLoop.gameLoop = gameLoop;
    }

    public void setGameSurface(Scene gameSurface) {
        this.gameSurface = gameSurface;
    }

    public void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }

    /************* CONSTRUCTOR ************/

    GameLoop(final int fps, final String title) {
        framesPerSecond = fps;
        windowTitle = title;
        initializeLoop();
    }

    /************* METHODS ************/

    public abstract void initialize(final Stage stage);

    protected final void initializeLoop() {
        final Duration oneFrameAmt = Duration.millis(1000 / (float) getFPS());
        final KeyFrame oneFrame = new KeyFrame (oneFrameAmt,
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    updateSprites();
                    checkCollisions();
                    cleanupSprites();
                }
            }
        ); 
        Timeline timeline = new Timeline(oneFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        setGameLoop(timeline);
    }

    public void startLoop() {
        getLoop().play();
    }

    protected void handleUpdate(Sprite sprite) {}

    protected void updateSprites() {
        for (Sprite sprite : spriteManager.getActiveSprites()) {
            handleUpdate(sprite);
        }
    }

    protected boolean handleCollision(Sprite sprite1, Sprite sprite2) {
        return false;
    }

    protected void checkCollisions() {
        spriteManager.resetCollisionList();
        for (Sprite sprite1 : spriteManager.getCollisionList()) {
            for (Sprite sprite2 : spriteManager.getActiveSprites()) {
                handleCollision(sprite1, sprite2);
            }
        }
    }

    protected void cleanupSprites() {
        spriteManager.cleanupSprites();
    }

    public void shutdown() {
        getLoop().stop();
    }
}
