// src/main/java/br/com/fiap/mottucontrol/model/Moto.java
package br.com.fiap.mottucontrol.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TB_MOTTU_MOTOS")
@Data // Anotação do Lombok para gerar Getters, Setters, toString, etc.
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DS_MODELO", nullable = false)
    private String modelo;

    @Column(name = "NR_PLACA", nullable = false, unique = true)
    private String placa;

    @Column(name = "NR_ANO")
    private int ano;
}