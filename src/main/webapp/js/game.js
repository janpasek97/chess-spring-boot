var gameSocket = new SockJS('/secured/game');
var gameStompClient = Stomp.over(gameSocket);
var gameSessionId = "";
var initialized = false;

gameStompClient.connect({}, function (frame) {
    var url = gameStompClient.ws._transport.url;
    url = url.replace(
        "ws://localhost:8080/secured/game/",  "");
    url = url.replace("/websocket", "");
    url = url.replace(/^[0-9]+\//, "");
    gameSessionId = url;

    gameStompClient.subscribe('/secured/notification/queue/specific-user'
        + '-user' + gameSessionId, function (msgOut) {
        msgParsed = JSON.parse(msgOut.body);
        handleMessage(msgParsed);
    });

    initGame();
});

async function reload(time = 500) {
    if (time > 0) {
        await sleep(time);
    }
    location.reload();
}

const offset = 20;
const fieldSize = 40;
const playerEnum = {"EMPTY":0, "WHITE":1, "BLACK":2};
var turn = playerEnum.EMPTY;
var myColor = playerEnum.EMPTY;
var board = []

var requestFrom;
var requestWidth;
var requestHeight;

function handleMessage(message) {
    if(message.action === "COUNTER_MOVE") {
        draw(message.x, message.y, playerEnum[msgParsed.playerColor]);
        turn = (playerEnum[msgParsed.playerColor] === playerEnum.WHITE) ? playerEnum.BLACK : playerEnum.WHITE;
        changeTurnImage();
    }
    else if(message.action === "CONNECT_DATA"){
        initialized = true;
        turn = playerEnum[message.playerOnMove];
        changeTurnImage();

        myColor = playerEnum[message.playerColor];
        if(myColor === playerEnum.WHITE) {
            $("#youPicture").attr("src", "/img/white_player.png");
        } else {
            $("#youPicture").attr("src", "/img/black_player.png");
        }
        $('#opponentName').html(message.opponent);
        var boardWidth = message.board.length;
        var boardHeight = message.board[0].length;
        drawBoard(message.board, boardWidth, boardHeight);
    } else if (message.action === "START"){
        requestFrom = message.opponent;
        requestWidth = message.x;
        requestHeight = message.y;

        $("#gameRequestWidth").html(message.x);
        $("#gameRequestHeight").html(message.y);
        $("#gameRequestFrom").html(message.opponent);
        $("#gameRequestModal").modal("show");
    } else if (message.action === "ACCEPT") {
        reload(500);
    } else if (message.action === "WIN") {
        onWin();
    } else if (message.action === "LOSE") {
        onLose();
    } else if (message.action === "MESSAGE") {
        messageReceived(message.message);
    }
}

function acceptGame(){
    stompClient.send("/app-ws/secured/game", {}, JSON.stringify(
        {
            "action":"ACCEPT",
            "opponent": requestFrom,
            "playerOnMove": null,
            "playerColor": null,
            "x": requestWidth,
            "y": requestHeight,
            "board": null,
            "message": null
        }));
    reload(500);
}

function newGame(user, opponent, width, height) {
    stompClient.send("/app-ws/secured/game", {}, JSON.stringify(
        {
            "action":"START",
            "opponent": opponent,
            "playerOnMove": null,
            "playerColor": null,
            "x": width,
            "y": height,
            "board": null,
            "message": null
        }));
}

function drawBoard(initBoard, width=16, height=16) {
    $("#boardLoading").hide();
    $("#board").show();

    var canvas = document.getElementById("board");
    var context = canvas.getContext('2d');

    context.canvas.width = width * fieldSize + 2*offset;
    context.canvas.height = height * fieldSize + 2*offset;

    for(var i=0; i < width+1; i++) {
        context.moveTo(offset + fieldSize * i, offset);
        context.lineTo(offset + fieldSize * i, offset + height * fieldSize);
        context.strokeStyle = "#cc9966";
        context.stroke();
    }

    for(var j=0; j < height+1; j++) {
        context.moveTo(offset, offset + fieldSize * j);
        context.lineTo(offset + width * fieldSize, offset + fieldSize * j);
        context.strokeStyle = "#cc9966";
        context.stroke();
    }

    for (var i=0; i < width; i++) {
        board[i] = [];
        for (var j=0; j < height; j++) {
            board[i][j] = playerEnum[initBoard[i][j]];
            if(playerEnum[initBoard[i][j]] !== playerEnum.EMPTY) {
                draw(i, j, playerEnum[initBoard[i][j]]);
            }
        }
    }
}

function initGame() {
    if(initialized === true) return;
    stompClient.send("/app-ws/secured/game", {}, JSON.stringify(
        {
            "action":"CONNECT",
            "opponent": "",
            "playerOnMove": null,
            "playerColor": null,
            "x": 0,
            "y": 0,
            "board": null,
            "message": null
        }));
}

function onCanvasClick(e) {
    if(turn !== myColor) return;
    var eventX = e.offsetX - offset;
    var eventY = e.offsetY - offset;

    if(eventX < 0 || eventY < 0) return;

    var x = Math.floor(eventX / fieldSize);
    var y = Math.floor(eventY / fieldSize);

    if(x >= board.length || y>= board[0].length) return;

    if(board[x][y] !== playerEnum.EMPTY) return;
    draw(x, y, turn);

    colorName = turn === playerEnum.WHITE ? "WHITE" : "BLACK";
    stompClient.send("/app-ws/secured/game", {}, JSON.stringify(
        {
            "action":"MOVE",
            "opponent": "",
            "playerColor":colorName,
            "playerOnMove": null,
            "x": x,
            "y": y,
            "board": null,
            "message": null
        }));

    turn = (turn === playerEnum.WHITE) ? playerEnum.BLACK : playerEnum.WHITE;
    changeTurnImage();
}

function changeTurnImage(){
    if(turn === playerEnum.WHITE) {
        $("#turnPicture").attr("src", "/img/white_player.png");
    } else {
        $("#turnPicture").attr("src", "/img/black_player.png");
    }
}

function draw(x, y, turn) {
    board[x][y] = turn;

    var canvas = document.getElementById('board');
    var context = canvas.getContext('2d');

    var dx = offset + fieldSize * x + fieldSize/2;
    var dy = offset + fieldSize * y + fieldSize/2;

    context.beginPath();
    context.arc(dx, dy, 15, 0, 2 * Math.PI);
    context.closePath();

    var gradient = context.createRadialGradient(dx, dy, 5, dx, dy, 15)
    if (turn === playerEnum.BLACK) {
        gradient.addColorStop(0, "#cccccc");
        gradient.addColorStop(1, "#000000");
        context.fillStyle = gradient;
    } else {
        gradient.addColorStop(0, "#ffffff");
        gradient.addColorStop(1, "#cccccc");
        context.fillStyle = gradient;
    }
    context.fill();
}

function onLose() {
    window.alert("You lost the game :-(");
    window.location = "/";
}

function onWin() {
    window.alert("You won the game :-)");
    window.location = "/";
}

function messageReceived(msg) {
    $("#noMessage").hide();

    var snd = new Audio("/wav/message.wav"); // buffers automatically when created
    snd.play();

    var msgElement = ""
    msgElement += '<div class="message-box-holder">';
    msgElement += '<div class="message-box message-partner">';
    msgElement += msg;
    msgElement += '</div>';
    msgElement += '</div>';

    $("#chatContainer").append(msgElement);
    $('#chatContainer').scrollTop($('#chatContainer')[0].scrollHeight);
}

function messageSend() {
    $("#noMessage").hide();
    var msg = $("#chatText").val();
    if(msg === "") return;

    $("#chatText").val("");
    var msgElement = ""
    msgElement += '<div class="message-box-holder">';
    msgElement += '<div class="message-box">';
    msgElement += msg;
    msgElement += '</div>';
    msgElement += '</div>';

    $("#chatContainer").append(msgElement);
    $('#chatContainer').scrollTop($('#chatContainer')[0].scrollHeight);

    stompClient.send("/app-ws/secured/game", {}, JSON.stringify(
        {
            "action":"MESSAGE",
            "opponent": requestFrom,
            "playerOnMove": null,
            "playerColor": null,
            "x": null,
            "y": null,
            "board": null,
            "message": msg
        }));
}

function surrender() {
    stompClient.send("/app-ws/secured/game", {}, JSON.stringify(
        {
            "action":"SURRENDER",
            "opponent": requestFrom,
            "playerOnMove": null,
            "playerColor": null,
            "x": null,
            "y": null,
            "board": null,
            "message": null
        }));
}