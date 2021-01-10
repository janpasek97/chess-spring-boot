/**
 * Sleeep given time of milliseconds
 * @param ms time to sleep in milliseconds
 * @returns {Promise<timeout>}
 */
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

/**
 * Get CSFR headers
 * @returns csfr header entry
 */
function getHeaders() {
    var headers = Array();
    headers[_csrfHeaderName] = _csrfToken;
    return headers;
}

/**
 * Parse pagination meta data from header
 * @param header header to be parse
 * @returns links to first, next, previous and last page
 */
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

/**
 * Display pagination based on available pages
 * @param linkInfo link info parsed from header
 */
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

/**
 * Go to the next page of pagination
 */
function onNextPage() {
    currentPage += 1;
    loadUsersFromUrl(lastLoadURL, currentPage, usersPerPage);
}

/**
 * Go to the previous page of pagination
 */
function onPrevPage() {
    currentPage -= 1;
    loadUsersFromUrl(lastLoadURL, currentPage, usersPerPage);
}