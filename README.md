# Santorini Board Game - Sprint 3

## Project Description
This project implements a playable prototype of the Santorini board game using object-oriented design principles. 
It includes support for two players, a 5x5 flexible board, and 3 God cards: Artemis, Demeter and Triton.

## Technologies Used
- Programming Language: Java
- UI Framework: Java Swing
- Coding Style Guide: Google Java Style Guide (https://google.github.io/styleguide/javaguide.html)

## Key Functionalities Implemented from Sprint 2 (Group project)
- Dynamic 5x5 board setup with randomised worker and God card assignments
- Worker movement with full rule validation 
- Level construction with validation 
- Turn switching logic
- Win condition check 

## New Extensions Implemented for Sprint 3(Individual Project)
- Triton god card - automatic extra move when on a perimeter cell
- Timer implementation - player automatically wins when opponent runs out of time.
- Introduction of a buy phase
- Introduction of tokes - game currency
- Purchasable artifacts to gain strategic advantage
- Artifact usage with full validation

## Design Artifacts
- `UML Class Diagram FIT3077`: UML class diagram showing system architecture

# Build instructions
These build instructions are for both Windows and Mac. As Mac does not have support for a windows style executable, the Mac build instructions end at step 3.
## 1. Get Ready
- Make sure you have Java Development Kit (JDK) installed
- Verify that you have Java installed by running:
    ```sh
  java -version
  ```

## 2A. How to Compile to `.jar` using Intellij
1. Navigate to **File > Project Structure > Artifacts** (make sure your source folder is marked as 'Sources').
2. Click the `+` button and select **JAR > From modules with dependencies**.
3. Select your main module
4. Click **OK**,then apply changes.
5. Build the JAR by navigating to **Build > Build Artifacts**, select your artifact and click **Build**.

## 2B. How to Compile to `.jar` without Intellij
1. Open a terminal and navigate to the **parent of the src directory**.
2. Run the following commands in the directory:
``` bash
$files = Get-ChildItem -Recurse -Filter *.java -Path .\src | ForEach-Object { $_.FullName }
javac --release 22 -d out $files
Copy-Item -Recurse -Path .\src\assets -Destination .\out\
jar cfm SantoriniGame.jar manifest.txt -C out .
```

## 3. Run the JAR File
Execute the packaged JAR file by using:
```sh
java -jar SantoriniGame.jar
```
in the directory containt the .jar

## 3. How to convert `.jar` file to `.exe` file
### This method uses `launch4j`, download before proceeding
- **Run** the application when you have downloaded it
- **In the fields:**
  - **Output file:**  the path where you want to generate the exe file `(e.g. Santorini.exe)`
  - **Jar file:** the jar file `(Santorini.jar)`
  - **Icon (Optional):** upload desired icon for the exe file
- Click on the `Build Wrapper (gear icon)` button

## 4. Run the `.exe` file
- In your Command Prompt, enter:

```sh
cd path\to\your\exe
.\Santorini.exe
```
Or **run** the exectuable directly!