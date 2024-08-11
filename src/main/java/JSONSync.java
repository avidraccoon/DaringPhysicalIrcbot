import com.google.gson.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class JSONSync<T> {
  final Gson gson;
  private final boolean keepOldValues;
  private ArrayList<Timer> timers = new ArrayList<Timer>();
  private T genericInstance;

  class JSONSyncTask extends TimerTask {

    String filePath = "";
    JSONSync updateInstance;
    int hash = -1;

    public JSONSyncTask(String path, JSONSync instance) {
      filePath = path;
      updateInstance = instance;
    }

    public void run() {
      try {
        int hash = FileIO.readFileAsString(filePath).hashCode();
        if (this.hash == hash)
          return;
        updateInstance.loadJSONFile(filePath);
        System.out.println("Updated");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public Timer autoSyncFromFile(String path, int period) {
    return autoSyncFromFile(path, 0, period);
  }

  public Timer autoSyncFromFile(String path, int delay, int period) {
    Timer newTimer = new Timer();
    newTimer.scheduleAtFixedRate(new JSONSyncTask(path, this), delay, period);
    timers.add(newTimer);
    return newTimer;
  }

  public void stopTimer(Timer timer) {
    timer.cancel();
    timers.remove(timer);
  }

  public void StopAllTimers() {
    for (Timer t : timers) {
      t.cancel();
    }
    timers.clear();
  }

  public boolean doAutoReload() {
    JSONConfig config = genericInstance.getClass().getAnnotation(JSONConfig.class);
    if (config == null) {
      throw new RuntimeException("No JSONConfig annotation found on class: " + genericInstance.getClass().getName());
    }
    return config.autoReload();
  }

  public JSONSync(T defaultInstance) {
    this.genericInstance = defaultInstance;
    JSONConfig config = defaultInstance.getClass().getAnnotation(JSONConfig.class);
    if (config == null) {
      throw new RuntimeException("No JSONConfig annotation found on class: " + defaultInstance.getClass().getName());
    }
    GsonBuilder builder = new GsonBuilder()
        .setNumberToNumberStrategy(config.numberToNumberPolicy())
        .setObjectToNumberStrategy(config.objectToNumberPolicy())
        .setLongSerializationPolicy(config.longSerializationPolicy())
        .setFieldNamingPolicy(config.namingPolicy());
    if (config.excludeFieldsWithoutExposeAnnotation())
      builder.excludeFieldsWithoutExposeAnnotation();
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
    loadJSONString(FileIO.readFileAsString(fileName));
  }

  public String getJSONString() {
    return gson.toJson(genericInstance);
  }

  public void saveJSONFile(String fileName) throws Exception {
    FileIO.writeFile(fileName, gson.toJson(genericInstance));
  }
}