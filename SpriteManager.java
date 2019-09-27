import java.util.*;

public class SpriteManager {
    private final static List<Sprite> activeSprites = new ArrayList<>();
    private final static List<Sprite> collisionList = new ArrayList<>(); 
    private final static Set<Sprite> spriteRemoval = new HashSet<>();
    
    /******** GETTERS *********/

    public List<Sprite> getActiveSprites() {
        return activeSprites;
    }

    public Set<Sprite> getSpritesForRemoval() {
        return spriteRemoval;
    }

    public List<Sprite> getCollisionList() {
        return collisionList;
    }
    
    /******** METHODS *********/

    public void addSprites(Sprite... sprites) {       
        activeSprites.addAll(Arrays.asList(sprites));
    }
    
    public void removeSprites(Sprite... sprites) {       
        activeSprites.removeAll(Arrays.asList(sprites));
    }
    
    public void addSpritesForRemoval(Sprite... sprites) {
        if (sprites.length > 1) {
            spriteRemoval.addAll(Arrays.asList((Sprite[]) sprites));
        } else {
            spriteRemoval.add(sprites[0]);
        }
    }

    public void resetCollisionList() {
        collisionList.clear();
        collisionList.addAll(activeSprites);
    }

    public void cleanupSprites() {
        activeSprites.removeAll(spriteRemoval);
        spriteRemoval.clear();
    }
}
