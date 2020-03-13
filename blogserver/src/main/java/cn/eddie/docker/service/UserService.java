package cn.eddie.docker.service;


import cn.eddie.docker.bean.Role;
import cn.eddie.docker.bean.User;
import cn.eddie.docker.mapper.RolesMapper;
import cn.eddie.docker.mapper.UserMapper;
import cn.eddie.docker.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.security.PublicKey;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RolesMapper rolesMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(s);
        if (user == null) {
            //避免返回null
            return new User();
        }
        List<Role> roles = rolesMapper.getRolesByUid(user.getId());
        user.setRoles(roles);
        return user;
    }

    /**
     * @param user
     * @return 0 成功
     * 1 用户名重复
     * 2 失败
     */
    public int reg(User user) {
        User loadUserByUsername = userMapper.loadUserByUsername(user.getUsername());
        if (loadUserByUsername != null) {
            return 1;
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setEnable(true);
        long result = userMapper.reg(user);
        String[] roles = new String[]{"2"};
        int i = rolesMapper.addRoles(roles, user.getId());
        boolean b = i == roles.length && result == 1;
        if (b) {
            return 0;
        } else {
            return 2;
        }
    }

    public int updateUserEmail(String email) {
        return userMapper.updateUserEmail(email, Util.getCurrentUser().getId());
    }

    public List<User> getUserByNickname(String nickname){
        List<User>list = userMapper.getUserByNickname(nickname);
        return list;
    }

    public List<Role> getAllRole(){
        return userMapper.getAllRole();
    }

    public int updateUserEnabled(Boolean enable,Long uid){
        return userMapper.updateUserEnabled(enable,uid);
    }

    public int deleteUserById(Long uid){
        return userMapper.deleteUserById(uid);
    }

    public int updateUserRoles(Long[] rids,Long id){
        int i =userMapper.deleteUserById(id);
        return userMapper.setUserRoles(rids,id);
    }

    public User getUserById(Long id){
        return userMapper.getUserById(id);
    }
}
