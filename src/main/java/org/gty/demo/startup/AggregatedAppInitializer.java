package org.gty.demo.startup;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

@Service
public final class AggregatedAppInitializer implements AppInitializer {

    private final List<AppInitializer> appInitializers;
    private final Scheduler scheduler;

    public AggregatedAppInitializer(@Nonnull @OrderedAppInitializer final List<AppInitializer> appInitializers,
                                    @Nonnull final Scheduler scheduler) {
        this.appInitializers =
            Objects.requireNonNull(appInitializers, "appInitializers must not be null");
        this.scheduler =
            Objects.requireNonNull(scheduler, "scheduler must not be null");
    }

    @Override
    public void onAppInitialize() {
        Flux.fromIterable(appInitializers)
            .flatMap(appInitializer ->
                Mono.fromRunnable(appInitializer::onAppInitialize)
                    .subscribeOn(scheduler)
            )
            .subscribeOn(scheduler)
            .subscribe();
    }
}
