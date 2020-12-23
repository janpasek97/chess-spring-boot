package cz.pasekj.pia.fiveinarow.game.services.impl;

import cz.pasekj.pia.fiveinarow.game.services.GameHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service("gameHandlerService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class GameHandlerServiceImpl implements GameHandlerService {

}
