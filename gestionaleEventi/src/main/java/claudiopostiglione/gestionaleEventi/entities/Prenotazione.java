package claudiopostiglione.gestionaleEventi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "prenotazione")
@Getter
@Setter
@NoArgsConstructor
public class Prenotazione {

    //Attributi
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "Data_Prenotazione")
    private LocalDate dataPrenotazione;

    @ManyToOne
    @JoinColumn(name = "utente")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "evento")
    private Evento evento;

    //Costruttori
    public Prenotazione(LocalDate dataPrenotazione, Utente utente, Evento evento){
        this.dataPrenotazione = dataPrenotazione;
        this.utente = utente;
        this.evento = evento;
    }

    //Metodi


    @Override
    public String toString() {
        return "|-- Prenotazione " + "\n" +
                " ID evento: " + id + "\n" +
                " Data prenotazione: " + dataPrenotazione + "\n" +
                " Utente: " + utente + "\n" +
                " Evento: " + evento + "\n" +
                " --|";
    }
}
