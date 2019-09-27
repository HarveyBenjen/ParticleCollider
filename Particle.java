import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class Particle extends Sprite {

    /************* CONSTRUCTOR ************/
    
    public Particle(double radius, Color fill) {
        Circle sphere = new Circle();
        sphere.setCenterX(radius);
        sphere.setCenterY(radius);
        sphere.setRadius(radius);
        RadialGradient rg = new RadialGradient(0, 0, sphere.getCenterX() - sphere.getRadius() / 3,
            sphere.getCenterY() - sphere.getRadius() / 3, sphere.getRadius(), false,
            CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, fill), new Stop(1.0, Color.BLACK)});
        sphere.setFill(rg);
        node = sphere;
    }
    
    public Circle getAsCircle() {
        return (Circle) node;
    }
   
    @Override  // Change particle's velocity
    public void update() {
        node.setTranslateX(node.getTranslateX() + vectorX);
        node.setTranslateY(node.getTranslateY() + vectorY);
    }
    
    @Override
    public boolean collide(Sprite other) {
        if (other instanceof Particle) {
            return collide((Particle)other);
        }
       return false;
    }
    
    private boolean collide(Particle other) {
        if (!node.isVisible() || !other.node.isVisible() || this == other) {
            return false;
        }
        Circle otherSphere = other.getAsCircle();
        Circle thisSphere =  getAsCircle();
        double dx = otherSphere.getTranslateX() - thisSphere.getTranslateX();
        double dy = otherSphere.getTranslateY() - thisSphere.getTranslateY();
        double distance = Math.sqrt( dx * dx + dy * dy );
        double minDist  = otherSphere.getRadius() + thisSphere.getRadius() + 3;
        return (distance < minDist);
    }
    
    public void vanish(final GameLoop game) {
        vectorX = vectorY = 0;
        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(node.getOpacity());
        ft.setToValue(0);
        ft.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg) {
                    isDead = true;
                    game.getSceneNodes().getChildren().remove(node);
                }
            });
        ft.play();
    }
}
