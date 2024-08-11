
@JSONConfig(autoReload = true)
public class Constants {

  public static final JSONSync<Constants> syncInstance = new JSONSync<Constants>(new Constants());

  public static Constants getInstance() {
    return syncInstance.getInstance();
  }

  public final Double minRotation;
  public final Double pi;
  public final Double maxRotation;
  public final Double[] testing;

  private Constants() {
    pi = null;
    minRotation = null;
    maxRotation = null;
    testing = new Double[3];
  }

}