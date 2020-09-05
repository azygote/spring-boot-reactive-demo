package org.gty.demo.startup;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public final class StartupRunner {

    private final AppInitializer aggregatedAppInitializer;

    public StartupRunner(@Nonnull final AppInitializer aggregatedAppInitializer) {
        this.aggregatedAppInitializer =
            Objects.requireNonNull(aggregatedAppInitializer, "aggregatedAppInitializer must not be null");
    }

    @EventListener
    public void onReady(final ApplicationReadyEvent event) {
        aggregatedAppInitializer.onAppInitialize();
    }
}
