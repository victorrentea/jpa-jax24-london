package victor.training.performance.jpa;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SqlFragmentAlias;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableCaching
@Slf4j
@SpringBootApplication
public class JpaApp {
  public static void main(String[] args) {
    SpringApplication.run(JpaApp.class, args);
  }

  @SneakyThrows
  @GetMapping("external-call/{parentId}")
  public String externalCall(@PathVariable long parentId) {
    Thread.sleep(100);
    return "review for " + parentId;
  }

  @EventListener(ApplicationStartedEvent.class)
  public void onAppStarted() {
    log.info("Started🎉 👉 http://localhost:8080");
  }
}
