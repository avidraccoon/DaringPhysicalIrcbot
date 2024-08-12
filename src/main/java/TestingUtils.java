import java.lang.reflect.*;
import java.util.*;

public class TestingUtils{

  public static ArrayList<Object> antiLoop = new ArrayList<Object>();
  public static void printObject(Object object){
    printObject(object, object.getClass()+"");
  }

  public static void printObject(Object object, String prefix) {
    if (object == null) return;
    String prefixOrginal = prefix;
    Class<?> clazz = object.getClass();
    for (Field field : clazz.getDeclaredFields()){
      try {
        prefix = prefixOrginal+"."+field.getName();
        Object obj = field.get(object);
        if (checkType(obj)){
          System.out.println(prefix + ": " + obj);
          continue;
        }
        if (antiLoop.contains(obj)) continue;
        antiLoop.add(object);
        if (isCollection(obj)){
          Collection collection = (Collection) obj;
          Object[] data = collection.toArray();
          for (Object o : data){
            printObject(o, prefix);
          }
          continue;
        }
        printObject(obj, prefix);
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
}