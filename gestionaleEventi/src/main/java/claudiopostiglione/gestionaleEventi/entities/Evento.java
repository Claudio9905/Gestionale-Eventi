package claudiopostiglione.gestionaleEventi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "evento")
@NoArgsConstructor
@Getter
@Setter
public class Evento {

    //Attributi
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "Titolo")
    private String titolo;
    @Column(name = "Descrizione")
    private String descrizione;
    @Column(name = "Data_Evento")
    private LocalDate dataEvento;
    @Column(name = "Luogo")
    private String luogo;
    @Column(name = "Numero_Posti_Disponibile")
    private int numPostDisp;

    @JsonIgnore
    @OneToMany(mappedBy = "evento")
    private List<Prenotazione> listaPrenotazioni= new ArrayList<>();

    //Costruttori
    public Evento(String titolo, String descrizione, LocalDate dataEvento, String luogo, int numPostDisp) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataEvento = dataEvento;
        this.luogo = luogo;
        this.numPostDisp = numPostDisp;
    }

    //Metodi
    @Override
    public String toString() {
        return "|-- Evento " + "\n" +
                " ID: " + id + "\n" +
                " Titolo: " + titolo + "\n" +
                " Descrizione: " + descrizione + "\n" +
                " Data dell'evento: " + dataEvento + "\n" +
                " Luogo dell'evento: " + luogo + "\n" +
                " Numero posti disponibili: " + numPostDisp + "\n" +
                " --|";
    }
}
