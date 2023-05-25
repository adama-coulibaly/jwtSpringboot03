package com.alibou.security.auth;

import com.alibou.security.Messages.ResponseMessage;
import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserRepository userRepository;

  @PostMapping("/register")
  public Object register(
      @RequestBody RegisterRequest request
  ) {
    Optional<User> user = userRepository.findByEmail(request.getEmail());

    if(request.getLastname().isEmpty()){
      ResponseMessage message = new ResponseMessage("Veuillez donner un prenom !",false);
      return message;
    }
    if(request.getFirstname().isEmpty()){
      ResponseMessage message = new ResponseMessage("Veuillez donner un nom !",false);
      return message;
    }
    if(request.getEmail().isEmpty()){
      ResponseMessage message = new ResponseMessage("Veuillez donner un email !",false);
      return message;
    }
    if(request.getPassword().isEmpty()){
      ResponseMessage message = new ResponseMessage("Veuillez donner un mot de passe correct !",false);
      return message;
    }

    if(user.isPresent()){
      ResponseMessage message = new ResponseMessage("Cet email existe déja !",false);
      return message;
    }
    else {
      return ResponseEntity.ok(service.register(request));
    }

  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}
