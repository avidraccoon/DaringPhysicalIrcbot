import java.lang.reflect.*;
import java.util.*;

public class TestingUtils{

  public static ArrayList<Object> antiLoop = new ArrayList<Object>();
  public static void printObject(Object object){
    StatTextBuilder builder = new StatTextBuilder(30, 15);
    builder.setTitle(object.getClass().getName());
    printObject(object, "", builder);
    System.out.println(builder.getText());
  }

  public static void printObject(Object object, String prefix, StatTextBuilder builder) {
    if (object == null) return;
    String prefixOrginal = prefix;
    Class<?> clazz = object.getClass();
    for (Field field : clazz.getDeclaredFields()){
      try {
        prefix = prefixOrginal+"."+field.getName();
        Object obj = field.get(object);
        if (checkType(obj)){
          builder.addStat(prefix, obj.toString());
          continue;
        }
        if (antiLoop.contains(obj)) continue;
        antiLoop.add(object);
        if (isCollection(obj)){
          Collection collection = (Collection) obj;
          Object[] data = collection.toArray();
          for (Object o : data){
            printObject(o, prefix, builder);
          }
          continue;
        }
        printObject(obj, prefix, builder);
      }catch (IllegalAccessException e){}
      catch (InaccessibleObjectException e){}
    }
  }

  public static boolean checkType(Object obj){
    if (obj == null) return false;
    Class<?> clazz = obj.getClass();
    if (clazz.equals(String.class)) return true;
    if (clazz.equals(Integer.class)) return true;
    if (clazz.equals(Double.class)) return true;
    if (clazz.equals(Float.class)) return true;
    if (clazz.equals(Long.class)) return true;
    if (clazz.equals(Short.class)) return true;
    if (clazz.equals(Byte.class)) return true;
    if (clazz.equals(Boolean.class)) return true;
    if (clazz.equals(Character.class)) return true;
    return false;
  }

  public static boolean isCollection(Object obj){
    if (obj == null) return false;
    return obj.getClass().isAssignableFrom(Collection.class);
  }

  public static String repeat(int count, String str) {
    return new String(new char[count]).replace("\0", str);
  }

  public static String repeat(int count) {
    return repeat(count, " ");
  }

  public static String leftPad(String str, int length){
    return leftPad(str, length, " ");
  }

  public static String leftPad(String baseStr, int length, String str){
    String output = (repeat((length-baseStr.length())/str.length(), str) + baseStr);
    return output.substring(output.length()-length);
  }

  public static String rightPad(String str, int length){
    return rightPad(str, length, " ");
  }

  public static String rightPad(String baseStr, int length, String str){
    return (baseStr+repeat((length-baseStr.length())/str.length(), str)).substring(0, length);
  }

  public static String center(String str, int length){
    return center(str, length, " ", false);
  }

  public static String center(String baseStr, int length, boolean right){
    return center(baseStr, length, " ", right);
  }

  public static String center(String baseStr, int length, String str){
    return center(baseStr, length, str, false);
  }

  public static String center(String baseStr, int length, String str, boolean right){
    double divide = length-baseStr.length();
    int leftSize = (int) ((!right) ? divide/2 + 0.5 : divide/2);
    int rightSize = length-baseStr.length()-leftSize;
    String padding = repeat((int) divide, str).substring(0, (int) divide);
    return padding.substring(0, leftSize) + baseStr + padding.substring(leftSize);
  }

  public static String wrap(String baseStr, String str){
    return str+baseStr+str;
  }

  public static String[] lineWrap(String str, int length){
    String[] lines = new String[(int) Math.ceil(str.length()*1.0/length)];
    for (int i = 0; i < lines.length; i++){
      lines[i] = str.substring(0, Math.min(length, str.length()));
      str = str.substring(Math.min(length, str.length()));
    }
    return lines;
  }

  static class StatTextBuilder{
    private StringBuilder builder = new StringBuilder();
    private int nameLength;
    private int statLength;
    private int lineLength;
    private int statBlocks = 0;

    public StatTextBuilder(int nameLength, int statLength){
      this.nameLength = nameLength;
      this.statLength = statLength;
      this.lineLength = nameLength+statLength+3;
    }

    public void setTitle(String name){
      String[] nameLines = lineWrap(name, lineLength);
      builder.append(wrap(repeat(lineLength, "-"), "|") + "\n");

      for (String line : nameLines){
        builder.append(wrap(wrap(center(line, lineLength-4), "  "), "|") + "\n");
      }
      
      builder.append(wrap(repeat(lineLength, "="), "|"));
    }
    
    public void addStat(String name, String stat){
      String[] nameLines = lineWrap(name, nameLength);
      String[] statLines = lineWrap(stat, statLength);
      int lines = Math.max(nameLines.length, statLines.length);
      if (statBlocks++ > 0){
        builder.append(wrap(repeat(lineLength, "-"), "|"));
      }else{
        //builder.append(wrap(repeat(lineLength, "="), "|"));
      }
      builder.append("\n");
      for (int i = 0; i < lines; i++){
        String nameLine = (nameLines.length > i)? nameLines[i] : "";
        String statLine = (statLines.length > i)? statLines[i] : "";
        builder.append("|");
        builder.append(rightPad(nameLine, nameLength));
        builder.append(" | ");
        builder.append(leftPad(statLine, lineLength-nameLength-3));
        builder.append("|");
        builder.append("\n");
      }
    }

    public String getText(){
      builder.append(wrap(repeat(lineLength, "-"), "|"));
      return builder.toString();
    }
  }
}