package pe.entelgy.backend.evfinal.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.entelgy.backend.evfinal.model.Producto;
import pe.entelgy.backend.evfinal.service.ProductoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    @GetMapping()
    public ResponseEntity<List<Producto>> getProductos(){
        List<Producto> productos = productoService.getAllProductos();
        if (productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id){
        Optional<Producto> producto = productoService.getProductoById(id);
        if (producto.isPresent()){
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Producto createProducto(@Valid @RequestBody Producto producto){
        return productoService.saveProduct(producto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails){
        Optional<Producto> producto = productoService.getProductoById(id);
        if (producto.isPresent()){
            Producto productoUpdated = producto.get();
            if (productoDetails.getNombre() != null) {
                productoUpdated.setNombre(productoDetails.getNombre());
            }

            if (productoDetails.getCategoria() != null) {
                productoUpdated.setCategoria(productoDetails.getCategoria());
            }

            if (productoDetails.getPrecio() != null) {
                productoUpdated.setPrecio(productoDetails.getPrecio());
            }

            if (productoDetails.getDescripcion() != null) {
                productoUpdated.setDescripcion(productoDetails.getDescripcion());
            }
            return ResponseEntity.ok(productoService.saveProduct(productoUpdated));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id){
        Optional<Producto> producto = productoService.getProductoById(id);
        if (producto.isPresent()){
            try {
                productoService.deleteProduct(id);
                return ResponseEntity.ok("Producto eliminado con éxito.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al eliminar el producto: " + e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
