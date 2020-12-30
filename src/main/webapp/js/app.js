function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function getHeaders() {
    var headers = Array();
    headers[_csrfHeaderName] = _csrfToken;
    return headers;
}

function parseLinkHeader(header) {
    var parsed = {
        "first": null,
        "prev": null,
        "next": null,
        "last": null
    };

    if (header != null) {
        var headerParts = header.split(",");
        for (var i = 0; i < headerParts.length; i++) {
            var part = headerParts[i];
            const relRegex = "(?<=rel=\").+?(?=\")";
            const linkRegex = "(?<=<).+?(?=>)"

            var rel = part.match(relRegex);
            var link = part.match(linkRegex);
            if (rel == null || link == null) continue;

            rel = rel[0].toUpperCase();
            link = link[0];

            switch (rel) {
                case "FIRST":
                    parsed.first = link;
                    break;
                case "PREV":
                    parsed.prev = link;
                    break;
                case "NEXT":
                    parsed.next = link;
                    break;
                case "LAST":
                    parsed.last = link;
                    break;
            }
        }
    }
    return parsed;
}

function displayPagination(linkInfo) {
    var dispCurrent = false;

    if(linkInfo.prev != null) {
        $("#navPrev").css("visibility", "visible");
        dispCurrent = true;
    } else {
        $("#navPrev").css("visibility", "hidden");
    }

    if(linkInfo.next != null) {
        $("#navNext").css("visibility", "visible");
        dispCurrent = true;
    } else {
        $("#navNext").css("visibility", "hidden");
    }

    if(dispCurrent) {
        $("#navCurrent").css("visibility", "visible");
    } else {
        $("#navCurrent").css("visibility", "hidden");
    }

    $("#currentPageNr").html(currentPage+1);
}

function onNextPage() {
    currentPage += 1;
    loadUsersFromUrl(lastLoadURL, currentPage, usersPerPage);
}

function onPrevPage() {
    currentPage -= 1;
    loadUsersFromUrl(lastLoadURL, currentPage, usersPerPage);
}