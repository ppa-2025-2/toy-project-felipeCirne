package com.example.demo.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;

import com.example.demo.controller.dto.NewUserDTO;
import com.example.demo.domain.stereotype.Business;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Profile;
import com.example.demo.repository.entity.Role;
import com.example.demo.repository.entity.User;

import jakarta.validation.Valid;

record NewTicketRequest(
        Integer ownerId,
        Integer recipientId,
        String object,
        String action,
        String details,
        String local
        ) {

}

@Business
@Validated
public class UserBusiness {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Set<String> defaultRoles;

    private final RestClient restClient;

    public UserBusiness(
            UserRepository userRepository,
            RoleRepository roleRepository,
            @Value("${app.user.default.roles}") Set<String> defaultRoles,
            
            RestClient.Builder restClientBuilder,
            @Value("${app.ms-ticket.url:http://localhost:8081/api/v1}") String msTicketUrl
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.defaultRoles = defaultRoles;
        this.restClient = restClientBuilder.baseUrl(msTicketUrl).build();
    }

    public void cadastrarUsuario(@Valid NewUserDTO newUser) {
       
        if (!newUser.password().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres e conter pelo menos uma letra e um número");
        }

        userRepository.findByEmail(newUser.email())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Usuário com o email " + newUser.email() + " já existe");
                });

        userRepository.findByHandle(newUser.handle())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Usuário com o nome " + newUser.handle() + " já existe");
                });

        User user = new User();
        user.setEmail(newUser.email());
        user.setHandle(newUser.handle() != null ? newUser.handle() : generateHandle(newUser.email()));
        user.setPassword(passwordEncoder.encode(newUser.password()));

        Set<Role> roles = new HashSet<>();
        roles.addAll(roleRepository.findByNameIn(defaultRoles));

        if (newUser.roles() != null && !newUser.roles().isEmpty()) {
            Set<Role> additionalRoles = roleRepository.findByNameIn(newUser.roles());
            if (additionalRoles.size() != newUser.roles().size()) {
                throw new IllegalArgumentException("Alguns papéis não existem");
            }
            roles.addAll(additionalRoles);
        }

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("O usuário deve ter pelo menos um papel");
        }

        user.setRoles(roles);

        Profile profile = new Profile();
        profile.setName(newUser.name());
        profile.setCompany(newUser.company());
        profile.setType(newUser.type() != null ? newUser.type() : Profile.AccountType.FREE);

        profile.setUser(user);
        user.setProfile(profile);

     
        User savedUser = userRepository.save(user);

        try {
            criarTicketsDeOnboarding(savedUser);
        } catch (Exception e) {
            System.err.println("AVISO: Falha ao criar tickets automáticos no ms-ticket: " + e.getMessage());
        }
    }

    private void criarTicketsDeOnboarding(User user) {
        String companyName = user.getProfile().getCompany() != null ? user.getProfile().getCompany() : "Empresa";

        var ticketOnboard = new NewTicketRequest(
                user.getId(),
                user.getId(), 
                "Processo de Onboarding",
                "Realizar Onboard",
                "Boas vindas ao novo colaborador " + user.getProfile().getName(),
                "RH / Remoto"
        );
        enviarTicket(ticketOnboard);

        var ticketEstacao = new NewTicketRequest(
                user.getId(),
                user.getId(),
                "Estação de Trabalho",
                "Alocar Equipamento",
                "Preparar notebook e periféricos para uso na " + companyName,
                "TI / Presencial"
        );
        enviarTicket(ticketEstacao);
    }

    private void enviarTicket(NewTicketRequest request) {
        restClient.post()
                .uri("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity(); 
    }

    private String generateHandle(String email) {
        String[] parts = email.split("@");
        String handle = parts[0];
        int i = 1;
        while (userRepository.existsByHandle(handle)) {
            handle = parts[0] + i++;
        }
        return handle;
    }
}
