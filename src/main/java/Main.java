import com.google.gson.*;
import com.google.gson.annotations.*;

public class Main {
  public static void main(String[] args) throws Exception {
    Config.configInstance.loadJsonFile("src/main/resources/config.json");
    System.out.println(Config.getInstance().pi);
  }
}