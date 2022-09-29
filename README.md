# asyncio
Goal of this program is to chain the set of operations or steps in completable future and using Java's apache async NIO for REST API calls for faster execution of the whole process

The code uses a Executor Service with single thread to chain all the steps in completable future and uses separate thread for REST API calls for Async NIO. Callbacks of REST API would be processed in a completable future and this signals the next step / action to execute in the chain

By default, this code would run this for 20 devices to complete a set of activities which needs to be executed one by one. 

Pre-requisites :

 1. JAVA 8 and above
 2. Maven (3.6.3)

To execute the code via editor :

 1. Clone/Download the Repository
 2. Open the Project in your favourite editor. (Here, IntelliJ IDEA is being referred as an example)
 3. Open AsyncioApplication.java file and right click to select "Debug AsyncioApplication.java" and run


To execute the code via terminal :
 
 1. Clone/Download the Repository
 2. From the repository, run the following command 
    ```
     mvn clean install
     java -jar target/asyncio-0.0.1-SNAPSHOT.jar --num <number-of-devices>
    ```
    
    Note :
       mvn executable binary should be set in environment variable or provide its full path
       Above command should be run from the root directory of the project 
       

The result would be printed in the terminal or console of the editor 

