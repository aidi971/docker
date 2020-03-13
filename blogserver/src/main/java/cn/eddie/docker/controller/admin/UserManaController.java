package cn.eddie.docker.controller.admin;

import cn.eddie.docker.Main;
import cn.eddie.docker.bean.RespBean;
import cn.eddie.docker.bean.Role;
import cn.eddie.docker.bean.User;
import cn.eddie.docker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserManaController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public List<User> getUserByNickname(String nickname) {
        return userService.getUserByNickname(nickname);
    }

    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @RequestMapping(value = "/roles",method = RequestMethod.GET)
    public List<Role> getAllRole(){
        return userService.getAllRole();
    }

    @RequestMapping(value = "/user/enabled",method = RequestMethod.PUT)
    public RespBean updateUserEnable(Boolean enabled,Long uid){
        if (userService.updateUserEnabled(enabled, uid)==1) {
            return new RespBean("success", "更新成功");
        }else {
            return new RespBean("error","更新失败");
        }
    }

    @RequestMapping(value = "/user/{uid}",method = RequestMethod.DELETE)
    public RespBean deleteUserById(@PathVariable Long uid){
        if (userService.deleteUserById(uid)==1){
            return new RespBean("success","删除成功");
        }else {
            return new RespBean("error", "删除失败");
        }
    }

    @RequestMapping(value = "/user/role",method = RequestMethod.PUT)
        public RespBean updateUserRoles(Long[] rids,long id){
        if (userService.updateUserRoles(rids,id)==rids.length){
            return new RespBean("success","更新成功");
        }else{
            return new RespBean("error","更新失败");
        }
    }
}
