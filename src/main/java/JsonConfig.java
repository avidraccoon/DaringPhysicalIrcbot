import com.google.gson.*;
import com.google.gson.annotations.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;


public class JsonConfig<T>{
  static final Gson gson = new GsonBuilder()
    .serializeNulls()
    .setPrettyPrinting()
    .create();
  
  private T genericInstance;

  public JsonConfig(T defaultInstance){
    this.genericInstance = defaultInstance;
  }

  public T getInstance(){
    return genericInstance;
  }

  @SuppressWarnings("unchecked")
  public void loadJsonString(String json){
    genericInstance = gson.fromJson(json, (Class<T>) genericInstance.getClass());
  }

  public void loadJsonFile(String fileName) throws Exception{
    loadJsonString(readFileAsString(fileName));
  }

  public String getJsonString(){
    return gson.toJson(genericInstance);
  }

  public void saveJsonFile(String fileName) throws Exception{
    writeFile(fileName, gson.toJson(genericInstance));
  }

  private static String readFileAsString(String fileName) throws Exception
  {
    String data = "";
    data = new String(
      Files.readAllBytes(Paths.get(fileName)));
    return data;
  }

  private void writeFile(String fileName, String data) throws Exception{
    File file = new File(fileName);
    file.getParentFile().mkdirs();
    file.createNewFile();
    FileWriter writer = new FileWriter(file);
    writer.write(data);
    writer.close();
  }
}