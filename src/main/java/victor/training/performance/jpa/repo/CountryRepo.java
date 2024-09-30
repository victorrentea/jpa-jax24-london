package victor.training.performance.jpa.repo;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import victor.training.performance.jpa.entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryRepo extends JpaRepository<Country, Long> {
  // Only caches returned IDs => MUST also have @Cache on Country @Entity
  @QueryHints({
      @QueryHint(name = "org.hibernate.cacheable", value = "true"),
      @QueryHint(name = "org.hibernate.cacheRegion", value = "allCountries")
  })
  List<Country> findAll();

  // second level cacheL:
  @QueryHints({
      @QueryHint(name = "org.hibernate.cacheable", value = "true"),
      @QueryHint(name = "org.hibernate.cacheRegion", value = "country-by-iso")
  })
  Optional<Country> findByIso2Code(String iso2Code);
}
