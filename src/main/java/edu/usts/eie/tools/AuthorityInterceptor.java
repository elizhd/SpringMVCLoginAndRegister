package edu.usts.eie.tools;



import edu.usts.eie.model.User;
import edu.usts.eie.service.IUserService;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 权限拦截器
 * @Author Haodong Zhao
 */
public class AuthorityInterceptor implements HandlerInterceptor {
    private static final String TAG = "AuthorityInterceptor";
    private static Logger log = Logger.getLogger(TAG);

    @Resource(name = "userService")
    private IUserService userService;

    // 不需要拦截的请求
    private static final String[] IGNORE_URI = {"/loginUser", "/user/logout",
            "/registerUser", "/getSKey", "/login.html",
            "/isRegistered", "/register.html", "/web_resources/bootstrap", "/web_resources/img",
            "/web_resources/tools", "not_manager.html", "not_login.html"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        boolean flag = false; // 默认用户没有登录
        String servletPath = request.getServletPath(); // 获得请求的ServletPath
        log.debug("SERVER[AuthorityInterceptor] ServletPath: " + servletPath);
        String requestUri = request.getRequestURI(); //请求完整路径，可用于登陆后跳转
        String contextPath = request.getContextPath();  //项目下完整路径
        String url = requestUri.substring(contextPath.length()); //请求页面
        log.debug("SERVER[AuthorityInterceptor] Request Uri: " + requestUri);
        log.debug("SERVER[AuthorityInterceptor] Url: " + url);

        //判断请求是否需要拦截
        for (String s : IGNORE_URI) {
            if (servletPath.contains(s)) {
                log.debug("SERVER do not intercept: " + servletPath);
                return true;
            }
        }

        log.debug("SERVER intercept");
        User user = userService.findUserByName((String)request.getSession().getAttribute("userName"));

        if (user == null) {
            log.debug("SERVER do not find user");
            log.debug("SERVER to " + "not_login.html");

            response.sendRedirect(contextPath + "/html/not_login.html");
            // request.getRequestDispatcher( "/html/not_login.html").forward(request, response);
            // response.sendRedirect(contextPath + "/html/not_login.html");
        } else {
            if (!user.isRole()) {
                log.debug("User is not manager");
                log.debug("SERVER to " + "/html/not_manager.html");
                response.sendRedirect(contextPath + "/html/not_manager.html");
                log.debug("SERVER Get User: " + user.toString());
                flag = true;
            }

        }
        return flag;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
