package cz.pasekj.pia.fiveinarow.game.services;

import cz.pasekj.pia.fiveinarow.game.GameResultInfo;

import java.util.List;

public interface GameResultService {
    List<GameResultInfo> currentUserResults();
}
