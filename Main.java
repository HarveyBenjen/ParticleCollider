import javafx.stage.Stage;
import javafx.application.Application;

public class Main extends Application {
    GameLoop game = new ParticleCollider(60, "Particle Collider");
    
    @Override
    public void start(Stage stage) {
        game.initialize(stage);
        game.startLoop();
        stage.show();       
    }
}
