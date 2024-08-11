
@JsonConfig(keepOldValuesWhenNotPresent = true)
public class Constants {

  public static final JsonSync<Constants> configInstance = new JsonSync<Constants>(new Constants());

  public static Constants getInstance() {
    return configInstance.getInstance();
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