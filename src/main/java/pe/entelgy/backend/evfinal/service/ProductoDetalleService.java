package pe.entelgy.backend.evfinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.entelgy.backend.evfinal.model.ProductoDetalle;
import pe.entelgy.backend.evfinal.repository.ProductoDetalleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoDetalleService {
    private final ProductoDetalleRepository productoDetalleRepository;

    @Autowired
    public ProductoDetalleService(ProductoDetalleRepository productoDetalleRepository){
        this.productoDetalleRepository = productoDetalleRepository;
    }

    public List<ProductoDetalle> getAllProductoDetalles(){
        return productoDetalleRepository.findAll();
    }

    public Optional<ProductoDetalle> getProductoDetalleById(Long id){
        return productoDetalleRepository.findById(id);
    }

    public ProductoDetalle saveProduct(ProductoDetalle productoDetalle){
        return productoDetalleRepository.save(productoDetalle);
    }

    public void deleteProduct(Long id){
        productoDetalleRepository.deleteById(id);
    }
}
