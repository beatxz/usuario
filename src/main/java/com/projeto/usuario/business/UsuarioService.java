package com.projeto.usuario.business;

import com.projeto.usuario.business.converter.UsuarioConverter;
import com.projeto.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.infrastructure.entity.Usuario;
import com.projeto.usuario.infrastructure.exception.ConflictException;
import com.projeto.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
         return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException("Email já cadastrado " +email);
            }
        }catch(ConflictException e){
            throw new ConflictException("Email já cadastrado "+ e.getCause());
        }
    }
    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Email não encontrado "+email));
    }
    public void deleteUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }
}
