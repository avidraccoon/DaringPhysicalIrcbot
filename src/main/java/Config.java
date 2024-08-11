import com.google.gson.*;
import com.google.gson.annotations.*;

public class Config {

  public static final JsonConfig<Config> configInstance = new JsonConfig<Config>(new Config());

  public static Config getInstance() {
    return configInstance.getInstance();
  }

  public final double pi;
  public final double minRotation;
  public final double maxRotation;

  private Config() {
    pi = 0.0;
    minRotation = 0.0;
    maxRotation = 0.0;
  }

}