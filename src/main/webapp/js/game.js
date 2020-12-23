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
        console.log(msgOut);
    });

});

const offset = 20;
const fieldSize = 40;
const playerEnum = {"empty":0, "white":1, "black":2};
var turn = playerEnum.white;
var board = []


function drawBoard(width=16, height=16) {
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
            board[i][j] = playerEnum.empty;
        }
    }
}

function onCanvasClick(e) {
    var eventX = e.offsetX - offset;
    var eventY = e.offsetY - offset;

    if(eventX < 0 || eventY < 0) return;

    var x = Math.floor(eventX / fieldSize);
    var y = Math.floor(eventY / fieldSize);

    if(x >= board.length || y>= board[0].length) return;

    if(board[x][y] !== playerEnum.empty) return;
    draw(x, y, turn);
    checkWin(x, y, turn);
    turn = (turn === playerEnum.white) ? playerEnum.black : playerEnum.white;
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
    if (turn === playerEnum.black) {
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
    if(player === playerEnum.white) {
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
