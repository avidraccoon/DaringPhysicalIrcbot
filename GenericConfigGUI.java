import javax.swing.*;
import java.util.List;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class GenericConfigGUI {
  private AbstractJSONSynced configObject;
  private Map<String, JTextField> fieldTextFields = new HashMap<>();
  private Map<String, JList> listFields = new HashMap<>();
  private JFrame frame;
  private JPanel mainPanel;
  private JButton saveButton;
  private String filePath;

  public void handleType(Field field, String fieldValueString) throws IllegalAccessException{
    field.setAccessible(true);
    if (field.getType() == Integer.class) {
        field.set(configObject, Integer.parseInt(fieldValueString));
    } else if (field.getType() == Double.class) {
        field.set(Constants.getInstance(), Double.parseDouble(fieldValueString));
    } else if (field.getType() == String.class) {
        field.set(configObject, fieldValueString);
    } else {
        System.err.println("Unsupported field type: " + field.getType());
    }
  }

  public void addField(String fieldName){
    try {
      Field field = configObject.getClass().getDeclaredField(fieldName);
      if (field.get(configObject) != null && field.getType().equals(JSONSync.class)) return;
      mainPanel.remove(saveButton);
      if (field.getType().equals(List.class)) {
        // Handle lists:
        List<?> listValue = (List<?>) field.get(configObject);
        JList list = new JList(listValue.toArray());
        listFields.put(fieldName, list);
        mainPanel.add(new JLabel(fieldName + ":"));
        mainPanel.add(list);
      } else {
        // Handle primitive fields as before
        Object fieldValue = field.get(configObject);
        JTextField textField = new JTextField(String.valueOf(fieldValue));
        fieldTextFields.put(fieldName, textField);
        mainPanel.add(new JLabel(fieldName + ":"));
        mainPanel.add(textField);
      }
      mainPanel.add(saveButton);
      //update();
      frame.setVisible(false);
      frame.setVisible(true);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public void addFields(){
    for (Field field : configObject.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        try {
            
            if (field.get(configObject) != null && field.getType().equals(JSONSync.class)) continue; 
            String fieldName = field.getName();
            Object fieldValue = field.get(configObject);
            String labelText = fieldName + ":"; // Create label with field name only
            JLabel fieldLabel = new JLabel(labelText);
            mainPanel.add(fieldLabel); // Add label to panel
            JTextField textField = new JTextField(String.valueOf(fieldValue)); // Create TextField with initial value
            fieldTextFields.put(fieldName, textField); // Store TextField for later reference
            mainPanel.add(textField); // Add TextField to panel
        } catch (IllegalAccessException e) {
            System.err.println("Error accessing field: " + field.getName());
        }
    }
  }

  public void update(){
    this.configObject = this.configObject.getAbstractInstance();
    SwingUtilities.invokeLater(() -> {
        updateFields(); 
    });
  }
  
  public void updateFields(){
    for (Map.Entry<String, JTextField> entry : fieldTextFields.entrySet()) {
        String fieldName = entry.getKey();
        try {
            Field field = configObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldValue = field.get(configObject);
            entry.getValue().setText(""+fieldValue); // Update label text
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Error updating label for field: " + fieldName);
        }
    }
  }
  
  public GenericConfigGUI(AbstractJSONSynced configObject, String filePath) {
    this.configObject = configObject;
    this.filePath = filePath;
    frame = new JFrame("Configuration Editor 2.0");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 250);
    
    mainPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 0 rows (dynamic), 2 columns, with spacing
    GenericConfigGUI temp = this;
    JTextField inputfield = new JTextField();
    JButton addButton = new JButton("add");
    addButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
        addField(inputfield.getText());
        inputfield.setText("");
        temp.frame.setTitle("random");
        //temp.update();
      }
    });
    mainPanel.add(inputfield);
    mainPanel.add(addButton);


    // Add button to save changes
    saveButton = new JButton("Save");
    saveButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
        temp.actionPerformed(e);
      }
    });
    mainPanel.add(saveButton);
    frame.getContentPane().add(mainPanel);
    frame.setVisible(true);
  }

  public void updateLists() {
      for (Map.Entry<String, JList> entry : listFields.entrySet()) {
          String fieldName = entry.getKey();
          try {
              Field field = configObject.getClass().getDeclaredField(fieldName);
              field.setAccessible(true);
              List<?> listValue = (List<?>) field.get(configObject);
              entry.getValue().setListData(listValue.toArray());
          } catch (NoSuchFieldException | IllegalAccessException e) {
              System.err.println("Error updating list for field: " + fieldName);
          }
      }
  }

  public void actionPerformed(ActionEvent e) {
    try {
      // Update fields in the configObject
      this.update();
      for (Map.Entry<String, JTextField> entry : fieldTextFields.entrySet()) {
        String fieldName = entry.getKey();
        Field field = configObject.getClass().getDeclaredField(fieldName);
        String fieldValueString = entry.getValue().getText(); // Extract value

        handleType(field, fieldValueString);

      }
      for (Map.Entry<String, JList> entry : listFields.entrySet()) {
          String fieldName = entry.getKey();
          Field field = configObject.getClass().getDeclaredField(fieldName);
          field.setAccessible(true);
          // Get selected values from the JList
          List<Object> selectedValues = new ArrayList<>();
          for (Object selectedItem : entry.getValue().getSelectedValuesList()) {
              selectedValues.add(selectedItem);
          }
          // Set the updated list in the configObject
          field.set(configObject, selectedValues);
      }
      Constants.syncInstance.saveJSONFile(this.filePath);

      JOptionPane.showMessageDialog(frame, "Configuration saved successfully!");
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numbers.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error saving configuration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public static GenericConfigGUI openGui(AbstractJSONSynced configObject, String filePath) {
    return new GenericConfigGUI(configObject, filePath);
  }
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try{
          Constants.syncInstance.loadJSONFile("src/main/resources/constants.json");
        }catch (Exception e){}
        Constants constants = Constants.getInstance();
        new GenericConfigGUI(constants, "src/main/resources/constants.json");
      }
    });
  }
  
}