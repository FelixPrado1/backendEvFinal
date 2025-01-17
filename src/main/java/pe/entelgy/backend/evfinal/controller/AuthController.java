package pe.entelgy.backend.evfinal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.entelgy.backend.evfinal.dto.AdminRegistrationDTO;
import pe.entelgy.backend.evfinal.dto.ClientRegistrationDTO;
import pe.entelgy.backend.evfinal.dto.LoginRequestDTO;
import pe.entelgy.backend.evfinal.dto.UserDTO;
import pe.entelgy.backend.evfinal.service.AuthService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registerCliente(@RequestBody @Valid ClientRegistrationDTO clienteDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error ->
                    errorMessage.append(error.getDefaultMessage()).append("\n")
            );
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        authService.registerClient(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente registrado exitosamente");
    }

    @PostMapping("/register/admin2")
    public ResponseEntity<?> registerAdmin2(@RequestBody @Valid AdminRegistrationDTO adminDTO) {
        authService.registerAdmin(adminDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin registrado exitosamente");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid AdminRegistrationDTO adminDTO) {
        authService.registerAdmin(adminDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin registrado exitosamente");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOList = authService.getAllUsers();
        return ResponseEntity.ok(userDTOList);
    }
}