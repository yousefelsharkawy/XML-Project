/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication0;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.swing.JFileChooser;

/**
 *
 * @author yousef
 */
public class FXMLDocumentController implements Initializable {

    List<String> lstfile;
    private Label label;
    private VBox keypad;
    private PasswordField display;
    @FXML
    private Button openFile;
    @FXML
    TextArea textarea;
    static File f;
    private BufferedReader br;
    @FXML
    private TextArea terminalarea;
    String file = "";
    File newfile;
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lstfile = new ArrayList<>();
        lstfile.add("*.xml");
    }    


    @FXML
    private void openfile(ActionEvent event) throws FileNotFoundException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                f = fileChooser.getSelectedFile();
                br = new BufferedReader(new FileReader(f.getAbsolutePath()));
                String line;
                
                while((line = br.readLine()) != null){
                    file += line;
                    file += "\n";
             }
             br.close();
             br = new BufferedReader(new FileReader(f.getAbsolutePath()));
             textarea.setText(file);
             terminalarea.setText("File opened Successfuly!");
             
            
                
                
            }
//        if(f != null){
//           BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
//           String line = br.readLine();
//           while(line != null){
//               line = line+br.readLine();
//           }
//           textarea.setText(line);
//            
//        }
    }

    @FXML
    private void writeFile(ActionEvent event) {
        try {
             FileWriter writer = new FileWriter(f.getAbsolutePath(), false);
             String new_content = textarea.getText();
             writer.write(new_content);
             writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }  
        terminalarea.setText("File Saved Successfully!");
    }

    @FXML
    private void DetectErrors(ActionEvent event) throws IOException {
        
       Phase1_1 p1 = new Phase1_1(br);
       terminalarea.setText(p1.allerrors);
       br.close();
       br = new BufferedReader(new FileReader(f.getAbsolutePath()));
       
       
       
      
    }

    @FXML
    private void ReturnFormatted(ActionEvent event) throws IOException {
        Phase1_1 p1 = new Phase1_1(br);
        textarea.setText(p1.formatted);
        br.close();
        br = new BufferedReader(new FileReader(f.getAbsolutePath()));
        
       
    }

    @FXML
    private void ReturnJsonified(ActionEvent event) throws IOException {
        Phase1_1 p1 = new Phase1_1(br);
        textarea.setText(p1.JSONified);
        br.close();
        br = new BufferedReader(new FileReader(f.getAbsolutePath()));
    }


    @FXML
    private void compress(ActionEvent event) {
        String output = lzw.run(file);
        output += "Compressed File Saved Successfully!";
        terminalarea.setText(output);
        
        
        
        
    }
    
    
}
