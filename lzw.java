/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication0;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import static javafxapplication0.FXMLDocumentController.f;

/**
 *
 * @author yousef
 */
public class lzw {
    private static String output = "";
    static File newfile;
    public lzw(){}

    public static String compress(String XML_file){
        // Creating an empty Dictionary
        HashMap<String,Integer> dict = new LinkedHashMap<>();
        //put each char in a string
        String[] charStream = (XML_file + "").split("");
        String compressedXML = "";
        ArrayList<String> temp = new ArrayList<>();
        
        String statement = charStream[0];
        String nextChar;
        
        
        int code = 256;
        for(int i=1; i<charStream.length;i++){
            nextChar = charStream[i];
            //if the next char is not found in dict, add it to the statement
            if(dict.get(statement+nextChar) != null){
                statement += nextChar;
            }
            //if the next char is found, add it to the temp Arraylist
            else{
                if(statement.length() > 1){
                    temp.add(Character.toString((char)dict.get(statement).intValue()));
                }
                else{
                    temp.add(Character.toString((char)Character.codePointAt(statement,0)));
                }

                dict.put(statement+nextChar,code);
                code++;
                statement = nextChar;
            }
        }

        if(statement.length() > 1){
            temp.add(Character.toString((char)dict.get(statement).intValue()));
        }
        else{
            temp.add(Character.toString((char)Character.codePointAt(statement,0)));
        }

        for(String outchar:temp){
            compressedXML+=outchar;
        }
        return compressedXML;
    }

    public static String decompress(String input){
        HashMap<Integer,String> dictionary = new LinkedHashMap<>();
        String[] charStream = (input + "").split("");
        String currentChar = charStream[0];
        String oldPhrase = currentChar;
        String out = currentChar;
        int code = 256;
        String statement="";
        for(int i=1;i<charStream.length;i++){
            int currCode = Character.codePointAt(charStream[i],0);
            if(currCode < 256){
                statement = charStream[i];
            }
            else{
                if(dictionary.get(currCode) != null){
                    statement = dictionary.get(currCode);
                }
                else{
                    statement = oldPhrase + currentChar;
                }
            }
            out+=statement;
            currentChar = statement.substring(0,1);
            dictionary.put(code,oldPhrase+currentChar);
            code++;
            oldPhrase = statement;
        }
        return out;
    }
    
     public static String run(String file){
        
         
        

        String OriginalString = file;

//        System.out.println("Original String-> "+OriginalString);

        System.out.println("Original String length -> "+OriginalString.length());
        output += "Original String length -> "+OriginalString.length() + "\n";
        

        String compressed = lzw.compress(OriginalString);

        System.out.println("Compressed String -> "+compressed);

        System.out.println("Compressed length String -> "+compressed.length());
        output += "Compressed length String -> "+compressed.length() + "\n";

        String decompressed = lzw.decompress(compressed);
        
//        System.out.println("Decompressed String -> "+decompressed);
        
        System.out.println("Efficiency = "+ (((double)OriginalString.length()-compressed.length())/OriginalString.length())*100 + "%");
        output += "Efficiency = "+ (((double)OriginalString.length()-compressed.length())/OriginalString.length())*100 + "%\n";
//        terminalarea.setText(output);
//        textarea.setText(output);
try {
            newfile = new File(f.getName());
        if (newfile.createNewFile()) {
        System.out.println("File created: " + newfile.getName());
        } else {
        System.out.println("File already exists.");
        }
        } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
        }
        
       
        try {
             FileWriter writer = new FileWriter(newfile.getAbsolutePath(), false);
             writer.write(compressed);
             writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return output;
        
    }
}
