package com.algaworks.algalog.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algalog.api.assembler.ClienteAssembler;
import com.algaworks.algalog.api.model.ClienteModel;
import com.algaworks.algalog.api.model.input.ClienteInput;
import com.algaworks.algalog.domain.model.Cliente;
import com.algaworks.algalog.domain.repository.ClienteRepository;
import com.algaworks.algalog.domain.services.CatalogoClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CatalogoClienteService catalogoClienteService;

    @Autowired
    private ClienteAssembler clienteAssembler;
    
    @GetMapping
    public List<ClienteModel> listar(){
        return clienteAssembler.toCollectionModel(clienteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteModel> buscar(@PathVariable Long id){
     
        return clienteRepository.findById(id)
        .map(cliente -> ResponseEntity.ok(clienteAssembler.toModel(cliente)))
        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteModel adicionar(@Valid @RequestBody ClienteInput clienteInput){

        Cliente novoCliente = clienteAssembler.toEntity(clienteInput);        
        Cliente clienteSolicitado = catalogoClienteService.salvar(novoCliente);
        return clienteAssembler.toModel(clienteSolicitado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteModel> atualizar(@PathVariable Long id, 
        @Valid @RequestBody ClienteInput clienteInput){

        if (!clienteRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        else{
            Cliente novoCliente = clienteAssembler.toEntity(clienteInput); 
            novoCliente.setId(id);       
            Cliente clienteSolicitado = catalogoClienteService.salvar(novoCliente);
            return ResponseEntity.ok(clienteAssembler.toModel(clienteSolicitado));
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id){
        
        if (!clienteRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        else{
            catalogoClienteService.excluir(id);
            return ResponseEntity.noContent().build();
        }
    }

}

