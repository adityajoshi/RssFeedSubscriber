package xyz.adityajoshi.RssFeedSubscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.adityajoshi.RssFeedSubscriber.service.OracleDataService;

@SpringBootApplication
public class RssFeedSubscriberApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(RssFeedSubscriberApplication.class);

	@Autowired
	OracleDataService service;

	public static void main(String[] args) {
		SpringApplication.run(RssFeedSubscriberApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length < 2) {
			logger.error("Usage: java -jar rss-subscriber.jar <Feed Name> <Feed URL> [Category]");
			return;
		}

		String feedName = args[0];
		String feedUrl = args[1];
		String category = args.length > 2 ? args[2] : null;

		logger.info("Received feed details - Name: {}, URL: {}, Category: {}", feedName, feedUrl, category);
		service.saveFeedSource(feedName, feedUrl, category);
	}
}
