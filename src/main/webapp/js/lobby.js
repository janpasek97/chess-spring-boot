var navAll = $("#navAll");
var navFriends = $("#navFriends");
var navOnline = $("#navOnline");
var currentPage = 0;
var lastLoadURL = ""
const usersPerPage = 4;

$('#removeFriendModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var user = button.data('whatever');
    var modal = $(this);
    modal.find('#removeFriendModalContent').html("Do you really want to remove user <b>"+ user +"</b> from your friends?");
    modal.find('#removeFriendConfirmBtn').attr("onclick", "removeFriend('"+ user +"'); refresh();");
});

async function refresh(time = 500) {
    if (time > 0) {
        await sleep(time);
    }
    loadUsersFromUrl(lastLoadURL, currentPage, usersPerPage);
}

function navOnlineActive() {
    currentPage = 0;
    navAll.removeClass("active");
    navFriends.removeClass("active");
    navOnline.addClass("active");
    loadUsersFromUrl("/users/online", currentPage, usersPerPage);
}

function navFriendsActive() {
    currentPage = 0;
    navAll.removeClass("active");
    navFriends.addClass("active");
    navOnline.removeClass("active");
    loadUsersFromUrl("/users/friends", currentPage, usersPerPage);
}

function navAllActive() {
    currentPage = 0;
    navAll.addClass("active");
    navFriends.removeClass("active");
    navOnline.removeClass("active");
    loadUsersFromUrl("/users/all", currentPage, usersPerPage)
}

function loadUsersFromUrl(url, page, size) {
    lastLoadURL = url;
    var pagination = {"page": page, "size": size};
    $.getJSON(url, pagination, function (data, status) {

    }).done(function (data, status, xhr) {
        var linkHeader = xhr.getResponseHeader("Link");
        var linkInfo = parseLinkHeader(linkHeader);
        displayPagination(linkInfo);
        $("#userListContainer").html("");
        jQuery.each(data, function (i, item){
            var svg = multiavatar(item.username);
            var username = item.username;
            var online = item.online;
            var inGame = item.inGame;
            var friends = item.friend;
            var userInfo= '';
            userInfo = '<div class="row user-item">';
            userInfo += '<div class="col-1 user-avatar">' + svg + '</div>';
            userInfo += '<div class="col-6" style="margin-top: auto; margin-bottom: auto;">';
            userInfo += username;
            if(online) {
                userInfo += '<img src="/img/online_dot.png" class="ml-2" width="15px" height="15px" alt="connection status"/>';
            } else {
                userInfo += '<img src="/img/offline_dot.png" class="ml-2" width="15px" height="15px" alt="connection status"/>'
            }
            if(inGame) {
                userInfo += '<span class="badge badge-light ml-2">In game</span>';
            }
            userInfo += '</div>'
            userInfo += '<div class="col-2 ml-auto">';
            if(!friends) {
                userInfo += '<span><button class="btn btn-primary" onclick="addFriend(\'' + username + '\')"><img src="/img/add-friend.png" width="20px" class="btn-img"/></button></span>';
            } else {
                userInfo += '<span><button class="btn btn-warning" onclick="" data-toggle="modal" data-target="#removeFriendModal" data-whatever="' + username + '"><img src="/img/remove-friend.png" width="20px" class="btn-img"/></button></span>';
            }
            if(online && !inGame) {
                userInfo += '<span><button class="btn btn-success ml-2" onclick="startGame(\''+ username +'\')"><img src="/img/play.png" width="20px" class="btn-img"/></button></span>';
            } else {
                userInfo += '<span><button class="btn btn-success ml-2" disabled><img src="/img/play.png" width="20px" class="btn-img"/></button></span>';
            }
            userInfo += '</div></div><hr/>'


            var tmp = $("#userListContainer").html();
            tmp += userInfo;
            $("#userListContainer").html(tmp);
        });
    }).fail(function (data, textStatus, xhr) {
        console.log("error", data.status);
        console.log("STATUS: "+xhr);
    });
}

function startGame(username){
    $("#newGameOpponent").val(username);
    $("#newOpponentName").html(username);
    $("#newGameModal").modal("show");
}

function askGame(){
    var currentUser = $("#usernameHidden").val();
    var opponent = $("#newGameOpponent").val();
    var width = $("#newGameWidth").val();
    var height = $("#newGameHeight").val();
    newGame(currentUser, opponent, width, height);
}