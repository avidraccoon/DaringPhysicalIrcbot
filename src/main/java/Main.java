
public class Main {
  public static void main(String[] args) throws Exception {
    Constants.configInstance.loadJsonFile("src/main/resources/defaultConstants.json");
    println("Default:");
    printConstants();
    Constants.configInstance.loadJsonFile("src/main/resources/constants.json");
    println("Actual:");
    printConstants();
  }

  public static void printConstants() {
    String[] keys = { "pi", "minRotation", "maxRotation" };
    double[] values = { Constants.getInstance().pi, Constants.getInstance().minRotation,
        Constants.getInstance().maxRotation };
    displayStats("Constants", keys, values, 15, 6);
  }

  public static void displayStats(String title, String[] names, double[] stats, int textLen, int statLen) {
    StringBuilder builder = new StringBuilder();
    builder.append("|");
    for (int i = 0; i < textLen + statLen + 1; i++) {
      builder.append("=");
    }
    builder.append("|\n");
    String line = builder.toString();
    builder.append("|" + center("Constants", textLen + statLen + 1) + "|\n|");
    for (int i = 0; i < textLen + statLen + 1; i++) {
      builder.append("-");
    }
    builder.append("|\n");
    for (int i = 0; i < names.length; i++) {
      builder.append("|" + displayStat(names[i], stats[i], textLen, statLen) + "|\n");
    }
    builder.append(line);
    builder.replace(0, 1, "/");
    builder.replace(builder.length() - 2, builder.length() - 1, "/");
    builder.replace(textLen + statLen + 2, textLen + statLen + 3, "\\");
    builder.replace(builder.length() - textLen - statLen - 4, builder.length() - textLen - statLen - 3, "\\");

    print(builder.toString());
  }

  public static void print(String s) {
    System.out.print(s);
  }

  public static void println(String s) {
    System.out.println(s);
  }

  public static String displayStat(String text, double stat, int textLen, int statLen) {
    return pad(text, textLen) + "|" + pad(formatNumber(stat, statLen), statLen);
  }

  public static String pad(String str, int length) {
    return String.format("%1$-" + length + "s", str);
  }

  public static String formatNumber(double number, int length) {
    String num = "" + number;
    if (num.length() >= length) {
      return num.substring(0, length);
    }
    return pad("", length - num.length()) + number;
  }

  public static String center(String str, int length) {
    length = length - str.length();
    return pad("", length - length / 2) + str + pad("", length / 2);
  }

}