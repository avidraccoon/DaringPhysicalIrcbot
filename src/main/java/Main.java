
public class Main {
  
  public static void main(String[] args) throws Exception {
    Constants.configInstance.loadJSONFile("src/main/resources/defaultConstants.json");
    Constants.configInstance.loadJSONFile("src/main/resources/constants.json");
  }

}