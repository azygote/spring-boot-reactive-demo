package org.gty.demo.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class SystemConstants {

    public static final ZoneId defaultTimeZone = ZoneId.systemDefault();
    public static final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZZZ");

    private SystemConstants() {
    }
}
