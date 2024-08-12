import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class FileIO{

  public static String readFileAsString(String fileName){
    try {
      String data = "";
      data = new String(Files.readAllBytes(Paths.get(fileName)));
      return data;
    }catch (IOException e){
      throw new RuntimeException("Unable to read file: " + fileName, e);
    }
  }

  public static void writeFile(String path, String data){
    FileWriter writer = createFileWritter(path);
    try{
      writer.write(data);
      writer.close();
    }catch (IOException e){
      throw new RuntimeException("Error writing to file: "+path, e);
    }
  }

  public static File createFile(String path){
    File file = new File(path);
    file.getParentFile().mkdirs();
    try{
      file.createNewFile();
      return file;
    }catch (IOException e){
      throw new RuntimeException("Unable to create file: "+path, e);
    }
  }

  public static FileWriter createFileWritter(String path){
    return createFileWriter(createFile(path));
  }

  public static FileWriter createFileWriter(File file){
    try {
      return new FileWriter(file);
    }catch (IOException e){
      throw new RuntimeException("Unable to create file writer of path: "+file.getPath(), e);
    }
  }
  
}