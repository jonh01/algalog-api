package com.algaworks.algalog.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algalog.domain.model.Entrega;
import com.algaworks.algalog.domain.model.StatusEntrega;
import com.algaworks.algalog.domain.repository.EntregaRepository;

@Service
public class FinalizacaoEntregaService {
    
    @Autowired
    private EntregaRepository entregaRepository;
    @Autowired
    private BuscaEntregaService buscaEntregaService;

    @Transactional
    public void finalizar(Long entregaId){

        Entrega entrega = buscaEntregaService.bsucar(entregaId);
        entrega.finalizar();
        entregaRepository.save(entrega);
    }
}
