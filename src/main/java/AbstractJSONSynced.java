
public abstract class AbstractJSONSynced {

  public abstract JSONSync getSynced();
  
  public abstract AbstractJSONSynced getObjectInstance();

  public void loadJSONFile(String path) {
    getSynced().loadJSONFile(path);
  }

  public static AbstractJSONSynced getInstance() {
    throw new RuntimeException("This code should not be run");
  }
}