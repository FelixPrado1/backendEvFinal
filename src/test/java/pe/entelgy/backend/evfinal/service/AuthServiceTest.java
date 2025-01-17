package pe.entelgy.backend.evfinal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pe.entelgy.backend.evfinal.controller.AuthenticationResponse;
import pe.entelgy.backend.evfinal.dto.AdminRegistrationDTO;
import pe.entelgy.backend.evfinal.dto.ClientRegistrationDTO;
import pe.entelgy.backend.evfinal.dto.LoginRequestDTO;
import pe.entelgy.backend.evfinal.dto.UserDTO;
import pe.entelgy.backend.evfinal.enums.Role;
import pe.entelgy.backend.evfinal.exception.UsernameAlreadyExistsException;
import pe.entelgy.backend.evfinal.model.Admin;
import pe.entelgy.backend.evfinal.model.Cliente;
import pe.entelgy.backend.evfinal.model.User;
import pe.entelgy.backend.evfinal.repository.AdminRepository;
import pe.entelgy.backend.evfinal.repository.ClienteRepository;
import pe.entelgy.backend.evfinal.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceTest {

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Test
    public void testRegisterClientUsernameExists() {
        ClientRegistrationDTO dto = new ClientRegistrationDTO();
        dto.setUsername("existingUser");

        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Username already exists"));

        assertThrows(UsernameAlreadyExistsException.class, () -> authService.registerClient(dto));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterAdminSuccess() {
        AdminRegistrationDTO dto = new AdminRegistrationDTO();
        dto.setNombres("Carlos");
        dto.setApellidoPaterno("Sánchez");
        dto.setApellidoMaterno("Martínez");
        dto.setUsername("adminuser");
        dto.setPassword("adminPassword123");

        Admin admin = new Admin();
        admin.setId(1L);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        User user = new User();
        user.setId(1);
        when(userRepository.save(any(User.class))).thenReturn(user);

        authService.registerAdmin(dto);

        verify(adminRepository, times(1)).save(any(Admin.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterAdminUsernameExists() {
        AdminRegistrationDTO dto = new AdminRegistrationDTO();
        dto.setUsername("existingAdmin");

        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UsernameAlreadyExistsException.class, () -> authService.registerAdmin(dto));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testLoginSuccess() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("user");
        dto.setPassword("password");

        User user = new User();
        user.setUsername("user");

        when(authenticationManager.authenticate((Authentication) any(Authentication.class))).thenReturn(null);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthenticationResponse response = authService.login(dto);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    public void testLoginInvalidCredentials() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("user");
        dto.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(dto));
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("juanperez");
        user1.setRole(Role.CLIENT);

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("adminuser");
        user2.setRole(Role.ADMIN);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserDTO> users = authService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("juanperez", users.get(0).getUsername());
        assertEquals(Role.CLIENT, users.get(0).getRole());
        assertEquals("adminuser", users.get(1).getUsername());
        assertEquals(Role.ADMIN, users.get(1).getRole());
    }
}
