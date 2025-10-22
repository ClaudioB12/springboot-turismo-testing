package pe.edu.upeu.turismospringboot.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.service.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/admin/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /** ✅ LISTAR TODAS LAS CATEGORÍAS */
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        return ResponseEntity.ok(categoriaService.getCategorias());
    }

    /** ✅ OBTENER UNA CATEGORÍA POR ID */
    @GetMapping("/{idCategoria}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long idCategoria) {
        Categoria categoria = categoriaService.getCategoriaById(idCategoria);
        return ResponseEntity.ok(categoria);
    }

    /** ✅ CREAR UNA NUEVA CATEGORÍA */
    @PostMapping
    public ResponseEntity<Categoria> guardarCategoria(
            @RequestPart(value = "categoria") String categoriaJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            CategoriaDto categoriaDto = objectMapper.readValue(categoriaJson, CategoriaDto.class);
            Categoria nuevaCategoria = categoriaService.postCategoria(categoriaDto, file);

            // 🔥 Aseguramos devolver el objeto completo con su ID generado
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** ✅ ACTUALIZAR UNA CATEGORÍA EXISTENTE */
    @PutMapping("/{idCategoria}")
    public ResponseEntity<Categoria> actualizarCategoria(
            @PathVariable Long idCategoria,
            @RequestPart(value = "categoria") String categoriaJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            CategoriaDto categoriaDto = objectMapper.readValue(categoriaJson, CategoriaDto.class);
            Categoria categoriaActualizada = categoriaService.putCategoria(idCategoria, categoriaDto, file);
            return ResponseEntity.ok(categoriaActualizada);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** ✅ ELIMINAR UNA CATEGORÍA */
    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Long idCategoria) {
        try {
            categoriaService.deleteCategoria(idCategoria);
            return ResponseEntity.ok("Categoría eliminada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoría no existe");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la categoría: " + e.getMessage());
        }
    }

    /** ✅ BUSCAR CATEGORÍAS POR NOMBRE */
    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(categoriaService.buscarPorNombre(nombre));
    }
}
