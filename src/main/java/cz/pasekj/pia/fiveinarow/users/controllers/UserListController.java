package cz.pasekj.pia.fiveinarow.users.controllers;

import cz.pasekj.pia.fiveinarow.game.services.InGameHandlerService;
import cz.pasekj.pia.fiveinarow.support.PaginatedResultsRetrievedEvent;
import cz.pasekj.pia.fiveinarow.support.ResourceNotFoundException;
import cz.pasekj.pia.fiveinarow.users.UserInfo;
import cz.pasekj.pia.fiveinarow.users.services.AllUsersService;
import cz.pasekj.pia.fiveinarow.users.services.UserInfoService;
import cz.pasekj.pia.fiveinarow.users.services.FriendsService;
import cz.pasekj.pia.fiveinarow.users.services.OnlineUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserListController {

    private final ApplicationEventPublisher eventPublisher;
    private final OnlineUsersService onlineUsersService;
    private final UserInfoService userInfoService;
    private final AllUsersService allUsersService;
    private final FriendsService friendsService;
    private final InGameHandlerService inGameService;

    @GetMapping(value = "/users/all", params = {"page", "size"})
    List<UserInfo> getAllUsers(@RequestParam("page") int page,
                             @RequestParam("size") int size,
                             UriComponentsBuilder uriBuilder,
                             HttpServletResponse response) {
        String currentUsername = userInfoService.getCurrentUserName();

        Page<UserInfo> users = allUsersService.getAllUsers(PageRequest.of(page, size), currentUsername);
        users.getContent().forEach(userInfo -> {
            userInfo.friend = friendsService.areFriends(currentUsername, userInfo.username);
            userInfo.inGame = inGameService.isInGame(userInfo.username);
        });

        if(page > users.getTotalPages()) {
            throw new ResourceNotFoundException("Page access out of range");
        }

        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                UserInfo.class,
                uriBuilder,
                response,
                page,
                users.getTotalPages(),
                size));

        return users.getContent();
    }

    @GetMapping(value = "/users/online", params = {"page", "size"})
    List<UserInfo> getOnlineUsers(@RequestParam("page") int page,
                                  @RequestParam("size") int size,
                                  UriComponentsBuilder uriBuilder,
                                  HttpServletResponse response){

        String currentUsername = userInfoService.getCurrentUserName();

        Page<UserInfo> onlineUsers = onlineUsersService.getLoggedInUsers(PageRequest.of(page, size), currentUsername);
        onlineUsers.getContent().forEach(userInfo -> {
            userInfo.friend = friendsService.areFriends(currentUsername, userInfo.username);
            userInfo.inGame = inGameService.isInGame(userInfo.username);
        });

        if (page > onlineUsers.getTotalPages()) {
            throw new ResourceNotFoundException("Page access out of range");
        }

        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                UserInfo.class, uriBuilder,
                response,
                page,
                onlineUsers.getTotalPages(),
                size));

        return onlineUsers.getContent();
    }

    @GetMapping(value = "/users/friends", params = {"page", "size"})
    List<UserInfo> getFriendsList(@RequestParam("page") int page,
                                  @RequestParam("size") int size,
                                  UriComponentsBuilder uriBuilder,
                                  HttpServletResponse response) {

        String currentUsername = userInfoService.getCurrentUserName();

        Page<UserInfo> users = friendsService.getFriendsOf(currentUsername, PageRequest.of(page, size));
        users.getContent().forEach(userInfo -> {
            userInfo.inGame = inGameService.isInGame(userInfo.username);
        });
        if(page > users.getTotalPages()) {
            throw new ResourceNotFoundException("Page access out of range");
        }

        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                UserInfo.class,
                uriBuilder,
                response,
                page,
                users.getTotalPages(),
                size));

        return users.getContent();
    }
}
