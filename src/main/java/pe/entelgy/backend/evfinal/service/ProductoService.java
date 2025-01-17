package pe.entelgy.backend.evfinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.entelgy.backend.evfinal.exception.ProductoException;
import pe.entelgy.backend.evfinal.exception.ProductoNotFoundException;
import pe.entelgy.backend.evfinal.model.Producto;
import pe.entelgy.backend.evfinal.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    public List<Producto> getAllProductos(){
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Long id){
        return productoRepository.findById(id);
    }

    public Producto saveProduct(Producto producto){
        return productoRepository.save(producto);
    }

    public void deleteProduct(Long id){
        productoRepository.deleteById(id);
    }
}
