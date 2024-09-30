package victor.training.performance.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import victor.training.performance.jpa.entity.Parent;
import victor.training.performance.jpa.repo.ParentRepo;

@Slf4j
@RequiredArgsConstructor
@RestController
@Transactional // this applies to all public methods below, when called from outside
public class TransactionalWaste {
  private final ParentRepo parentRepo;
  private final RestTemplate restTemplate = new RestTemplate();

  public record Response(String name,
                         String review) {
  }
  // or worse: locks held
  // 1) Table Lock (😱): SQL= LOCK TABLE JOB_IS_RUNNING
  // 2) Row Lock: SQL= SELECT * FROM PARENT WHERE ID = 13 FOR UPDATE
  @GetMapping("parent/{parentId}")
  public Response transactional(@PathVariable @DefaultValue("101") long parentId) {
    Parent parent = parentRepo.findById(parentId).orElseThrow();
    String review = restTemplate.getForObject("http://localhost:8080/external-call/" + parentId,String.class);
    return new Response(parent.getName(), review);
  }
}

// JDBC Connection Pool: to acquire it faster + limit the number of connections (max=10)
// for the duration of a @Transactional method a JDBC connection is kept open, blocked on this thread
// keeping a connection open for a long time is a waste of resources
// and can lead to connection pool exhaustion/starvation