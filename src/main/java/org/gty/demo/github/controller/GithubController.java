package org.gty.demo.github.controller;

import org.gty.demo.github.model.Contributor;
import org.gty.demo.github.model.vo.OwnerAndRepo;
import org.gty.demo.github.service.ReactiveGithubService;
import org.gty.demo.model.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

@RestController
public class GithubController {

    private static final Logger log = LoggerFactory.getLogger(GithubController.class);

    private static final String SPRING_PROJECTS = "spring-projects";
    private static final String SPRING_BOOT = "spring-boot";
    private static final String VUE_JS = "vuejs";
    private static final String VUE = "vue";

    private static final OwnerAndRepo springBoot = new OwnerAndRepo(SPRING_PROJECTS, SPRING_BOOT);
    private static final OwnerAndRepo vue = new OwnerAndRepo(VUE_JS, VUE);

    private static final List<OwnerAndRepo> ownerAndRepoList = List.of(springBoot, vue);

    private final ReactiveGithubService githubService;
    private final Scheduler scheduler;

    public GithubController(@Nonnull final Scheduler scheduler,
                            @Nonnull final ReactiveGithubService githubService) {
        this.githubService = Objects.requireNonNull(githubService, "githubService must not be null");
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler must not be null");
    }

    @Nonnull
    @GetMapping(value = "/api/github", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseVo<List<Contributor>>> query() {
        return Flux.fromIterable(ownerAndRepoList)
            .flatMap(ownerAndRepo -> githubService.contributors(ownerAndRepo.getOwner(), ownerAndRepo.getRepo()))
            .flatMap(Flux::fromIterable)
            .collectList()
            .map(ResponseVo::success)
            .subscribeOn(scheduler);
    }
}
