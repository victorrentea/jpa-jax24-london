package victor.training.performance.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Clob;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@Embeddable
record BankAccount(String bankName, String swiftCode, String ibanCode) {
}

@Entity
@Data
public class Uber {
    @Id
//    @GeneratedUUID// .save(entity{id=null}) => generator is called to create an id
//    private String id;
    private String id = UUID.randomUUID().toString(); // .save(entity{id!=null}) => .merge() => +1 SELECT before every INSERT

    private String name;
    private String address;
    private String city;

//    private String bankName;
//    private String swiftCode;
//    private String ibanCode;
    @Embedded // THIS refactoring did not change the DB schema => -2 fields
    private BankAccount bankAccount;

    private String cnp;
    private String ssn;
    private String passportNumber;

    // OOP naive paradise (a la enthusiastic Junior)
    // if in your business logic, you never very rarely have to traverse this link between the Uber object and the country
    // then do not model this as a JPA link as an object reference
    // instead, model it as a simple String or Long
//    @ManyToOne
//    private Country originCountry;

    private Long originCountryId; // numeric link => -1 JOIN or -1 SELECT everywhere

    @ManyToOne
    private Country nationality;
    @ManyToOne
    private Country fiscalCountry;
    @ManyToOne
    private Country invoicingCountry;
    @ManyToOne
    private Scope scope;
//    @Convert(converter = ScopeEnumConverter.class) // store 1 letter code
//    @Enumerated(STRING) // store enum name
//    private ScopeEnum scopeEnum;
    @ManyToOne
    private User createdBy;
    public enum Status {
        DRAFT, SUBMITTED, DELETED
    }
    @Enumerated(STRING)
    private Status status;

    @Lob // Character Large Object / Binary Large Object (CLOB/BLOB)
//    private char[] content;
    private Clob content;
}

