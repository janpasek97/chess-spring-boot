var gameSocket = new SockJS('/secured/game');
var gameStompClient = Stomp.over(gameSocket);
var gameSessionId = "";

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

const offset = 20;
const fieldSize = 40;
const playerEnum = {"EMPTY":0, "WHITE":1, "BLACK":2};
var turn = playerEnum.EMPTY;
var myColor = playerEnum.EMPTY;
var board = []

function handleMessage(message) {
    if(message.action === "COUNTER_MOVE") {
        draw(msgParsed.x, msgParsed.y, playerEnum[msgParsed.playerColor]);
        checkWin(msgParsed.x, msgParsed.y, turn);
        turn = (playerEnum[msgParsed.playerColor] === playerEnum.WHITE) ? playerEnum.BLACK : playerEnum.WHITE;
        changeTurnImage();
    }
    else if(message.action === "CONNECT_DATA"){
        turn = playerEnum[message.playerOnMove];
        changeTurnImage();

        myColor = playerEnum[message.playerColor];
        if(myColor === playerEnum.WHITE) {
            $("#youPicture").attr("src", "/img/white_player.png");
        } else {
            $("#youPicture").attr("src", "/img/black_player.png");
        }
        var boardWidth = message.board.length;
        var boardHeight = message.board[0].length;
        drawBoard(message.board, boardWidth, boardHeight);

    }
}

function drawBoard(initBoard, width=16, height=16) {
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
    stompClient.send("/app-ws/secured/game", {}, JSON.stringify(
        {
            "action":"CONNECT",
            "playerOnMove": null,
            "playerColor": null,
            "x": 0,
            "y": 0,
            "board": null
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
            "playerColor":colorName,
            "playerOnMove": null,
            "x": x,
            "y": y,
            "board": null
        }));

    checkWin(x, y, turn);
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

function onWin(player) {
    if(player === playerEnum.WHITE) {
        window.alert("White player win!");
    } else {
        window.alert("Black player win!");
    }
}

function checkWin(x, y, player) {
    // check horizontal
    var cntr = 0;
    for (var ix=0; ix < board.length; ix++) {
        if (board[ix][y] === player) {
            cntr++;
            if(cntr >= 5) {
                onWin(player);
                return;
            }
        }
        else {
            cntr = 0;
        }
    }

    // check vertical
    cntr = 0;
    for (var iy=0; iy < board[0].length; iy++) {
        if (board[x][iy] === player) {
            cntr++;
            if(cntr >= 5) {
                onWin(player);
                return;
            }
        }
        else {
            cntr = 0;
        }
    }

    // check diagonal
    cntr = 0;
    var sx;
    var sy;
    if(x >= y) {
        sx = x - y;
        sy = 0;

    } else {
        sx = 0;
        sy = y - x;
    }

    while (sx < board.length && sy < board[0].length) {

        if(board[sx][sy] === player) {
            cntr++;
            if (cntr >= 5) {
                onWin(player);
                return;
            }
        } else {
            cntr = 0;
        }

        sx++;
        sy++;
    }

    // check inversed diagonal
    cntr = 0;
    if(x + y < board.length) {
        sx = x + y;
        sy = 0;
    } else {
        var sum = x + y;
        sx = board.length - 1;
        sy = sum - sx;
    }

    while (sx >= 0 && sy < board[0].length) {

        if(board[sx][sy] === player) {
            cntr++;
            if(cntr >= 5) {
                onWin(player);
                return;
            }
        } else {
            cntr = 0;
        }

        sx--;
        sy++;
    }
}
