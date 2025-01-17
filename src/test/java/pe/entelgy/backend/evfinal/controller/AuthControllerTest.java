package pe.entelgy.backend.evfinal.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import pe.entelgy.backend.evfinal.dto.AdminRegistrationDTO;
import pe.entelgy.backend.evfinal.dto.ClientRegistrationDTO;
import pe.entelgy.backend.evfinal.dto.LoginRequestDTO;
import pe.entelgy.backend.evfinal.dto.UserDTO;
import pe.entelgy.backend.evfinal.enums.Role;
import pe.entelgy.backend.evfinal.service.AuthService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("user");
        loginRequestDTO.setPassword("password");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("some-token");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(authenticationResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"username\": \"user\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("some-token"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("user");
        loginRequestDTO.setPassword("password");

        when(authService.login(any(LoginRequestDTO.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"username\": \"user\", \"password\": \"wrong-password\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegisterClienteSuccess() throws Exception {
        ClientRegistrationDTO validClient = new ClientRegistrationDTO();
        validClient.setNombres("Juan");
        validClient.setApellidoPaterno("Pérez");
        validClient.setApellidoMaterno("Gómez");
        validClient.setDni("12345678");
        validClient.setTelefono("912345678");
        validClient.setUsername("juanperez");
        validClient.setPassword("password123");

        doNothing().when(authService).registerClient(any(ClientRegistrationDTO.class));

        mockMvc.perform(post("/auth/register/cliente")
                        .contentType("application/json")
                        .content("{\"nombres\": \"Juan\", \"apellidoPaterno\": \"Pérez\", \"apellidoMaterno\": \"Gómez\", \"dni\": \"12345678\", \"telefono\": \"912345678\", \"username\": \"juanperez\", \"password\": \"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Cliente registrado exitosamente"));
    }


    @Test
    public void testRegisterClienteDniInvalido() throws Exception {
        ClientRegistrationDTO invalidClient = new ClientRegistrationDTO();
        invalidClient.setNombres("Juan");
        invalidClient.setApellidoPaterno("Pérez");
        invalidClient.setApellidoMaterno("Gómez");
        invalidClient.setDni("1234");
        invalidClient.setTelefono("912345678");
        invalidClient.setUsername("juanperez");
        invalidClient.setPassword("password123");

        mockMvc.perform(post("/auth/register/cliente")
                        .contentType("application/json")
                        .content("{\"nombres\": \"Juan\", \"apellidoPaterno\": \"Pérez\", \"apellidoMaterno\": \"Gómez\", \"dni\": \"1234\", \"telefono\": \"912345678\", \"username\": \"juanperez\", \"password\": \"password123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(("El DNI debe tener exactamente 8 dígitos.\n")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testRegisterAdminSuccess() throws Exception {
        AdminRegistrationDTO validAdmin = new AdminRegistrationDTO();
        validAdmin.setNombres("Carlos");
        validAdmin.setApellidoPaterno("Sánchez");
        validAdmin.setApellidoMaterno("Martínez");
        validAdmin.setUsername("carlosadmin");
        validAdmin.setPassword("adminPassword123");

        doNothing().when(authService).registerAdmin(any(AdminRegistrationDTO.class));

        mockMvc.perform(post("/auth/register/admin")
                        .contentType("application/json")
                        .content("{\"nombres\": \"Carlos\", \"apellidoPaterno\": \"Sánchez\", \"apellidoMaterno\": \"Martínez\", \"username\": \"carlosadmin\", \"password\": \"adminPassword123\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Admin registrado exitosamente"));
    }

    @Test
    @WithMockUser(username = "cliente", roles = "CLIENT")
    public void testRegisterAdminForbidden() throws Exception {
        mockMvc.perform(post("/auth/register/admin")
                        .contentType("application/json")
                        .content("{\"nombres\": \"Carlos\", \"apellidoPaterno\": \"Sánchez\", \"apellidoMaterno\": \"Martínez\", \"username\": \"carlosadmin\", \"password\": \"adminPassword123\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testGetAllUsers() throws Exception {
        UserDTO user1 = new UserDTO();
        user1.setId(1);
        user1.setUsername("juanperez");
        user1.setRole(Role.CLIENT);

        UserDTO user2 = new UserDTO();
        user2.setId(2);
        user2.setUsername("adminuser");
        user2.setRole(Role.ADMIN);

        List<UserDTO> users = Arrays.asList(user1, user2);

        when(authService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/auth/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("juanperez"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testGetAllUsersUnauthorized() throws Exception {
        mockMvc.perform(get("/auth/users")
                        .contentType("application/json"))
                .andExpect(status().isForbidden());
    }

}
