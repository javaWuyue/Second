package com.itmuch.cloud.study.user.controller;

import com.itmuch.cloud.study.user.entity.User;
import com.itmuch.cloud.study.user.feign.UserFeignClient;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouli
 */
//@RequestMapping("/movies")
@Import(FeignClientsConfiguration.class)
@RestController
public class MovieController {

  private UserFeignClient userFeignClient;

  private UserFeignClient adminFeignClient;

  @Autowired
  public MovieController(Decoder decoder, Encoder encoder, Client client, Contract contract){

    this.userFeignClient =  Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
            .requestInterceptor(new BasicAuthRequestInterceptor("user","password2"))
            .target(UserFeignClient.class,"http://microservice-provider-user/");
    this.adminFeignClient =  Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
            .requestInterceptor(new BasicAuthRequestInterceptor("admin","password3"))
            .target(UserFeignClient.class,"http://microservice-provider-user/");
  }

  @GetMapping("/user-user/{id}")
  public User findByIdUser(@PathVariable Long id){
    return this.userFeignClient.findById(id);
  }

  @GetMapping("/user-admin/{id}")
  public User findByIdAdmin(@PathVariable Long id){
    return this.adminFeignClient.findById(id);
  }


  @GetMapping("/users/{id}")
  public User findById(@PathVariable Long id) {
    return this.userFeignClient.findById(id);
  }


}
