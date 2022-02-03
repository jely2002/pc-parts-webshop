package com.jelleglebbeek.pcparts.user;

import com.jelleglebbeek.pcparts.auth.Argon2PasswordEncoder;
import com.jelleglebbeek.pcparts.auth.model.UserLogin;
import com.jelleglebbeek.pcparts.exceptions.EmailExistsException;
import com.jelleglebbeek.pcparts.exceptions.EntityNotFoundException;
import com.jelleglebbeek.pcparts.exceptions.ForbiddenException;
import com.jelleglebbeek.pcparts.user.entities.CreateUserInitializeRequest;
import com.jelleglebbeek.pcparts.user.entities.CreateUserInitializeResponse;
import com.jelleglebbeek.pcparts.user.entities.CreateUserRequest;
import com.jelleglebbeek.pcparts.user.entities.User;
import com.jelleglebbeek.pcparts.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Argon2PasswordEncoder argon2PasswordEncoder;

    @Value("${superuser.username}")
    private String superUserUsername;

    @Value("${superuser.hashed-password}")
    private String superUserPassword;

    @Value("${superuser.public-key}")
    private String superUserPublicKey;

    @Value("${superuser.private-key}")
    private String superUserPrivateKey;


    @Autowired
    public UserService(UserRepository userRepository, Argon2PasswordEncoder argon2PasswordEncoder) {
        this.userRepository = userRepository;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
    }

    public void isSelf(UUID userId) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().
                getAuthorities().contains(Role.ADMIN.toString());
        if (isAdmin) return;
        User existingUser = userRepository
                .findById(userId)
                .orElseThrow(() -> new ForbiddenException("only own user can be changed"));
        if (!existingUser.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new ForbiddenException("only own user can be changed");
        }
    }

    public CreateUserInitializeResponse createInitialize(CreateUserInitializeRequest request) throws NoSuchAlgorithmException {
        boolean existingUser = this.userRepository.findByEmail(request.getEmail()).isPresent();
        if (existingUser) throw new EmailExistsException("This email is already in use");
        User user = new User();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        user.setPrivateKey(Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()));
        user.setPublicKey(Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return new CreateUserInitializeResponse(user.getPublicKey());
    }

    public User create(CreateUserRequest request) throws GeneralSecurityException {
        User user = findOneByEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setLastName(request.getLastName());
        user.setRole(Role.CUSTOMER);

        user.setPassword(hashPassword(decryptPassword(user, request.getPassword())));

        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            throw new EmailExistsException("An account with this e-mail already exists.");
        }

        return userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }


    public User findOne(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class));
    }

    public User findOneByEmail(String email) {
        if (email.equals(superUserUsername)) {
            User user = new User();
            user.setRole(Role.ADMIN);
            user.setEmail(superUserUsername);
            user.setPassword(new String(Base64.getDecoder().decode(superUserPassword)));
            user.setFirstName("Administrator");
            user.setLastName("PCparts");
            user.setPublicKey(superUserPublicKey);
            user.setPrivateKey(superUserPrivateKey);
            return user;
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(User.class));
    }

    public User update(UUID id, User appUser) {
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        if (appUser.getEmail() != null) {
            existingUser.setEmail(appUser.getEmail());
        }
        if (appUser.getPassword() != null) {
            existingUser.setPassword(hashPassword(appUser.getPassword()));
        }
        return userRepository.save(existingUser);
    }

    public void delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class));
        userRepository.delete(user);
    }

    private Role getUserRole() {
        List<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().
                getAuthorities().stream().collect(Collectors.toList());
        try {
            return Role.valueOf(authorities.get(0).toString());
        } catch (IllegalArgumentException e) {
            if (authorities.size() != 1) {
                return Role.valueOf(authorities.get(1).toString());
            } else {
                return null;
            }
        }
    }

    private String decryptPassword(User user, String password) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(user.getPrivateKey())));
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);
        byte[] decrypted = decryptCipher.doFinal(Base64.getDecoder().decode(password));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private String hashPassword(String password) {
        return argon2PasswordEncoder.encode(password);
    }
}
