package com.becoder.service.impl;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.becoder.config.security.CustomUserDetails;
import com.becoder.dto.EmailRequest;
import com.becoder.dto.LoginRequest;
import com.becoder.dto.LoginResponse;
import com.becoder.dto.PasswordChngRequest;
import com.becoder.dto.PswdResetRequest;
import com.becoder.dto.UserRequest;
import com.becoder.dto.UserResponse;
import com.becoder.entity.AccountStatus;
import com.becoder.entity.Role;
import com.becoder.entity.User;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.RoleRepository;
import com.becoder.repository.UserRepository;
import com.becoder.service.JwtService;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtil;
import com.becoder.util.Validation;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService{


	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository rolerepo;
	
	@Autowired
	private Validation validation;
	
	@Autowired
	private ModelMapper mapper;
	
    @Autowired
	private EmailService emailService;
    
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
   
	@Autowired    
	private PasswordEncoder passwordEncoder;
	    
    @Autowired
    private JwtService jwtService;
    
	
	
	@Override
	public Boolean register(UserRequest userDto, String url) throws Exception {
		validation.userValidation(userDto);
		User user = mapper.map(userDto, User.class);
		
		setRole(userDto, user);
		AccountStatus status= AccountStatus.builder()
				.isActive (false)
				.verificationCode (UUID. randomUUID() .toString())
				.build();
				user.setStatus (status) ;
				user.setPassword (passwordEncoder.encode(user.getPassword()));
		
		User saveUser = userRepo.save(user);
		if (!ObjectUtils.isEmpty(saveUser)) {
			//Send Email 
			emailSendForRegister(saveUser, url);
			return true;
		}
		return false;
	}

	private void emailSendForRegister(User saveUser, String url) throws Exception {

	    String message = "Hi, <b>[[username]]</b> "
	            + "<br> Your account registered successfully.<br>"
	            + "<br> Click the below link to verify & activate your account.<br>"
	            + "<a href='[[url]]'>Click Here</a><br><br>"
	            + "Thanks,<br>Anil Kumar";

	    message = message.replace(
	            "[[username]]",
	            saveUser.getFirstName()
	    );

	    String verificationUrl =
	            url + "/api/v1/home/verify?uid="
	            + saveUser.getId()
	            + "&code="
	            + saveUser.getStatus().getVerificationCode();

	    message = message.replace(
	            "[[url]]",
	            verificationUrl
	    );
	

	    EmailRequest emailRequest = EmailRequest.builder()
	            .to(saveUser.getEmail())
	            .title("Account Creating Confirmation")
	            .subject("Account Created Successfully")
	            .message(message)
	            .build();

	    emailService.sendEmail(emailRequest);
	}
	
	public void setRole(UserRequest userDto, User user) {
			List<Integer> regRoleld = userDto.getRoles().stream().map(r->r.getId()).toList();
			List<Role> roles = rolerepo.findAllById(regRoleld);
			user.setRoles(roles);

			}

			@Override
			public LoginResponse login(LoginRequest loginRequest) {
				Authentication authenticate = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

				if (authenticate.isAuthenticated()) {
					CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
					String token = jwtService.generateToken(customUserDetails.getUser());
					
					LoginResponse loginResponse = LoginResponse.builder()
							.user(mapper.map(customUserDetails.getUser(), UserResponse.class))
							.token(token)
							.build();

					return loginResponse;
				}
				return null;

			}

			@Override
			public void changePassword(PasswordChngRequest passwordRequest) {

			    User loggedInUser = CommonUtil.getLoggedInUser();

			    if (passwordRequest.getOldPassword() == null ||
			        passwordRequest.getOldPassword().isBlank()) {

			        throw new IllegalArgumentException("Old password cannot be empty");
			    }

			    if (passwordRequest.getNewPassword() == null ||
			        passwordRequest.getNewPassword().isBlank()) {

			        throw new IllegalArgumentException("New password cannot be empty");
			    }

			    if (!passwordEncoder.matches(
			            passwordRequest.getOldPassword(),
			            loggedInUser.getPassword())) {

			        throw new IllegalArgumentException("Old password is incorrect!");
			    }

			    loggedInUser.setPassword(
			            passwordEncoder.encode(passwordRequest.getNewPassword())
			    );

			    userRepo.save(loggedInUser);
			}

			@Override
			public void sendEmailPasswordRest(String email, HttpServletRequest request) throws Exception {

				User user = userRepo.findByEmail(email);

				if (ObjectUtils.isEmpty(user)) {
					throw new ResourceNotFoundException("invalid Email");
				}
				
				// Generate unique password reset token

				String passwordResetToken = UUID.randomUUID().toString();
				user.getStatus().setPasswordRestToken(passwordResetToken);
				User updateUser = userRepo.save(user);
				
				String url = CommonUtil.getUrl(request);
				sendEmailRequest(user, url);
			}

			private void sendEmailRequest(User user, String url) throws Exception {
				String message = "Hi <b>[[username]]</b>,"
						+ "<br>"
						+ "<p>You have requested to reset your password.</p>"
						+ "<p>Click the link below to change your password:</p>"
						+ "<p><a href=\"[[url]]\">Change my password</a></p>"
						+ "<p>Ignore this email if you remember your password, "
						+ "or if you did not make this request.</p>"
						+ "<br>"
						+ "Thanks,<br>" + "Anil";

			    message = message.replace(
			            "[[username]]",
			            user.getFirstName()
			    );

			    String verificationUrl =
			            url + "/api/v1/home/verify-pswd-link?uid="
			            + user.getId()
			            + "&code="
			            + user.getStatus().getPasswordRestToken();

			    message = message.replace(
			            "[[url]]",
			            verificationUrl);
			

			    EmailRequest emailRequest = EmailRequest.builder()
			            .to(user.getEmail())
			            .title("Password Reset")
			            .subject("Password Reset link")
			            .message(message)
			            .build();

			 // send password reset email to user
			    
			    emailService.sendEmail(emailRequest);
	} 
			@Override
			public void verifyPswdRestLink(Integer uid, String code) throws Exception{
			User user =	userRepo.findById(uid).orElseThrow(()->new ResourceNotFoundException("invalid user"));	
			verifyPasswordResetToken(user.getStatus().getPasswordRestToken(), code) ;

	}

			private void verifyPasswordResetToken(String existToken, String reqtoken) {
				// request token not null
				if (StringUtils.hasText(reqtoken)) {

				    // password already reset
				    if (!StringUtils.hasText(existToken)) {
				        throw new IllegalArgumentException("Already password reset");
				    }

				    // requested token and existing token are different
				    if (!existToken.equals(reqtoken)) {
				        throw new IllegalArgumentException("Invalid token");
				    }

				    // If they are equal, continue with password reset
				}
			}

			@Override
			public void resetPassword(PswdResetRequest pswdResetRequest) throws Exception{
				User user = userRepo.findById(pswdResetRequest.getUid()).orElseThrow(()->new ResourceNotFoundException("Invalid User"));
						String encodePassword = passwordEncoder.encode (pswdResetRequest .getNewPassword());
						user.setPassword (encodePassword) ;
						user.getStatus().setPasswordRestToken(null);
						userRepo.save (user) ;
	}		
}