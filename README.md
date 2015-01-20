Fides Storage
======
The goal of our project is to create a proof of concept secure cloud storage.
To guarantee the safety of the user's files, all files are encrypted on the client side. Because the files are encrypted locally, and the key is only accessible on the user's side, the user can safely put the files on an externally hosted server.

Fides Storage Components
======
The components contains code which is shared with the client and the server. The components are needed for the client and server in order to work properly.

Installation requirements for development
======
In order to get the software running, you need the following requirements:

* Atleast Java JRE 7
* Maven
* Maven plugin (Maven integration for Eclipse)
* Eclipse

Checkout all three Github repositories from Github.com/Fides-Storage/ on your prefered location. Open Eclipse and import 'Existing Maven Projects' and open the folder where all three repositories are cloned to and import them.

Installation guide for building packages
======
In order to build an executable jar for both the client and the server, first you need to build the components package with the following command:
`mvn install` in the components repository. The next step is to package the client and the server. While in the concerning repository fill out the following command: `mvn package`. This will result into two different jar files in the target map of the current repository, please use the jar-with-dependancies.jar. 

Checkstyle and Formatter
======
Our project makes use of checkstyle and a formatter. Checkstyle is a plugin which can be downloaded from the Marketplace within Eclipse. After you've downloaded Checkstyle, you have to activate it by right clicking on the concerning project and then left click on Checkstyle -> Activate Checkstyle. To use the right formatter, right click on the project and search for 'Formatter'. Check the 'Enable project specific settings'. You can click on import where you have to navigate to the etc folder of the concerning folder and choose Formatter.xml. After you've done that you can apply the changes and search for 'Save Actions' (optional). Here you have to select 'Enable project specific settings', 'Perform the selected actions on save' and 'Format source code'.
