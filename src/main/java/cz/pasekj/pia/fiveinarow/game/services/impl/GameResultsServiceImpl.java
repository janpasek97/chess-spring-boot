package cz.pasekj.pia.fiveinarow.game.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.GameResultEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.game.GameResultInfo;
import cz.pasekj.pia.fiveinarow.game.services.GameResultService;
import cz.pasekj.pia.fiveinarow.users.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service("gameResultsService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class GameResultsServiceImpl implements GameResultService {

    @Value("${dateTimeFormat}")
    private String dateTimeFormat;
    private final UserInfoService userInfoService;

    @Override
    public List<GameResultInfo> currentUserResults() {

        List<GameResultInfo> result = new ArrayList<>();
        UserEntity currentUser = userInfoService.getCurrentUser();

        if(currentUser != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);

            List<GameResultEntity> resultEntities = new ArrayList<>();
            resultEntities.addAll(currentUser.getWinnings());
            resultEntities.addAll(currentUser.getLosses());
            resultEntities.sort(new GameResultDateComparator());
            Collections.reverse(resultEntities);

            resultEntities.forEach(gameResultEntity -> {
                if (gameResultEntity.getWinner() == currentUser) {
                    result.add(new GameResultInfo(GameResultInfo.Result.WIN, gameResultEntity.getTimestamp().format(formatter), gameResultEntity.getLoser().getUsername()));
                } else {
                    result.add(new GameResultInfo(GameResultInfo.Result.DEFEAT, gameResultEntity.getTimestamp().format(formatter), gameResultEntity.getWinner().getUsername()));
                }
            });
        }

        return result;
    }

    private static class GameResultDateComparator implements Comparator<GameResultEntity> {

        @Override
        public int compare(GameResultEntity o1, GameResultEntity o2) {
            return o1.getTimestamp().compareTo(o2.getTimestamp());
        }
    }

}
