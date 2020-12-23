var currentPage = 0;
var lastLoadURL = ""
const usersPerPage = 4;

var username = "";

function hideError() {
    $("#saveErrorAlert").attr("hidden", "hidden");
}

function hideOk() {
    $("#saveOkAlert").attr("hidden", "hidden");
}

function saveDetails() {
    var userdata = {
        "username" : $('#inputUsernameEdit').val(),
        "email" :  $('#inputEmaiEdit').val(),
        "password" : $('#inputPasswordEdit').val(),
        "roles" : []
    };

    $('.roleCheck').each(function (i, obj) {
        if($(obj).prop("checked")) {
           userdata.roles.push($(obj).val());
        }
    });

    $.ajax('/admin/api', {
        type: 'POST',
        data: JSON.stringify(userdata),
        headers: getHeaders(),
        contentType: 'application/json',
        success: function (data, status, xhr) {
            if(data.success) {
                $("#saveErrorAlert").attr("hidden", "hidden");
                $("#saveOkAlert").removeAttr("hidden");
                loadUsersFromUrl("/users/all", currentPage, usersPerPage);
            } else {
                $("#saveErrorAlert").removeAttr("hidden");
                $("#saveOkAlert").attr("hidden", "hidden");
                $("#errorMessage").html(data.errorMessage);
            }
        }
    })
}

function clearModal(){
    $('#errorModalAlert').attr("hidden", "hidden");
    $('#saveDetailsBtn').removeAttr("disabled");
    $('#inputUsernameEdit').val("");
    $('#inputEmaiEdit').val("");
    $('#inputPasswordEdit').val("");
    $('.roleCheck').each(function (i, obj) {
        $(obj).prop("checked", false);
    });
}

function editUser() {
    username = $("#usernameInput").val();
    $("#editModal").modal("show");
}

$('#editModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var user = button.data('whatever');
    var modal = $(this);

    if(user == null) {
        user = username;
    }
    var url = "/users/details/" + user;
    $('#inputUsernameEdit').val(user);
    $.getJSON(url, function (data, status) {

    }).done(function (data, status, xhr) {
        $('#inputEmaiEdit').val(data.email);
        $.each(data.roles, function (i, role) {
            var id = "#roleCheck" + role;
            $(id).prop("checked", true);
        });
    }).fail(function (data, textStatus, xhr) {
        $('#saveDetailsBtn').attr("disabled", "disabled");
        $('#errorModalAlert').removeAttr("hidden");
        console.log("error", data.status);
        console.log("STATUS: "+xhr);
    });
});


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
            var userInfo= '';
            userInfo = '<div class="row user-item">';
            userInfo += '<div class="col-1 user-avatar">' + svg + '</div>';
            userInfo += '<div class="col-3" style="margin-top: auto; margin-bottom: auto;">';
            userInfo += username;
            if(online) {
                userInfo += '<img src="/img/online_dot.png" class="ml-2" width="15px" height="15px" alt="connection status"/>';
            } else {
                userInfo += '<img src="/img/offline_dot.png" class="ml-2" width="15px" height="15px" alt="connection status"/>'
            }
            userInfo += '</div>'
            userInfo += '<div class="col-2 ml-auto">';
            userInfo += '<span><button class="btn btn-light" data-toggle="modal" data-target="#editModal" data-whatever="' + username + '"><img src="/img/gear.png" width="20px" class="btn-img"/></button></span>';
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