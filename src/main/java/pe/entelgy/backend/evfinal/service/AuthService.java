package pe.entelgy.backend.evfinal.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.entelgy.backend.evfinal.controller.AuthenticationResponse;
import pe.entelgy.backend.evfinal.dto.*;
import pe.entelgy.backend.evfinal.exception.UsernameAlreadyExistsException;
import pe.entelgy.backend.evfinal.model.Admin;
import pe.entelgy.backend.evfinal.model.Cliente;
import pe.entelgy.backend.evfinal.model.User;
import pe.entelgy.backend.evfinal.repository.AdminRepository;
import pe.entelgy.backend.evfinal.repository.ClienteRepository;
import pe.entelgy.backend.evfinal.repository.UserRepository;
import pe.entelgy.backend.evfinal.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final ClienteRepository clienteRepository;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AdminRepository adminRepository;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public void registerClient(ClientRegistrationDTO dto) {

        try {
            Cliente cliente = new Cliente();
            cliente.setNombres(dto.getNombres());
            cliente.setApellidoPaterno(dto.getApellidoPaterno());
            cliente.setApellidoMaterno(dto.getApellidoMaterno());
            cliente.setDni(dto.getDni());
            cliente.setTelefono(dto.getTelefono());
            clienteRepository.save(cliente);

            User user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRole(Role.CLIENT);
            user.setCliente(cliente);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso");
        }
    }

    @Transactional
    public void registerAdmin(AdminRegistrationDTO dto) {
        try {
            Admin admin = new Admin();
            admin.setNombres(dto.getNombres());
            admin.setApellidoPaterno(dto.getApellidoPaterno());
            admin.setApellidoMaterno(dto.getApellidoMaterno());
            adminRepository.save(admin);

            User user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRole(Role.ADMIN);
            user.setAdmin(admin);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso");
        }
    }

    public AuthenticationResponse login(LoginRequestDTO loginRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );
        var user = userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setRole(user.getRole());

            if (user.getCliente() != null) {
                Cliente cliente = user.getCliente();
                ClienteDTO clienteDTO = new ClienteDTO();
                clienteDTO.setId(cliente.getId());
                clienteDTO.setNombres(cliente.getNombres());
                clienteDTO.setApellidoPaterno(cliente.getApellidoPaterno());
                clienteDTO.setApellidoMaterno(cliente.getApellidoMaterno());
                clienteDTO.setTelefono(cliente.getTelefono());
                clienteDTO.setDni(cliente.getDni());
                userDTO.setCliente(clienteDTO);
            }

            if (user.getAdmin() != null) {
                Admin admin = user.getAdmin();
                AdminDTO adminDTO = new AdminDTO();
                adminDTO.setId(admin.getId());
                adminDTO.setNombres(admin.getNombres());
                adminDTO.setApellidoPaterno(admin.getApellidoPaterno());
                adminDTO.setApellidoMaterno(admin.getApellidoMaterno());
                userDTO.setAdmin(adminDTO);
            }

            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}
