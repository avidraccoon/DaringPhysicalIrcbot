
@JSONConfig(autoReload = true)
public class Constants extends AbstractJSONSynced {

  public static final JSONSync<Constants> syncInstance = new JSONSync<Constants>(new Constants());
  
  public Constants getObjectInstance() {
    return Constants.getInstance();
  }
  
  public JSONSync getSynced(){
    return Constants.syncInstance;
  }

  public static Constants getInstance() {
    return syncInstance.getInstance();
  }

  public final Double minRotation;
  public Double pi;
  public final Double maxRotation;
  public final Transformation transformation;

  public class Position{
    public final Double x;
    public final Double y;
    public final Double z;

    public Position(Double x, Double y, Double z){
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }

  public class Rotation{
    public final Double x;
    public final Double y;
    public final Double z;

    public Rotation(Double x, Double y, Double z){
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }

  public class Scale{
    public final Double x;
    public final Double y;
    public final Double z;

    public Scale(Double x, Double y, Double z){
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }
  
  public class Transformation{
    public final Position position;
    public final Rotation rotation;
    public final Scale scale;

    public Transformation(Position position, Rotation rotation, Scale scale){
      this.position = position;
      this.rotation = rotation;
      this.scale = scale;
    }
  }

  private Constants() {
    pi = null;
    minRotation = null;
    maxRotation = null;
    transformation = new Transformation(new Position(0.0, 0.0, 0.0),new Rotation(0.0, 0.0, 0.0),new Scale(1.0, 1.0, 1.0));
  }

}