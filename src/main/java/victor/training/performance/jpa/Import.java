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
    log.info("▶️▶️▶️▶️▶️▶️ Start page"); // runs in a tx commited at the end of the method
    for (ImportedRecord record : page) {
      Country country = countryRepo.findByIso2Code(record.countryIso2Code()).orElseThrow();
      User user = userRepo.findById(record.userId()).orElseThrow();
      // fairy god mother gave us the ID of the country
//      Long countryId = ...;

      Uber entity = new Uber()
          .setFiscalCountry(country)
          .setCreatedBy(user);
      uberRepo.save(entity);
    }
  }
}
