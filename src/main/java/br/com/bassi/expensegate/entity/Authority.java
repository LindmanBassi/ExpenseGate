package br.com.bassi.expensegate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_authority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public static class Values {
        public static final String EXPE_CREATE = "expense:create";
        public static final String EXPE_READ = "expense:read";
        public static final String EXPE_READ_ANY = "expense:read:any";
        public static final String EXPE_APPROVE = "expense:approve";
        public static final String EXPE_APPROVE_ANY = "expense:approve:any";
        public static final String EXPE_WILDCARD= "expense:*";
    }
}
