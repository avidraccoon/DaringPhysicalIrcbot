
@JSONConfig(autoReload = true)
public class Constants {

  public static final JSONSync<Constants> syncInstance = new JSONSync<Constants>(new Constants());

  public static Constants getInstance() {
    return syncInstance.getInstance();
  }

  public Double minRotation;
  public Double pi;
  public Double maxRotation;

  private Constants() {
    pi = null;
    minRotation = null;
    maxRotation = null;
  }

}