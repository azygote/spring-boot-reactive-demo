package org.gty.demo.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@OrderedAppInitializer
@Order(OrderedAppInitializer.ORDER_WAIT_FIVE_SECONDS)
public class WaitFiveSecondsAppInitializer implements AppInitializer {

    private static final Logger log = LoggerFactory.getLogger(WaitFiveSecondsAppInitializer.class);

    @Override
    public void onAppInitialize() {
        log.debug("Start waiting for 5 seconds");
        try {
            Thread.sleep(5_000L);
        } catch (final InterruptedException ignored) {
        } finally {
            log.debug("Waiting for 5 seconds finished");
        }
    }
}
