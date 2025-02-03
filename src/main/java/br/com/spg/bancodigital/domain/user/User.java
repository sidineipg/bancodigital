package br.com.spg.bancodigital.domain.user;

import br.com.spg.bancodigital.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name="users")
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fistName;

    private String lastName;

    @Column(unique = true)
    private String document;

    @Column(unique = true)
    private String email;

    private String password;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserDTO data){
        this.fistName= data.firstName();;
        this.lastName=data.lastName();
        this.balance=data.balance();
        this.userType=data.userType();
        this.password=data.password();
        this.email=data.email();
        this.document = data.document();
    }
}
