package com.itmuch.cloud.study.user.controller;

import com.itmuch.cloud.study.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * @author wuyue
 */
@RequestMapping("/movies")
@RestController
public class MovieController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

  @Autowired
  private LoadBalancerClient loadBalancerClient;

  @Autowired
  private RestTemplate restTemplate;


  //要把访问地址改成服务名  虚拟主机名  ribbon和eureka 合用的时候 自动将虚拟机主机名映射成微服务的网络地址
  @GetMapping("/users/{id}")
  public User findById(@PathVariable Long id) {
    // 这里用到了RestTemplate的占位符能力
    User user = this.restTemplate.getForObject("http://microservice-provider-user/"+ id , User.class);
    // ...电影微服务的业务...
    return user;
  }

  @GetMapping("/log-instance")
  public void logUserInstance(){
    ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-provider-user");
    MovieController.LOGGER.info("{}:{}:{}",serviceInstance.getServiceId(),serviceInstance.getHost(),serviceInstance.getPort());
  }

}
