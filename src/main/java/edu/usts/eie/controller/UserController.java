package edu.usts.eie.controller;

import edu.usts.eie.model.User;
import edu.usts.eie.service.IUserService;
import edu.usts.eie.tools.DESTools;
import edu.usts.eie.tools.RSATools;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
public class UserController {
    private String publicKey, privateKey;

    @Resource(name = "userService")
    IUserService userService;
    private static final String TAG = "UserController";
    private static Logger logger = Logger.getLogger(TAG);


    protected void writeJSON2Response(Object out, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            logger.debug("[SERVER] " + out);
            response.getWriter().print(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 用户登录
    @RequestMapping(value = "/loginUser", method = RequestMethod.POST)
    @ResponseBody
    public void userLogin(@RequestParam(value = "userName") String userName,
                          @RequestParam(value = "userPassword") String userPassword,
                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 对前端传输至后端的待登录用户进行RSA解密
        logger.debug("[login] Get encrypted username: " + userName);
        logger.debug("[login] Get decrypted password: " + userPassword);
        userName = RSATools.decrypt(userName, privateKey);
        userPassword = RSATools.decrypt(userPassword, privateKey);
        logger.debug("[login] Decrypted username: " + userName);
        logger.debug("[login] Decrypted password: " + userPassword);

        // 对用户信息进行DES加密
        logger.debug(userName + " " + userPassword);
        Object result = "{\"flag\":false}";
        String eUser = DESTools.encrypt(userName);
        String ePassword = DESTools.encrypt(userPassword);
        if (userService.findByNameAndPassword(eUser, ePassword) != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userName", eUser);
            result = "{\"flag\":true}";
        }

        writeJSON2Response(result, response);
    }

    // 用户注册
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    @ResponseBody
    public void userRegister(@RequestBody User user, HttpServletResponse response) throws Exception {
        String name = user.getName();
        String password = user.getPassword();
        // 对前端传输至后端的待查询用户名进行RSA解密
        logger.debug("[Registering] Get encrypted username: " + name);
        logger.debug("[Registering] Get encrypted password: " + password);
        name = RSATools.decrypt(name, privateKey);
        password = RSATools.decrypt(password, privateKey);
        logger.debug("[Registering]  Decrypted username: " + name);
        logger.debug("[Registering]  Decrypted password: " + password);

        user.setName(DESTools.encrypt(name));
        user.setPassword(DESTools.encrypt(password));

        Object result = "{\"flag\":false}";
        if (userService.insertUser(user) > 0)
            result = "{\"flag\":true}";
        writeJSON2Response(result, response);
    }

    // 用户名检测
    @RequestMapping(value = "/isRegistered", method = RequestMethod.POST)
    @ResponseBody
    public void isRegistered(String name, HttpServletResponse response) throws Exception {
        // 对前端传输至后端的待查询用户名进行RSA解密
        System.out.println("[Searching] Get encrypted username: " + name);
        name = RSATools.decrypt(name, privateKey);
        System.out.println("[Searching] After being decrypt: " + name);

        // 对用户名进行DES加密
        name = DESTools.encrypt(name);
        Object result = "{\"flag\":false}";
        if (userService.findUserByName(name) == null) {
            result = "{\"flag\":true}";
        } else {
            System.out.println(userService.findUserByName(name).toString());
        }

        writeJSON2Response(result, response);
    }

    // 获取公钥
    @RequestMapping(value = "/getSKey", method = RequestMethod.GET)
    @ResponseBody
    public String getSKey(HttpServletResponse response) throws Exception {
        Map<Integer, String> keyMap = RSATools.genKeyPair();  // 生成公钥 私钥
        publicKey = keyMap.get(0);
        privateKey = keyMap.get(1);
        System.out.println("The randomly generated public key is:" + publicKey);
        System.out.println("The randomly generated private key is:" + privateKey);
        return publicKey;
    }


}
