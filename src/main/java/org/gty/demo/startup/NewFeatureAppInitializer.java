package org.gty.demo.startup;

import org.gty.demo.constant.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@OrderedAppInitializer
@Order(OrderedAppInitializer.ORDER_NEW_FEATURE)
public final class NewFeatureAppInitializer implements AppInitializer {

    private static final Logger log = LoggerFactory.getLogger(NewFeatureAppInitializer.class);

    @Override
    public void onAppInitialize() {
        final var dateTime = ZonedDateTime.now(SystemConstants.defaultTimeZone);
        final var monthInfo = "This is " + switch (dateTime.getMonth()) {
            case JANUARY -> "JAN";
            case FEBRUARY -> "FEB";
            case MARCH -> "MAR";
            case APRIL -> "APR";
            case MAY -> "MAY";
            case JUNE -> "JUN";
            case JULY -> "JUL";
            case AUGUST -> "AUG";
            case SEPTEMBER -> "SEPT";
            case OCTOBER -> "OCT";
            case NOVEMBER -> "NOV";
            default -> "DEC";
        };

        log.info(monthInfo);
    }
}
