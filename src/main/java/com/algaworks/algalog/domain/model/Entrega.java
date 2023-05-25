package com.algaworks.algalog.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.algaworks.algalog.domain.exception.NegocioException;

@Entity
public class Entrega {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Cliente cliente;
    
    @Embedded
    private Destinatario destinatario;
    
    private BigDecimal taxa;
    
    @Enumerated(EnumType.STRING)
    private StatusEntrega status;

    private OffsetDateTime dataPedido;
    private OffsetDateTime dataFinalizacao;

    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL)
    private List<Ocorrencia> ocorrencias = new ArrayList<>();
    
    public List<Ocorrencia> getOcorrencias() {
        return ocorrencias;
    }
    public Long getId() {
        return id;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public Destinatario getDestinatario() {
        return destinatario;
    }
    public BigDecimal getTaxa() {
        return taxa;
    }
    public StatusEntrega getStatus() {
        return status;
    }
    public OffsetDateTime getDataPedido() {
        return dataPedido;
    }
    public OffsetDateTime getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void setDestinatario(Destinatario destinatario) {
        this.destinatario = destinatario;
    }
    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }
    public void setStatus(StatusEntrega status) {
        this.status = status;
    }
    public void setDataPedido(OffsetDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }
    public void setDataFinalizacao(OffsetDateTime dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }
    public void setOcorrencias(List<Ocorrencia> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entrega other = (Entrega) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    public Ocorrencia adicionarOcorrencia(String descricao) {
        
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setDescricao(descricao);
        ocorrencia.setDataRegistro(OffsetDateTime.now());
        ocorrencia.setEntrega(this);

        this.getOcorrencias().add(ocorrencia);

        return ocorrencia;
    }
    public void finalizar() {
        if(naoPodeSerFinalizada()){
            throw new NegocioException("Entrega não pode ser finalizada!");
        }
        setStatus(StatusEntrega.FINALIZADA);
        setDataFinalizacao(OffsetDateTime.now());  
    }

    public boolean podeSerFinalizada(){
        return StatusEntrega.PENDENTE.equals(getStatus());
    }

    public boolean naoPodeSerFinalizada(){
        return !podeSerFinalizada();
    }

    
}

