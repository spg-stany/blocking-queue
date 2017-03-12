package ru.project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class Test1 {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskService taskService;

    @Test
    public void add() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        taskService.add(now.plusSeconds(10), new Callable() {
            public Object call() throws Exception {
                log.info("third");
                return null;
            }
        });
        taskService.add(now.plusSeconds(5), new Callable() {
            public Object call() throws Exception {
                log.info("second");
                return null;
            }
        });
        taskService.add(now.plusSeconds(3), new Callable() {
            public Object call() throws Exception {
                log.info("first");
                return null;
            }
        });

        taskService.process();
        Thread.sleep(20000);
        taskService.stop();
    }
}
