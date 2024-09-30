package victor.training.performance.jpa;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import victor.training.performance.jpa.entity.Country;
import victor.training.performance.jpa.entity.Uber;
import victor.training.performance.jpa.entity.User;
import victor.training.performance.jpa.repo.CountryRepo;
import victor.training.performance.jpa.repo.UberRepo;
import victor.training.performance.jpa.repo.UserRepo;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Import {
  public static final int ITEMS_PER_PAGE = 5;
  private final UberRepo uberRepo;
  private final UserRepo userRepo;
  private final CountryRepo countryRepo;
  private final PlatformTransactionManager transactionManager;

  record ImportedRecord(String name, String countryIso2Code, Long userId) {
  }

  public void bulkImport(List<ImportedRecord> allRecords) {
    TransactionTemplate newTransaction = new TransactionTemplate(transactionManager);
    List<List<ImportedRecord>> pages = Lists.partition(allRecords, ITEMS_PER_PAGE);
    // Map<String, Long> countryIdByIso2Code = countryRepo.findAll().stream().collect(toMap(Country::getIso2Code, Country::getId));
    for (List<ImportedRecord> page : pages) {
      newTransaction.executeWithoutResult(status -> savePageInTx(page));
    }
  }

//  @Transactional // call annotations that should make stuff work when the method is called magically
//  do not work if the method is invoked within the same class
  private void savePageInTx(List<ImportedRecord> page) {
    log.info("▶️▶️▶️▶️▶️▶️ Start page ( a new tx )"); // runs in a tx commited at the end of the method
    for (ImportedRecord record : page) {
      // A
      Country country = countryRepo.findByIso2Code(record.countryIso2Code()).orElseThrow();

      // it gives you a proxy that is not loaded until you access its properties (no SELECT)
      log.info("Before getReferenceById");
//      User user = userRepo.getReferenceById(record.userId()); // EntityManager.getReference
//      User user = userRepo.findById(record.userId()).orElseThrow(); // +1 SELECT
      User user = new User().setId(record.userId());
      log.info("After getReferenceById : " + user.getClass());
//      log.info("after .tostring " + user);

      Uber entity = new Uber()
          .setFiscalCountry(country)
          .setCreatedBy(user);
      uberRepo.save(entity);
      // .save can act as an insert(.persist) or update(.merge)
      // if the entity has a null ID => INSERT(persist)
      // if the entity has a non-null ID => UPDATE(merge) maybe?
         // to know whether the entity is new or not, Hibernate checks the ID in the DB => SELECT
    }
  }
}
