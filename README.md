# RSS Feed Subscriber

## Overview
RSS Feed Subscriber is a Spring Boot command-line application that allows users to save RSS feed details into an Oracle database. 
The application takes feed details as input arguments and inserts them into the `FEED_SOURCE` table.

## Features
- Accepts feed name, feed URL, and optional category as input arguments.
- Saves feed details into an Oracle database.
- Logs important events such as successful insertions and errors.
- Lightweight and easy to use.

## Prerequisites
- Java 17+
- Maven 3.6+
- Oracle Database with the `FEED_SOURCE` table created
- `application.properties` configured with database credentials

## Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/adityajoshi/RssFeedSubscriber.git
   cd RssFeedSubscriber
   ```

2. Update `src/main/resources/application.properties` with your Oracle database credentials:
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@your-database-url:1521:xe
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
   ```

3. Build the application:
   ```sh
   mvn clean package
   ```

## Running the Application
Execute the following command to run the application:
```sh
java -jar target/rss-subscriber.jar "Tech News" "https://example.com/rss" "Technology"
```

### Example
To save a feed:
```sh
java -jar target/RssFeedSubscriber-0.0.1-SNAPSHOT.jar "Daily Updates" "https://news.example.com/rss" "News"
```

If the category is not provided, it will be saved as `NULL` in the database.

## Logging
The application logs all events, including successful feed insertions and errors, using SLF4J. Logs can be found in the console output.

## Next Steps
- Add unit tests for better reliability.
- Implement a REST API for managing feeds.
- Improve error handling for better resilience.

## License
This project is open-source and available under the ISC License.


