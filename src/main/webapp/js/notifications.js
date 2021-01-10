
var socket = new SockJS('/secured/friends');
var stompClient = Stomp.over(socket);
var sessionId = "";
var toastID = 0;

/**
 * Connect to WS endpoint
 */
stompClient.connect({}, function (frame) {
    var url = stompClient.ws._transport.url;
    url = url.replace(
        "ws://localhost:8080/secured/friends/",  "");
    url = url.replace("/websocket", "");
    url = url.replace(/^[0-9]+\//, "");
    sessionId = url;

    stompClient.subscribe('/secured/notification/queue/specific-user'
        + '-user' + sessionId, function (msgOut) {
        handleNotification(JSON.parse(msgOut.body));
    });

});

/**
 * Add a new friend request
 * @param username username to add to a friendlist
 */
function addFriend(username) {
    var from = $("#usernameHidden").val();
    var to = username;

    stompClient.send("/app-ws/secured/friends", {},
        JSON.stringify({'from':from, 'to':to, "action": "FRIENDS_ADD"}));
}

/**
 * Remove  friend button event
 * @param username username to remove from the friends list
 */
function removeFriend(username) {
    var from = $("#usernameHidden").val();
    var to = username;

    stompClient.send("/app-ws/secured/friends", {},
        JSON.stringify({'from':from, 'to':to, "action": "FRIENDS_REMOVE"}));
}

/**
 * Accept a fried request
 * @param username username of the friend
 */
function acceptFriend(username) {
    var from = $("#usernameHidden").val();
    var to = username;
    stompClient.send("/app-ws/secured/friends", {},
        JSON.stringify({'from':from, 'to':to, "action": "FRIENDS_ACCEPT"}));
}

/**
 * Refuse a friend request
 * @param username username whose friendship to refuse
 */
function refuseFriend(username) {
    var from = $("#usernameHidden").val();
    var to = username;
    stompClient.send("/app-ws/secured/friends", {},
        JSON.stringify({'from':from, 'to':to, "action": "FRIENDS_REFUSE"}));
}

/**
 * Handle notification message received
 * @param message received message
 */
function handleNotification(message) {
   if(message.action === "FRIENDS_ADD") {
       handleAddFriendNotification(message);
   } else if(message.action === "FRIENDS_ACCEPT") {
       handleFriendsAcceptNotification(message);
   } else if(message.action === "FRIENDS_REMOVE") {
       refresh();
   }
}

/**
 * Display accept friend notification
 * @param message received message
 */
function handleFriendsAcceptNotification(message){
    toastID++;
    toast = '<div id="toast-'+toastID+'" class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-autohide="false">';
    toast += '<div class="toast-header">'
    toast += '<strong class="mr-auto">Friends request accepted</strong>';
    toast += '<small class="text-muted">now</small>';
    toast += '<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">';
    toast += '<span aria-hidden="true">&#215;</span>';
    toast += '</button>';
    toast += '</div>';
    toast += '<div class="toast-body align-content-center">';
    toast += 'User '+message.from+" confirmed your friendship request! <br/>";
    toast += '</div>';
    toast += '</div>';
    $("#toastContainer").append(toast);
    $("#toast-"+toastID).toast("show");
    refresh();
}

/**
 * Display friendship request notification
 * @param message recevied message
 */
function handleAddFriendNotification(message) {
    toastID++;
    toast = '<div id="toast-'+toastID+'" class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-autohide="false">';
    toast += '<div class="toast-header">'
    toast += '<strong class="mr-auto">Friends request</strong>';
    toast += '<small class="text-muted">now</small>';
    toast += '<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">';
    toast += '<span aria-hidden="true">&#215;</span>';
    toast += '</button>';
    toast += '</div>';
    toast += '<div class="toast-body align-content-center">';
    toast += 'User '+message.from+" asked you to become friends! <br/>";
    toast += '<span class="align-content-center w-100" style="display: inline-flex">';
    toast += '<button type="button" class="btn btn-success ml-auto mt-2" onclick="acceptFriend(\''+ message.from +'\'); refresh();" data-dismiss="toast">Accept</button>';
    toast += '<button type="button" class="btn btn-danger mr-auto ml-1 mt-2" onclick="refuseFriend(\''+ message.from +'\')" data-dismiss="toast">Refuse</button>';
    toast += '</span>';
    toast += '</div>';
    toast += '</div>';
    $("#toastContainer").append(toast);
    $("#toast-"+toastID).toast("show");
}