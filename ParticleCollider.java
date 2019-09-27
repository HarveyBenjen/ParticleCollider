import java.util.Random;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.EventHandler;
import javafx.animation.Timeline;
import static javafx.animation.Animation.Status.RUNNING;
import static javafx.animation.Animation.Status.STOPPED;

public class ParticleCollider extends GameLoop {
    private final static Label spritesNumber = new Label();
    
    /************* CONSTRUCTOR ************/

    ParticleCollider(int fps, String title){
        super(fps, title);
    }
    
    @Override
    public void initialize(final Stage stage) {
        final Timeline gameLoop = getLoop();
        stage.setTitle(getWindowTitle());
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), 1000, 800));
        stage.setScene(getGameSurface());
        generateSpheres(150);
        HBox hud = new HBox(5, new Label("Number of Particles: "), spritesNumber);
        Button regenerate = new Button("Regenerate");
        regenerate.setOnMousePressed(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent me) {
                                        generateSpheres(150);}
                                    });
        Button freeze = new Button("Freeze/Resume");
        freeze.setOnMousePressed(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent arg0) {
                                        switch (gameLoop.getStatus()) {
                                            case RUNNING: gameLoop.stop();
                                                          break;
                                            case STOPPED:
                                                          gameLoop.play();
                                                          break;
                                        }
                                    }
                                });
        VBox stats = new VBox(5, hud, regenerate, freeze);
        stats.setTranslateX(10);
        stats.setTranslateY(10);
        getSceneNodes().getChildren().add(stats);
    }

    private void generateSpheres(int num) {
        Random rand = new Random();
        Scene gameSurface = getGameSurface();
        for (int i=0; i<num; i++) {
            Color c = Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
            Particle b = new Particle(rand.nextInt(15) + 5, c);
            Circle circle = b.getAsCircle();
            b.vectorX = (rand.nextInt(2) + rand.nextDouble()) * (rand.nextBoolean() ? 1 : -1);
            b.vectorY = (rand.nextInt(2) + rand.nextDouble()) * (rand.nextBoolean() ? 1 : -1);
            double newX = rand.nextInt((int) gameSurface.getWidth());
            if (newX > (gameSurface.getWidth() - (circle.getRadius() * 2))) {
                newX = gameSurface.getWidth() - (circle.getRadius()  * 2);
            }
            double newY = rand.nextInt((int) gameSurface.getHeight());
            if (newY > (gameSurface.getHeight() - (circle.getRadius() * 2))) {
                newY = gameSurface.getHeight() - (circle.getRadius() * 2);
            }
            circle.setTranslateX(newX);
            circle.setTranslateY(newY);
            circle.setVisible(true);
            circle.setId(b.toString());
            getSpriteManager().addSprites(b);
            getSceneNodes().getChildren().add(0, b.node);            
        }
    }
    
    @Override
    protected void handleUpdate(Sprite sprite) {
        if (sprite instanceof Particle) {
            Particle particle = (Particle) sprite;
            particle.update();
            if (particle.node.getTranslateX() > (getGameSurface().getWidth()  -
            particle.node.getBoundsInParent().getWidth()) ||
            particle.node.getTranslateX() < 0 ) 
            {                 
                particle.vectorX = particle.vectorX * -1;
            }             
            if (particle.node.getTranslateY() > getGameSurface().getHeight()-
            particle.node.getBoundsInParent().getHeight() ||
            particle.node.getTranslateY() < 0) 
            {
                particle.vectorY = particle.vectorY * -1;
            }
        }
    }

    @Override
    protected boolean handleCollision(Sprite sprite1, Sprite sprite2) {
        if (sprite1.collide(sprite2)) {
            ((Particle)sprite1).vanish(this);
            ((Particle)sprite2).vanish(this);
            getSpriteManager().addSpritesForRemoval(sprite1, sprite2);
            return true;
        }
        return false;
    }

    @Override
    protected void cleanupSprites() {
        super.cleanupSprites();
        spritesNumber.setText(String.valueOf(getSpriteManager().getActiveSprites().size()));
    }
}
