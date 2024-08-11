import com.google.gson.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class JSONSync<T> {
  final Gson gson;
  private final boolean keepOldValues;

  private T genericInstance;

  public JSONSync(T defaultInstance) {
    this.genericInstance = defaultInstance;
    JSONConfig config = defaultInstance.getClass().getAnnotation(JSONConfig.class);
    if (config == null) {
      throw new RuntimeException("No JSONConfig annotation found on class: " + defaultInstance.getClass().getName());
    }
    GsonBuilder builder = new GsonBuilder();
    /*
     * .setNumberToNumberStrategy(config.numberToNumberPolicy())
     * .setObjectToNumberStrategy(config.objectToNumberPolicy())
     * .setLongSerializationPolicy(config.longSerializationPolicy())
     * .setFieldNamingPolicy(config.namingPolicy());
     * if (config.excludeFieldsWithoutExposeAnnotation())
     * builder.excludeFieldsWithoutExposeAnnotation();
     */
    if (config.prettyPrinting())
      builder.setPrettyPrinting();
    if (config.serializeNulls())
      builder.serializeNulls();
    keepOldValues = config.keepOldValuesWhenNotPresent();
    gson = builder.create();
  }

  public T getInstance() {
    return genericInstance;
  }

  private T mergeValues(T instance, T newValues) {
    for (Field newField : newValues.getClass().getDeclaredFields()) {
      try {
        if (newField.getType().equals(JSONSync.class))
          continue;
        if (newField.get(newValues) == null)
          continue;
        Field field = instance.getClass().getDeclaredField(newField.getName());
        field.setAccessible(true);
        field.set(instance, newField.get(newValues));
      } catch (IllegalAccessException e) {
        throw new RuntimeException(
            "Unable to set field: " + newField.getName() + " on instance: " + instance.getClass().getName());
      } catch (NoSuchFieldException e) {
        throw new RuntimeException(
            "Unable to find field: " + newField.getName() + " on instance: " + instance.getClass().getName());
      }
    }
    return instance;
  }

  @SuppressWarnings("unchecked")
  public void loadJSONString(String JSON) {
    T oldGenericsInstance = (T) genericInstance;
    genericInstance = gson.fromJson(JSON, (Class<T>) genericInstance.getClass());
    if (keepOldValues) {
      genericInstance = mergeValues(oldGenericsInstance, genericInstance);
    }
  }

  public void loadJSONFile(String fileName) throws Exception {
    loadJSONString(readFileAsString(fileName));
  }

  public String getJSONString() {
    return gson.toJSON(genericInstance);
  }

  public void saveJSONFile(String fileName) throws Exception {
    writeFile(fileName, gson.toJSON(genericInstance));
  }

  private static String readFileAsString(String fileName) throws Exception {
    String data = "";
    data = new String(Files.readAllBytes(Paths.get(fileName)));
    return data;
  }

  private void writeFile(String fileName, String data) throws Exception {
    File file = new File(fileName);
    file.getParentFile().mkdirs();
    file.createNewFile();
    FileWriter writer = new FileWriter(file);
    writer.write(data);
    writer.close();
  }
}