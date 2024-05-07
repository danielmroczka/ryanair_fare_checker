package com.dm.labs.ryanairwebscrapper.service;

import com.dm.labs.ryanairwebscrapper.entity.Fare;
import com.dm.labs.ryanairwebscrapper.entity.Task;
import com.dm.labs.ryanairwebscrapper.entity.Trip;
import com.dm.labs.ryanairwebscrapper.repository.TaskRepository;
import com.dm.labs.ryanairwebscrapper.service.command.FareCommandService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Service
@Validated
public class TaskService {

    private final FareCommandService fareCommandService;
    private final TaskRepository repository;
    private List<ScheduledFuture> schedule = new ArrayList<>();
    private ThreadPoolTaskScheduler executor;

    public TaskService(FareCommandService fareCommandService, TaskRepository repository, ThreadPoolTaskScheduler executor) {
        this.fareCommandService = fareCommandService;
        this.repository = repository;
        this.executor = executor;
    }

    @PostConstruct
    public void postConstruct() {
        scheduling();
    }

    public Task addNewTask(@Valid Task task) {
        var res = repository.save(task);
        scheduleTask(task);
        return res;
    }

    private void onTaskChange(Long id) {
        // Not implemented yet
    }

    public Optional<Task> readTask(Long id) {
        return repository.findById(id);
    }

    public void deleteTask(Long id) {
        repository.deleteById(id);
    }

    public List<Task> readAllTask() {
        return repository.findAll();
    }

    private void scheduling() {
        System.out.println("Scheduling...");

        List<Task> tasks = repository.findAll();
        System.out.println("Found " + tasks.size() + " task(s)");
        for (Task task : tasks) {
            scheduleTask(task);
        }
    }

    private void scheduleTask(Task task) {
        final Runnable runnable = () -> {
            LocalDate now = LocalDate.now();
            List<com.dm.labs.ryanairwebscrapper.clientmodel.Fare> latest = new ArrayList<>();
            List<Fare> cached = new ArrayList<>();
            for (int i = 0; i <= task.getMonthsAhead(); i++) {
                String query = now.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM"));
                var res = fareCommandService.fareByMonth(task.getOrigin(), task.getDestination(), query);
                latest.addAll(res);

                var cache = fareCommandService.caches(task.getOrigin(), task.getDestination(), query);
                for (Trip trip : cache) {
                    Fare f = trip.getFares().getLast();
                    cached.add(f);
                }
//                cache.forEach(trip -> {
//                    cached.add(trip.getFares().stream().max((o1, o2) -> Math.toIntExact(o1.getId() - o2.getId())).get());
//                                });
                //cached.addAll(cache.l);
            }
            System.out.println(latest);
            System.out.println(cached);
        };

        schedule.add(executor.scheduleAtFixedRate(runnable, Instant.ofEpochSecond(1), Duration.of(task.getIntervals(), ChronoUnit.SECONDS)));

    }
}
