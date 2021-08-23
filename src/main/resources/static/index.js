"use strict";
(function () {
    var cookieName = 'tplCookieConsent'; // The cookie name
    var cookieLifetime = 365; // Cookie expiry in days

    /**
     * Set a cookie
     * @param cname - cookie name
     * @param cvalue - cookie value
     * @param exdays - expiry in days
     */
    var _setCookie = function (cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    };

    /**
     * Get a cookie
     * @param cname - cookie name
     * @returns string
     */
    var _getCookie = function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    };

    /**
     * Should the cookie popup be shown?
     */
    var _shouldShowPopup = function () {
        if (_getCookie(cookieName)) {
            return false;
        } else {
            return true;
        }
    };

    // Show the cookie popup on load if not previously accepted
    if (_shouldShowPopup()) {
        $('#cookieModal').modal('show');
    }

    // Modal dismiss btn - consent
    $('#cookieModalConsent').on('click', function () {
        _setCookie(cookieName, 1, cookieLifetime);
    });

})();

function toggleContent() {
    // Get the DOM reference
    var contentId = document.getElementById("1-query-opinion"); //Insert PHP-ID here! Instead of "1"
    // Toggle 
    contentId.style.display == "block" ? contentId.style.display = "none" :
    contentId.style.display = "block";
    return false;
}