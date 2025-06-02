package com.dm.labs.ryanairwebscrapper.service;

import com.dm.labs.ryanairwebscrapper.RyanairWebscrapperApplication;
import com.dm.labs.ryanairwebscrapper.entity.Task;
import com.dm.labs.ryanairwebscrapper.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RyanairWebscrapperApplication.class)
class TaskServiceTest {

    @MockBean
    private TaskRepository repository;

    @Autowired
    private TaskService service;

    @Test
    void foo() {
        var task = new Task();
        var res = service.addNewTask(task);
        assertNull(res);
    }

    @Test
    void test() {
        var task = new Task();
        task.setOrigin("XXX");
        task.setDestination("YYY");

        service.addNewTask(task);

        var res = service.readAllTask();
        assertEquals(1, res.size());
    }
}