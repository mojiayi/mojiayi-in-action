package com.mojiayi.action.springcloud.service;

import com.mojiayi.action.springcloud.domain.CommonResult;
import com.mojiayi.action.springcloud.domain.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(value = "user-service", fallback = UserFallbackService.class)
public interface UserService {
//    @PostMapping("/user/create")
    CommonResult create(@RequestBody User user);

//    @GetMapping("/user/{id}")
    CommonResult<User> getUser(@PathVariable Long id);

//    @GetMapping("/user/getByUsername")
    CommonResult<User> getByUsername(@RequestParam String username);

//    @PostMapping("/user/update")
    CommonResult update(@RequestBody User user);

//    @PostMapping("/user/delete/{id}")
    CommonResult delete(@PathVariable Long id);
}
