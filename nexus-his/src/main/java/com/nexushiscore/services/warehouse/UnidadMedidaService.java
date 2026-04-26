package com.nexushiscore.services.warehouse;

import com.nexushiscore.models.warehouse.UnidadMedidaModel;
import com.nexushiscore.repositories.warehouse.IUnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadMedidaService {

    @Autowired
    private IUnidadMedidaRepository repository;

    public List<UnidadMedidaModel> getAll() {
        return repository.findAll();
    }

    public Optional<UnidadMedidaModel> getById(Long id) {
        return repository.findById(id);
    }

    public UnidadMedidaModel save(UnidadMedidaModel unit) {
        return repository.save(unit);
    }

//    public void status(Long id) {
//        repository.deleteById(id);
//    }
}