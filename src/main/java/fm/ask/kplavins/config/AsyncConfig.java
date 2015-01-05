package fm.ask.kplavins.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * We will use async calls and scheduled invokes
 *
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {
    // all done with annotations, for pool size and other configs we'll use
    // defaults
}
