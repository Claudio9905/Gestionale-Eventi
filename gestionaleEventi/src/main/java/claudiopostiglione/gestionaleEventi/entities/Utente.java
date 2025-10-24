package claudiopostiglione.gestionaleEventi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.*;

@Entity
@Table(name = "utente")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"password","authorities", "enabled", "accountNonLocked", "accountNonExpired", "credentialsNonExpired"})
public class Utente implements UserDetails {

    //Attributi
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "Nome")
    private String nome;
    @Column(name = "Cognome")
    private String cognome;
    @Column(name = "Età")
    private int eta;
    @Column(name = "Email")
    private String email;
    @Column(name = "Password")
    private String password;
    @Column(name = "Ruolo")
    @Enumerated(EnumType.STRING)
    private RuoloUtente role;
    @Column(name = "Avatar_url")
    private String avatar_url;

    @JsonIgnore
    @OneToMany(mappedBy = "utente")
    private List<Prenotazione> listaPrenotazioni= new ArrayList<>();


    //Costruttori
    public Utente(String nome, String cognome, int eta, String email, String password, RuoloUtente role) {

//        Random rdm = new Random();
//        String[] roles = {"UTENTE_NORMALE", "ORGANIZZATORE_DI_EVENTI", "ADMIN"};
//        RuoloUtente role = RuoloUtente.valueOf(roles[rdm.nextInt(0, 2)]);

        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    //Metodi
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String toString() {
        return "|-- Utente " + "\n" +
                " ID= " + id + "\n" +
                " Nome= " + nome + "\n" +
                " Cognome= " + cognome + "\n" +
                " Età= " + eta + "\n" +
                " E-mail= " + email + "\n" +
                " Password= " + password + "\n" +
                " Role= " + role + "\n" +
                " Avatar_url= " + avatar_url + "\n" +
                " --|";
    }
}
