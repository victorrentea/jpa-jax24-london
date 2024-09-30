package victor.training.performance.jpa.entity;

import lombok.Getter;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Immutable // hibernate will never UPDATE it
@Getter
public class ParentView { // mapped to JSON,
   // a dedicated model for optimizing the query (as in CQRS)
   @Id
   private Long id;
   private String name;
   private String countryName;
   private String childrenNames;
}
