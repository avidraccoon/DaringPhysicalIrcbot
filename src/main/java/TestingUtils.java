import java.lang.reflect.*;
import java.util.*;
import java.lang.*;

public class TestingUtils{

  public static Scanner scan = new Scanner(System.in);
  public static String selectedField = "";
  public static ArrayList<Object> antiLoop = new ArrayList<Object>();
  public static void printObject(Object object){
    StatTextBuilder builder = new StatTextBuilder(30, 15);
    builder.setTitle(object.getClass().getName());
    printObject(object, "", builder);
    System.out.println(builder.getText());
    antiLoop.clear();
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
        builder.append((name.equals(selectedField))? ">" : "|");
        builder.append(rightPad(nameLine, nameLength));
        builder.append(" | ");
        builder.append(leftPad(statLine, lineLength-nameLength-3));
        builder.append((name.equals(selectedField))? "<" : "|");
        builder.append("\n");
      }
  
    }

    public String getText(){
      builder.append(wrap(repeat(lineLength, "-"), "|"));
      return builder.toString();
    }
  }

  public static void clear(){
    System.out.print("\033[H\033[2J");  
    System.out.flush();
  }

  public static void selectOption(Object object){
    boolean running = true;
    while (running){
      System.out.println("Select a action (1)edit a field, (2)display data, (3)exit");
      String input = scan.nextLine();
      if (input.equals("1")){
        selectField(object);
      }else if (input.equals("2")){
        clear();
        printObject(object);
      }else if (input.equals("3")){
        running = false;
      }else{
        System.out.println("Invalid input");
      }
    }
  }
  
  public static void selectField(Object object){
    clear();
    printObject(object);
    System.out.println("Select field to edit: ");
    selectedField = scan.nextLine();
    clear();
    printObject(object);
    System.out.println("Confirm selected line enter (y) to continue or (n) to quit a go back to main menu: ");
    String input;
    while (!(input = scan.nextLine()).equals("y") && !input.equals("n")){
      System.out.println("Invalid input, try again: ");
    }
    if (input.equals("n")){
      selectedField = "";
      return;
    }
    try{
      String[] list = selectedField.substring(1).split("[.]");
      Object current = object;
      Field field = current.getClass().getDeclaredField(list[0]);
      for (int i = 1; i<list.length; i++){
        current = field.get(current);
        field = current.getClass().getDeclaredField(list[i]);
      }
      System.out.println("Please input a "+ field.getType().getSimpleName() +": ");
      field.setAccessible(true);
      if (field.getType().equals(String.class)){
        field.set(current, scan.nextLine());
      }else if (field.getType().equals(Integer.class)){
        field.set(current, Integer.parseInt(scan.nextLine()));
      }else if (field.getType().equals(Double.class)){
        field.set(current, Double.valueOf(scan.nextLine()));
      }else if (field.getType().equals(Float.class)){
        field.set(current, Float.parseFloat(scan.nextLine()));
      }else if (field.getType().equals(Long.class)){
        field.set(current, Long.parseLong(scan.nextLine()));
      }else if (field.getType().equals(Short.class)){
        field.set(current, Short.parseShort(scan.nextLine()));
      }else if (field.getType().equals(Byte.class)){
        field.set(current, Byte.parseByte(scan.nextLine()));
      }else if (field.getType().equals(Boolean.class)){
        field.set(current, Boolean.parseBoolean(scan.nextLine()));
      }else if (field.getType().equals(Character.class)){
        field.set(current, scan.nextLine().charAt(0));
      }
      System.out.println(field.getType().getName());
    }catch (Exception e){
      e.printStackTrace();
      System.out.println("Invalid input");
    }
    selectedField = "";
  }
}