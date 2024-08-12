import java.util.ArrayList;
@JSONConfig(autoReload = true)
public class Constants extends AbstractJSONSynced{

  public static final JSONSync<Constants> syncInstance = new JSONSync<Constants>(new Constants());

  public static AbstractJSONSynced getAbstractInstance(){
    return getInstance();
  }
  
  public static Constants getInstance() {
    return syncInstance.getInstance();
  }

  public Double minRotation;
  public Double pi;
  public Double maxRotation;
  public ArrayList<String> list;

  private Constants() {
    pi = null;
    minRotation = null;
    maxRotation = null;
    list = new ArrayList<String>();
    list.add("one");
    list.add("teset");
    list.add("three");
  }

}