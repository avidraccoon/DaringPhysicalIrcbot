
public class Main {

  public static GenericConfigGUI gui = GenericConfigGUI.openGui(Constants.getInstance(), "src/main/resources/constants.json");
;
  
  public static void main(String[] args) throws Exception {
    Constants.syncInstance.loadJSONFile("src/main/resources/constants.json");
    //Constants.syncInstance.autoSyncFromFile("src/main/resources/constants.json", 2000);
    gui.update();
  }

  public static void printConstants(int hash) {
    System.out.println("PI: " + Constants.getInstance().pi);
    System.out.println("MIN_ROTATION: " + Constants.getInstance().minRotation);
    System.out.println("MAX_ROTATION: " + Constants.getInstance().maxRotation);
    System.out.println("hash: " + hash);
    System.out.println();
  }

}