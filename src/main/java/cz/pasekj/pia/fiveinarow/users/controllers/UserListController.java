package cz.pasekj.pia.fiveinarow.users.controllers;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.support.PaginatedResultsRetrievedEvent;
import cz.pasekj.pia.fiveinarow.support.ResourceNotFoundException;
import cz.pasekj.pia.fiveinarow.users.UserInfo;
import cz.pasekj.pia.fiveinarow.users.services.AllUsersService;
import cz.pasekj.pia.fiveinarow.users.services.CurrentUserService;
import cz.pasekj.pia.fiveinarow.users.services.FriendsService;
import cz.pasekj.pia.fiveinarow.users.services.OnlineUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.webjars.NotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserListController {

    private final ApplicationEventPublisher eventPublisher;
    private final OnlineUsersService onlineUsersService;
    private final CurrentUserService currentUserService;
    private final AllUsersService allUsersService;
    private final FriendsService friendsService;

    @GetMapping(value = "/users/all", params = {"page", "size"})
    List<UserInfo> getAllUsers(@RequestParam("page") int page,
                             @RequestParam("size") int size,
                             UriComponentsBuilder uriBuilder,
                             HttpServletResponse response) {
        String currentUsername = currentUserService.getCurrentUserName();

        Page<UserInfo> users = allUsersService.getAllUsers(PageRequest.of(page, size), currentUsername);
        users.getContent().forEach(userInfo -> {
            userInfo.friend = friendsService.areFriends(currentUsername, userInfo.username);
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

        String currentUsername = currentUserService.getCurrentUserName();

        Page<UserInfo> onlineUsers = onlineUsersService.getLoggedInUsers(PageRequest.of(page, size), currentUsername);
        onlineUsers.getContent().forEach(userInfo -> {
            userInfo.friend = friendsService.areFriends(currentUsername, userInfo.username);
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

        String currentUsername = currentUserService.getCurrentUserName();

        Page<UserInfo> users = friendsService.getFriendsOf(currentUsername, PageRequest.of(page, size));
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
