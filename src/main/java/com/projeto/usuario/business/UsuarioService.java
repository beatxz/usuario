package com.projeto.usuario.business;

import com.projeto.usuario.business.converter.UsuarioConverter;
import com.projeto.usuario.business.dto.EnderecoDTO;
import com.projeto.usuario.business.dto.TelefoneDTO;
import com.projeto.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.infrastructure.entity.Endereco;
import com.projeto.usuario.infrastructure.entity.Telefone;
import com.projeto.usuario.infrastructure.entity.Usuario;
import com.projeto.usuario.infrastructure.exception.ConflictException;
import com.projeto.usuario.infrastructure.exception.ResourceNotFondException;
import com.projeto.usuario.infrastructure.repository.EnderecoRepository;
import com.projeto.usuario.infrastructure.repository.TelefoneRepository;
import com.projeto.usuario.infrastructure.repository.UsuarioRepository;
import com.projeto.usuario.infrastructure.security.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;


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

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(
                                    () -> new RuntimeException("Email não encontrado " + email)
                            )
            );
        } catch (ResourceNotFondException e) {
            throw new RuntimeException("Email não encontrado " + email);
        }
    }

    public void deleteUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFondException("Email não localizado"));
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }
    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(()->
                new ResourceNotFondException("Id não encontrado " +idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO,entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }
    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO){

        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(()->
                new ResourceNotFondException("Id não encontrado "+idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO,entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));

    }
    public EnderecoDTO cadastroEndereco(String token, EnderecoDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->
                new RuntimeException("Email não localizado "+email));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto,usuario.getId());
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }
    public TelefoneDTO cadastroTelefone(String token, TelefoneDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->
                new RuntimeException("Email não localizado "+email));

        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto,usuario.getId());
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

}
