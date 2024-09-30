package victor.training.performance.jpa.entity;

import lombok.Getter;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Immutable // hibernate will never UPDATE it
@Getter
public class ParentView {
   @Id
   private Long id;
   private String name;
   private String childrenNames;
}
