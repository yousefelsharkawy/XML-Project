# XML-Project
This is Project is the Semester Project for (Data Structures and Algorithms | CSE331s)
## Background
In the first phase of this project, we are going to develop a tool that allows the user to check if the XML file is valid, and if it is. He will be able to format it to increase the readability and to be more informative. Furthermore, many languages do not have XML parsers as perfect as JSON parsers, so the user is able to convert the XML file to a JSON file.

For more complex and bigger relationships. Many XML files may be used, and the files size may increase too, which consumes data and time. So, we have provided the user with the ability to compress the file and reduce its size.

## Implementation Details
### 1. Reading and Parcing
Through the GUI, the user is able to select the path of the XML file from his hard disk. Once he clicks the file, it will appear in the text area

What happens under the hood?  The once the file path is chosen. It will be stored into a **Buffered Reader** object, which is passed immediately to [fileReader](https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#L67) function, which converts the buffered reader into an **ArrayList** of strings, with each element corresponds to a line in the XML file.

In order to increase the performance of the operations and simplify the next functions. we stored the contents of the **ArrayList** into a single String, with no spaces, tabs nor new lines using [xml_parser](https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#L80).

![Reding and Parsing the file](https://github.com/0ssamaak0/XML-Project/blob/main/Reding_and_Parsing_the_file.png)

### 2.	The XML Tree
Having the XML file in string format, now we convert it to a **Tree** to make the best use of it.

Our approach was to generalize the tree node as far as we can, to handle any unpredicted cases. Make more flexibility in the following functions, even adding more features to it.
In terms of data members each node is as shown in the figure below:

![Tree node](https://github.com/0ssamaak0/XML-Project/blob/main/Tree_node.png)

The tree is also provided with method to add a new node as a child to a specific parent. And since we have way to access the siblings directly, we made a function that goes back to the parent, and get that parent’s children, including of course the node itself

### 3.Validation
Since we always need to check that every tag must match the last opening tag, we have chosen to use a **Stack**. Where the top of the stack is the most recent opening tag. If we find its closing tag. We pop it and proceed to the next one .

If the closing tag doesn’t match the current top of the stack. We add the error in detail to the **Errors ArrayList** with the index of the line caused the problem.

After the parsing is completed, we check some cases, like if the stack is still not empty if no errors and none of these cases occurred. We inform the user that the file is a valid XML file. And he is able to perform the next operations

![Validating the XML file](https://github.com/0ssamaak0/XML-Project/blob/main/Validating_the_XML_file.png)

### 4. Formatting
The idea behind the formatting is quite similar to the idea behind the validation. since every node when constructed is taking its parent’s (depth + 1). We used this depth to iterate through a loop and indent every tag. 

We have preferred using a behavior similar to the **PreOrderTraversal** to add all the nodes to the **Formatted** Srting, and to achieve more abstraction in our code, we have made a function that handles the XML declaration if any and call our traversal function on the root of our tree.

### 5. Convering to JSON
Converting the XML file to JSON is somehow easy process in most cases. We replace the tag with double quotes, and we add the column and the brackets. But the main issue is if we had a JSON lists? How can we detect if the current node is the beginning of a series of similar nodes?

This led us to start modifying or Formatting traversal function. And reusing it with many details and parameters. In this function we really started using the Tree data members which may seemed to be trivial in the first look.


```Java
// Pseudocode to check the 3 cases for each node
if this node has siblings with similar names:
    if this is the first child of the parent:
        print(tag + [:) // e.g., "user:["
    else:
    "don't print the tag name"
else:
    print(tag + {}:) // e.g., "user:{"
```

and for every node, we check also if it’s a leaf or not, to determine whether we just print it with its value or open a new curly bracket.
Lastly, we checked if this node is the last sibling. To avoid adding the comma “,” after it.

### 6. Compression
The idea of the **Compression** algorithm is the following: as the input data is being processed, a dictionary keeps a correspondence between the longest encountered words and a list of code values. The words are replaced by their corresponding codes and so the input file is compressed. Therefore, the efficiency of the algorithm increases as the number of long, repetitive words in the input data increases

```Java
//  LZW ENCODING PSEUDOCODE
Initialize table with single character strings
P = first input character
WHILE not end of input stream
    C = next input character
    IF P + C is in the string table
        P = P + C
    ELSE
        output the code for P
    add P + C to the string table
    P = C
    END WHILE
output code for P 
```

Considering the **Decompression** process The LZW decompressor creates the same string table during decompression. It starts with the first 256 table entries initialized to single characters. The string table is updated for each character in the input stream, except the first one. Decoding is achieved by reading codes and translating them through the code table being built.

```Java
// LZW DECODING PSEUDOCODE
Initialize table with single character strings
OLD = first input code
output translation of OLD
WHILE not end of input stream
    NEW = next input code
    IF NEW is not in the string table
            S = translation of OLD
            S = S + C
    ELSE
            S = translation of NEW
    output S
    C = first character of S
    OLD + C to the string table
    OLD = NEW
END WHILE
```
## Complexity Analysis
Where n is the number of lines in the XML file, and m is the number of letters per line.

[fileReader](https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#L67): T=O(n)

[xml_parser](https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#L80): T=O(nm)

[xml_validator](https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#/132):T=O(nm)

[tree_creator](https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#L266): T=O(n2)   

[formatting_maker](https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#L388): T=O(nodes) (preorder traversal)

[JSONIfy]((https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#L442)): T=O(nodes) (preorder traversal)

compress(String XML_file): T=O(nm)

decompress(String input): T=O(nm)

## Known Issues
1. Adding an attribute to a tag corrupts the validation process. This can be solved by adding a condition to completely ignore the attribution and push only the tag name to the stack.

2. Adding a comment corrupts the converting to JSON process, since the comment is considered as a node. So, it adds 1 to the number of children. This can be solved by checking if there’s a comment, we can add an integer that indicates the number of comments under a given node.
