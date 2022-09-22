package com.vermeg;

import com.vermeg.entities.ERole;
import com.vermeg.entities.Role;
import com.vermeg.entities.User;
import com.vermeg.repositories.RoleRepository;
import com.vermeg.repositories.UserRepository;
import com.vermeg.service.impl.FilesStorageServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.gson.JsonElementValueReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class Application implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Resource
    FilesStorageServiceImpl storageService;

    @Autowired
    private RoleRepository roleRepository ;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // this bean used to crypt the password
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder passwordEncoderBean = applicationContext.getBean(BCryptPasswordEncoder.class);
        return passwordEncoderBean;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().addValueReader(new JsonElementValueReader());
        // modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Insert default user (with ADMIN role as default)
        List<Role> roleList = new ArrayList<>();
        roleList.add(Role.builder()
                .name("Role_Admin")

                .build()) ;

        roleList.add(Role.builder()
                .name("Role_Employee")
                .build()) ; roleList.add(Role.builder()
                .name("Role_super_Admin")
                .build()) ;

        roleList.add(Role.builder()
                .name("Role_Responsable")
                .build()) ;
        roleRepository.saveAll(roleList);
        /* this.userRepository.deleteAll();*/
       User user1 = new User();
        user1.setFirstName("Hammami");
        user1.setLastName("Nadia");
        user1.setRole(roleRepository.findByName("Role_super_Admin"));
        user1.setEmail("hammaminadia293@gmail.com");
        user1.setPassword(this.passwordEncoder().encode("Nadia123#"));
        this.userRepository.save(user1);


        // Files Storage section
        //storageService.deleteAll();
        //storageService.init();
    }
}
