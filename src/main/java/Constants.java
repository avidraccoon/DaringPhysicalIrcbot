
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
  public final Double pi;
  public final Double maxRotation;

  private Constants() {
    pi = null;
    minRotation = null;
    maxRotation = null;
  }

}