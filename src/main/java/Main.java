

public class Main {
  
  public static void main(String[] args) {
    Constants.syncInstance.loadJSONFile("src/main/resources/constants.json");
    TestingUtils.selectOption(Constants.getInstance());
    //System.out.println();
  }

}