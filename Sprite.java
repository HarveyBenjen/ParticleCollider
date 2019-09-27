import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.geometry.Point2D;

public abstract class Sprite {
    protected Node node;
    protected double vectorX = 0;
    protected double vectorY = 0;
    protected boolean isDead = false;
    protected Circle collisionBounds;
    protected abstract void update();

    public boolean collide(Sprite other) {
        if (collisionBounds == null || other.collisionBounds == null) {
            return false;
        }
        Circle otherSphere = other.collisionBounds;
        Circle thisSphere = collisionBounds;
        Point2D otherCenter = otherSphere.localToScene(otherSphere.getCenterX(), otherSphere.getCenterY());
        Point2D thisCenter = thisSphere.localToScene(thisSphere.getCenterX(), thisSphere.getCenterY());
        double dx = otherCenter.getX() - thisCenter.getX();
        double dy = otherCenter.getY() - thisCenter.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minDist = otherSphere.getRadius() + thisSphere.getRadius();
        return (distance < minDist);
    }

    public void handleDeath(GameLoop game) {
        game.getSpriteManager().addSpritesForRemoval(this);
    }
}
