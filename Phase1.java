import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Stack;
public class Phase1{
    // Data members

    // the XML file itself as a BufferedReader
    public BufferedReader xml_file;

    // the XML file as a single string with no spaces
    public String xml_text = "";

    // ArrayList of the XML file line by line
    public ArrayList<String> lines = new ArrayList<String>();

    // ArrayList of the XML file line by line
    public ArrayList<String> opening_tags = new ArrayList<String>();
    public ArrayList<String> all_tags = new ArrayList<String>();

    public ArrayList<String> values = new ArrayList<String>();

    // ArrayList of all errors
    public ArrayList<String> errors = new ArrayList<String>();

    // Stack
    Stack<String> stack = new Stack<String>();

    // Constructor
    public Phase1(BufferedReader xml_file) {
        this.xml_file = xml_file;
        fileReader();
        xml_parser();
    }

    // Getters
    public String getXml_text() {
        return xml_text;
    }

    // Reading the xml_file into the ArrayList (lines)
    public void fileReader() {
        try {
            String line;
            while ((line = xml_file.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error in file reading");
            return;
        }
    }

    public void xml_parser() {
        boolean value = false;
        for (int lineindx = 0; lineindx < lines.size(); lineindx++) {
            for (int i = 0; i < lines.get(lineindx).length(); i++) {
                if (lines.get(lineindx).charAt(i) == '<') {
                    value = false;
                }

                else if (lines.get(lineindx).charAt(i) == '>') {
                    try {
                        if (lines.get(lineindx).charAt(i + 1) != ' ') {
                            value = true;
                        }
                    } catch (Exception e) {
                        System.out.print("");
                    }
                }

                if (!value && lines.get(lineindx).charAt(i) != ' ') {
                    xml_text += lines.get(lineindx).charAt(i);
                }

                else if (value) {
                    xml_text += lines.get(lineindx).charAt(i);
                }
            }
        }
    }

    // Validating XML, gettint the errors and filling xml_text
    public void xml_validator() {
        // Checking if there's (any) error
        boolean error = false;

        // iterating the ArrayList of the lines by index (by index to indicate the lines
        // with errors)
        for (int lineindx = 0; lineindx < lines.size(); lineindx++) {

            // ignoring spaces
            try {
                while (lines.get(lineindx).charAt(0) == ' ') {
                    lines.set(lineindx, lines.get(lineindx).substring(1));
                }
            } catch (Exception e) {
                System.out.print("");
            }

            // Neglect the declaration
            try {
                if (lines.get(lineindx).substring(0, 2).compareTo("<?") == 0) {
                    System.out.println("Version");
                }

                // Neglect the comments
                else if (lines.get(lineindx).substring(0, 2).compareTo("<!") == 0) {
                    System.out.println("Comment");
                }

                else {
                    // By default: the text is not (opening nor closing) tag
                    boolean readingopen = false;
                    boolean readingclose = false;

                    // initializing the tag with empty value
                    String tag = "";
                    String value = "";

                    // iterating through the chars of a single line
                    for (int i = 0; i < lines.get(lineindx).length(); i++) {

                        // checking if it's a/n (oepning or closing) tag
                        if (lines.get(lineindx).charAt(i) == '<') {
                            if (value != "") {
                                values.add(value);
                            }
                            value = "";

                            // check if it's a closing tag
                            if (lines.get(lineindx).charAt(i + 1) == '/') {
                                i++;
                                readingclose = true;
                                readingopen = false;

                                // resetting the value to empty

                                tag = "";
                            }

                            // check if it's an opening tag
                            else {
                                readingopen = true;
                                readingclose = false;

                                tag = "";
                            }
                        }
                        // check the end of a tag (opening or closing)
                        else if (lines.get(lineindx).charAt(i) == '>') {

                            // if it's the end of an opening tag: push
                            if (readingopen) {
                                stack.push(tag);
                                opening_tags.add(tag);
                                all_tags.add("<" + tag + ">");
                                // xml_text += ("<" + tag + ">");
                                System.out.println(tag + " is pushed!");
                            }

                            // if it's the end of a closing tag: pop
                            else if (readingclose) {

                                // Checking of the stack is empty: ERROR!
                                if (stack.empty()) {
                                    errors.add("line: " + lineindx + ": stack is empty! the tag " + tag
                                            + " has no opening tag!");
                                    error = true;
                                }
                                // Checking if the stack has the correspoding opening tag : no error
                                else if (stack.peek().compareTo(tag) == 0) {
                                    stack.pop();
                                    all_tags.add("</" + tag + ">");
                                    System.out.println(tag + " is popped!");
                                }
                                // Checking if the stack doesn't have to correspoding opening tag: ERROR!
                                else {
                                    errors.add("line " + lineindx + ": " + tag
                                            + " is here but stack.top == " + stack.peek());
                                    error = true;
                                }
                            }
                            readingopen = false;
                            readingclose = false;
                        }
                        // If it's a tag: write into tag
                        else if (readingopen || readingclose) {
                            tag += lines.get(lineindx).charAt(i);
                        }
                        // If it's a value: write it into xml_text
                        else {
                            value += lines.get(lineindx).charAt(i);
                            // System.out.println("DONE");
                            // xml_text += lines.get(lineindx).charAt(i);
                        }
                    }
                }
            }

            catch (Exception e) {
                System.out.print("");
            }
        }
        // If the stack isn't empty: print the tag that hasn't been closed
        if (!stack.empty()) {
            System.out.println("The tag " + stack.peek() + " hasn't been closed");
        }
        // If there're any errors: print them
        else if (error) {
            for (String errorinerrors : errors) {
                System.out.println(errorinerrors);
            }
        }
        // If not
        else {
            System.out.println("No Errors 👍");
        }
    }

    public String formatting() {
        // Initializing of an empty string
        String formatted = "";

        // Initializing the indentation level
        int level = 0;

        // Iterating through the XML text string (with no spaces)
        for (int i = 0; i < xml_text.length(); i++) {
            // checking closing tag
            if (xml_text.charAt(i) == '<' && xml_text.charAt(i + 1) == '/') {
                // checking if the previous word was a tag
                if (xml_text.charAt(i - 1) == '>') {
                    // decreasing the indentation level
                    level--;
                    // indenting with the level's value
                    for (int j = 0; j < level; j++) {
                        formatted += "\t";
                    }
                }
                // adding the tag to the formatted string
                while (xml_text.charAt(i) != '>') {
                    formatted += xml_text.charAt(i);
                    i++;
                }
                // adding the ">"
                formatted += xml_text.charAt(i);
                // New line
                formatted += "\n";
            }

            // Checking opening tag
            else if (xml_text.charAt(i) == '<') {
                // indenting with the level's value
                for (int j = 0; j < level; j++) {
                    formatted += "\t";
                }
                // adding the tag to the formatted string
                while (xml_text.charAt(i) != '>') {
                    formatted += xml_text.charAt(i);
                    i++;
                }
                // adding the ">"
                formatted += xml_text.charAt(i);
                // checking if the following word is a tag
                if (xml_text.charAt(i + 1) == '<') {
                    // increasing the indentation level
                    level++;
                    // newline
                    formatted += "\n";
                }
            }
            // not a tag, just add it
            else {
                formatted += xml_text.charAt(i);
            }
        }
        return formatted;
    }

    public String Jsonifier() {
        ArrayList<String> lists = new ArrayList<String>();

        String JSON = "";
        String tag = "";
        String bracket = "";
        int level = 0;

        for (int i = 0; i < all_tags.size() - 1; i++) {
            if (all_tags.get(i).charAt(1) == '/') {
                if (all_tags.get(i).substring(2).compareTo(all_tags.get(i + 1).substring(1)) == 0) {
                    lists.add(all_tags.get(i).substring(2, all_tags.get(i).length() - 1));
                }
            }
        }

        for (int i = 0; i < xml_text.length(); i++) {
            if (xml_text.charAt(i) == '<' && xml_text.charAt(i + 1) == '/') {
                continue;

            } else if (xml_text.charAt(i) == '<') {
                i++;
                while (xml_text.charAt(i) != '>') {
                    tag += xml_text.charAt(i);
                    i++;
                }
                if (lists.contains(tag)) {
                    JSON += "\"" + tag + "\" " + "[\n";
                    bracket = "]";
                } else {
                    JSON += "\"" + tag + "\" " + "{\n";
                    bracket = "}";
                }
                level++;

            }

        }

        return "";

    }
}
