package com.projeto.usuario.business;

import com.projeto.usuario.business.converter.UsuarioConverter;
import com.projeto.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.infrastructure.entity.Usuario;
import com.projeto.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
         return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
}
