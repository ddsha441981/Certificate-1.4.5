<p align="center"><strong>Backend Setup:</strong></p>
Step 1: Clone the project from GitHub.


![Step 1](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step%201.png)

Step 2: Open your favorite IDE for the backend (STS, IntelliJ IDEA) and for the frontend (VS Code, WebStorm).

Step 3: We'll set up the backend project first. Open your IDE and either clone the project or open the cloned folder.

Step 4: Before starting the backend, there are some configurations needed, such as adding your SonarQube token in the pom.xml file. First, download SonarQube from [Download SonarQube](https://www.sonarsource.com/products/sonarqube/downloads/historical-downloads/) After downloading, we need to have [OpenJDK 17 Download](https://adoptium.net/en-GB/temurin/archive/?version=17) installed to run SonarQube.

![Step 4](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step_4.png)

After downloading OpenJDK and SonarQube, run SonarQube in the background by starting the SonarQube service (which varies depending on your operating system: macOS, Linux, or Windows)

![Step 4](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/sonar.png)

Once the services have started successfully, open the terminal and execute the following command to compile the code: ./mvnw install sonar:sonar.

![Step 4](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/sonar1.png)

After successfully compiling the code...

![Step 4](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/sonarcompile.png)

Go to your browser and open the URL http://localhost:9000

![Step 4](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/sonarlogin.png)

And finally, access the SonarQube dashboard to view the analysis results and project metrics

![Step 4](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/sonardashbard.png)

Step 5: Create a database in PostgreSQL.

Step 6: Now, start the application.

<p align="center"><strong>Frontend Setup:</strong></p>

Step 7: Now we will set up the frontend application.

Step 8: Before starting the application, we need to download npm packages. Open the terminal and run the command
``npm install``

Step 9: Once ``npm install`` completes successfully, we can start the application on port 3000.

<p align="center"><strong>App Script Setup:</strong></p>

Step 10: Now, we need to integrate our app script with [Google App Script](https://www.google.com/script/start/) Create a specific file name, open it for anyone, deploy it, copy the deployed script, paste it into the frontend application, and update it accordingly.
![Step 10](https://github.com/ddsha441981/Chandrayans3/blob/master/snaps/step_10.png)

![Step 101](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step_10_1.png)

![Step 102](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step_10_2.png)

![Step 103](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step_10_3.png)

Step 11: Next, add your documents with their particular IDs using this URL format: https://docs.google.com/document/d/1lK3u7KQRkb4BTpQ9J_z8HMcHVqywVAZFxgUOkv3dUUio/edit. We only need the document ID "1lK3u7KQRkbBTpQ9J_z8HMcHVqywVAZFxg34UOkv3dUUio".

![Step 11](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step_11.png)

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step_11_1.png)

Step 12: Now, create a candidate profile with their appropriate details, including selecting the company name.

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step_12.png)

Step 13: Afterward, you can generate documents by clicking on the generate button. The generation process may take 5 to 10 seconds, depending on the number of documents and internet connectivity.

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step12_1.png)

Step 14: After successfully generating the documents, navigate to your Google Drive and check the documents in the "PDF Documents" folder.

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step14.png)

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step14_1.png)

Step 15: Alternatively, you can check your email where all documents are attached as attachments.

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step15.png)

<p align="center"><strong>Using Docker</strong></p>

The command to run in the command prompt (cmd) is:
```docker-compose up --build```
This command is used to build and start containers defined in the docker-compose.yml file.

<p align="center"><strong>For Backend</strong></p>

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step16.png)

<p align="center"><strong>For Frontend</strong></p>

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step16_1.png)

<p align="center"><strong>Swagger</strong></p>

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/step16_2.png)

You can track application log files located inside

Windows: C:\Users\username\Certificate-Logs 

MacOS: /Users/username/Certificate-Logs

Linux: /home/username/Certificate-Logs

![Step 111](https://github.com/ddsha441981/Certificate-Generation/blob/main/snapshoots/logs.png)
