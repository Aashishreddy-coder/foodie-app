package com.foodie.foodieapp.service;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import com.foodie.foodieapp.domain.AppUser;
import com.foodie.foodieapp.dto.RegisterRequest;
import com.foodie.foodieapp.exceptions.InvalidCredentialsException;
import com.foodie.foodieapp.exceptions.UserAlreadyExistsException;
import com.foodie.foodieapp.repository.UserRepository;
import com.foodie.foodieapp.exceptions.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;


    @Value("${project.image}")
    private String path;

    private String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public AppUser save(RegisterRequest user, MultipartFile image) throws UserAlreadyExistsException, IOException {
        
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        String fileName =fileService.uploadImage(path,image);
        

        AppUser appUser=modelMapper.map(user, AppUser.class);

        appUser.setImage(fileName);





        return userRepository.save(appUser);
    }

    @Override
    public AppUser getUserByEmail() {
        String userEmail=getUserEmail();
        return userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));
    }

    @Override
    public AppUser updateUser(AppUser user) {
        String userEmail=getUserEmail();
        AppUser existingUser=userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));
        
      if(user.getName()!=null){
            existingUser.setName(user.getName());
        }
        if(user.getEmail()!=null){
            existingUser.setEmail(user.getEmail());
        }
        if(user.getPassword()!=null){
            existingUser.setPassword(user.getPassword());
        }
        if(user.getPhone()!=null){
            existingUser.setPhone(user.getPhone());
        }
        if(user.getAddress()!=null){
            existingUser.setAddress(user.getAddress());
        }   
       

        return userRepository.save(existingUser);


   



      
    }

   
    public AppUser updateImage(MultipartFile image) throws IOException {
        String userEmail=getUserEmail();
        AppUser existingUser=userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));

        fileService.deleteImage(path,existingUser.getImage());
        String fileName =fileService.uploadImage(path,image);
        existingUser.setImage(fileName);
        return userRepository.save(existingUser);
    }




    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public AppUser findByEmailAndPassword(String email, String password) throws InvalidCredentialsException {
        return userRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }

   

}   
