AutoMeta
========

A Semantic Annotation Tool

see more http://code.google.com/p/autometa/
contact : celsowm at gmail dot com

GUI mode (windows, linux, ios)

- unrar "dist" folder
- just open the autometa.jar (requires java)


CLI mode

java AutoMeta2.jar with parameters:

-ontology: to indicate the full path and name of the ontology in RDF, OWL or N-TRIPLE;

-documentpath: to indicate the directory where you have a set of input documents;

-document: the name of a document that will contain the target annotation. .

-outputpath: the directory where is (are) stored (s) number (s) document (s) annotated (s);

-reasoning (optional): refers to the use of the reasoner (on by default "true") or just the regular ontology parser (value "false").

-exhaustive (optional): annotate all occurrences of a term (true) or only one (false)

-reasoner (optional): pellet or hermit

Example: java -Xms1g -jar Autometa2.jar -ontology "c:\ontololy.owl" -documentpath "c:\txts" -outpath "c:\rdfas" 
