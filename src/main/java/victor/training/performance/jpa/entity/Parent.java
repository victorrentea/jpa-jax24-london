package victor.training.performance.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

//@SequenceGenerator(name = "parent_seq", sequenceName = "parent_seq", allocationSize = 1) // older versions of Hibernate
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Parent {
   @Id
   @GeneratedValue// TODO ⚠️older versions might do (strategy = GenerationType.SEQUENCE, generator = "parent_seq")
   private Long id;

   private String name;
   private Integer age;

   @OneToMany(mappedBy = "parent", cascade = ALL
//       , fetch = FetchType.EAGER// tell Hibernate to load all children every time it fetcjhes you a
   )
   @BatchSize(size = 20) // Hibernate-specific: use select from CHILDREN WHERE c.parent_id IN (1,2,3 ... 1000)
   private Set<Child> children = new HashSet<>();

   @ManyToOne
   private Country country; // surprise !

//   protected Parent() {} // Can't touch this!! 🎵 . It's only for Hibernate
   public Parent(String name) {
      this.name = Objects.requireNonNull(name);
   }

   public Parent addChild(Child child) {
      children.add(child);
      child.setParent(this);
      return this;
   }

   public String toString() {
      return "Parent{id=" + id + ", name='" + name + "'}";
   }
}