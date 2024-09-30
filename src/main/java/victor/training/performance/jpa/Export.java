package victor.training.performance.jpa;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.performance.jpa.entity.Parent;
import victor.training.performance.jpa.repo.ParentRepo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Slf4j
@Service
@RequiredArgsConstructor
public class Export {
  private final ParentRepo parentRepo;
  private final EntityManager entityManager;

  @Transactional(readOnly = true)
  public void export() {
//    System.out.println(parentRepo.findById(10001L).orElseThrow());
//    System.out.println(parentRepo.findById(10001L).orElseThrow());
    try (Writer writer = new BufferedWriter(new FileWriter("export.txt"))) {
      // 0
//      for (Parent parent : parentRepo.findAll()) {
//        writer.write(parent.toString() + "\n");
//      }

      //1
//      parentRepo.findPage(PageRequest.) // load in pages: risk : concurreny changes might make you
      // export twice the same raw or miss a row

      //2
      // driving query List<Long> parentIds = get all ids
      // partitiom tje ids and fetch ID IN (?,?..)
      // using parallelStream for maximum effort!

    // keeps a cursor open in DB on the current connectin for the entire duraton of the traversal
      // ResultSet rs; // while (rs.next()) {
      parentRepo.streamAll()
          .peek(entityManager::detach)
          .map(parent -> {
            String s = parent.toString() + "\n";
//            entityManager.detach(parent);
          // avoid memory leak; the entity manager will keep all entities in memory,
            return s;
          })
          // because it's a cache
          .forEach(line -> {
            try {
              writer.write(line);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });


    } catch (IOException e) {
      log.error("Failed to export", e);
    }
  }
}
