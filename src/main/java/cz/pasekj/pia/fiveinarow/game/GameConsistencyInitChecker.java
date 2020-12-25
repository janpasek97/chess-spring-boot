package cz.pasekj.pia.fiveinarow.game;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Component
@Transactional
@RequiredArgsConstructor
public class GameConsistencyInitChecker implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO check consistency of redis database
    }

}
