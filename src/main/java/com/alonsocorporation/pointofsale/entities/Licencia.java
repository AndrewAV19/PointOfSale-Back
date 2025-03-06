package com.alonsocorporation.pointofsale.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "licencia")
public class Licencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String claveLicencia;

    @Column(nullable = false)
    private String estadoActivacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClaveLicencia() { return claveLicencia; }
    public void setClaveLicencia(String claveLicencia) { this.claveLicencia = claveLicencia; }

    public String getEstadoActivacion() { return estadoActivacion; }
    public void setEstadoActivacion(String estadoActivacion) { this.estadoActivacion = estadoActivacion; }
}
