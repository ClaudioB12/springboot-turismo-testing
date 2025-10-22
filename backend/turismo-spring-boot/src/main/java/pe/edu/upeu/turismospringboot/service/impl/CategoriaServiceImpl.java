package pe.edu.upeu.turismospringboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.repository.CategoriaRepository;
import pe.edu.upeu.turismospringboot.service.CategoriaService;
import pe.edu.upeu.turismospringboot.util.ArchivoUtil;

import java.util.List;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria getCategoriaById(Long idCategoria) {
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("La categoria con id " + idCategoria + " no existe"));
    }

    @Override
    public Categoria postCategoria(CategoriaDto categoriaDto, MultipartFile file) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDto.getNombre());
        categoria.setDescripcion(categoriaDto.getDescripcion());

        if (file != null && !file.isEmpty()) {
            String fileName = ArchivoUtil.saveFile(file);
            categoria.setImagenUrl(fileName);
        }

        Categoria savedCategoria = categoriaRepository.saveAndFlush(categoria);
        // 🔥 Importante: asegurar que el ID se genere y se devuelva correctamente
        return savedCategoria;
    }

    @Override
    public Categoria putCategoria(Long idCategoria, CategoriaDto categoriaDto, MultipartFile file) {
        Categoria categoriaEncontrada = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("La categoria con id " + idCategoria + " no existe"));

        categoriaEncontrada.setNombre(categoriaDto.getNombre());
        categoriaEncontrada.setDescripcion(categoriaDto.getDescripcion());

        if (file != null && !file.isEmpty()) {
            String fileName = ArchivoUtil.saveFile(file);
            categoriaEncontrada.setImagenUrl(fileName);
        }

        // 🔄 Save y flush inmediato para reflejar cambios en el test
        return categoriaRepository.saveAndFlush(categoriaEncontrada);
    }

    @Override
    public void deleteCategoria(Long idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new RuntimeException("La categoria con id " + idCategoria + " no existe");
        }
        categoriaRepository.deleteById(idCategoria);
    }

    @Override
    public List<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.buscarPorNombre(nombre);
    }
}
