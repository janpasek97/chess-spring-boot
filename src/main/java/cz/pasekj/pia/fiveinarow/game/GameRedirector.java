package cz.pasekj.pia.fiveinarow.game;

import cz.pasekj.pia.fiveinarow.game.services.InGameHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(1)
public class GameRedirector implements Filter {

    private final InGameHandlerService inGameService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String currentUrl = UrlUtils.buildRequestUrl((HttpServletRequest) request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            if(authentication.isAuthenticated() && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
                String username = authentication.getName();
                if(inGameService.isInGame(username)) {
                    if(currentUrl.equals("/") || currentUrl.equals("/profile") || currentUrl.equals("/admin")) {
                        ((HttpServletResponse)response).sendRedirect("/game");
                        return;
                    }
                } else {
                    if(currentUrl.equals("/game")) {
                        ((HttpServletResponse)response).sendRedirect("/");
                        return;
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

}
