# AspectJ-Reflection-GUI

A Proof-of-Concept project for Object Oriented Design course

##Dynamically load classes and recompile them with aspects using a GUI.

The program dynamically loads .class/.jar files onto Java's class path while bypassing the classloader security protocols preventing this behavior, using the SystemLoader.

The loaded classes are then displayed in a scrollable list using JTree, while each class containing child nodes that describe its Constructors, Fields & Methods.

**Dependencies:<br/>**
>The program requires the AspectJ Compiler (AJC) installed on the user's system and in the PATH environment variable.
