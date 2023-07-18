## How to Run
To run this app, you need java 17 installed, and Gradle 7.2. You will also need to create a `.env` file according to the `.env.example` in this repository. The Google API key is optional, however, you must specify a selenium web driver url.

### Docker (Manual Method)
Docker can be used to achieve this:
`docker run -it --rm -v .:/home/gradle/ -w /home/gradle gradle:7.2-jdk17 gradle run --console=plain`

Before running the command above, you will need to start the selenium remote web driver:
`docker run --rm -d -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-firefox:latest`

### Docker (Compose Method)
To start the app, run `docker compose up -d` then connect to the java/gradle container with `docker run -it container_name bash`.

You can then run the app with `gradle run --console=plain` or you can build it first with `gradle jar` and run the file directly with `java -jar my_jar_file`. Depending on your terminal, the jar file may run better.

> Running from the jar file is recommended for production, whereas running through gradle is easiest for development purposes.


## How to Build
Create a jar file:
`docker run --rm -v .:/home/gradle/ -w /home/gradle gradle:7.2-jdk17 gradle jar`
The file will be stored in `app/build/libs/app.jar`

Run the jar file with:
`docker run -it --rm -v .:/home/gradle/ -w /home/gradle gradle:7.2-jdk17 java -jar app/build/libs/app.jar`



