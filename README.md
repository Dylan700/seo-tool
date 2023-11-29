## What is The SEO Tool
The SEO Tool is a command line utility to perform SEO checks on a given webpage. Features include:
* Generating a Sitemap, ready to upload to Google Search Console
* Finding Broken Links
* Validating the Structure of Heading Tags
* Checking Images have alt tags
* Ensuring Good Internal Linking
* Performing Keyword Densitity and Readability Checks
* Validating the Meta Title and Description
* Checking Redirects
* Ensure Links are Secure
* Testing the Speed of the Webpage
* Reporting Mobile Friendliness
* And much more!

![seotool](https://github.com/Dylan700/seo-tool/assets/54528768/932774ae-29bb-4950-a55a-3e0f0a41e5cd)

## How to Run
To run this app, you need java 17 installed, and Gradle 7.2. You will also need to create a `.env` file according to the `.env.example` in this repository. The Google API key is optional, however, you must specify a selenium web driver url.

### Docker (Manual Method)
Docker can be used to achieve this:
`docker run -it --rm -v .:/home/gradle/ -w /home/gradle gradle:7.2-jdk17 gradle run --console=plain`

Before running the command above, you will need to start the selenium remote web driver:
`docker run --rm -d -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-firefox:latest`

### Docker (Compose Method)
To start the app, run `docker compose up -d` then connect to the java/gradle container with `docker exec -it container_name bash`.

You can then run the app with `gradle run --console=plain` or you can build it first with `gradle jar` and run the file directly with `java -jar my_jar_file`. Depending on your terminal, the jar file may run better.

> Running from the jar file is recommended for production, whereas running through gradle is easiest for development purposes.


## How to Build
Create a jar file:
`docker run --rm -v .:/home/gradle/ -w /home/gradle gradle:7.2-jdk17 gradle jar`
The file will be stored in `app/build/libs/app.jar`

Run the jar file with:
`docker run -it --rm -v .:/home/gradle/ -w /home/gradle gradle:7.2-jdk17 java -jar app/build/libs/app.jar`

## How Does it Work?
This tool uses Selenium to peform checks on given webpages. Functionalities such as the SSL Certificate check or Sitemap Generation use faster methods where applicable.
