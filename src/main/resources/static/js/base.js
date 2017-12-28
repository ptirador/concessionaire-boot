$(function () {
    sidebarMenuActions();
    confirmActions();
    setupCsrfToken();
    goBack();
});

function sidebarMenuActions() {
    var $sidebar = $('#sidebar');
    var link = $sidebar.find("a");

    var firstLi = $sidebar.find('li').first();
    var firstUl = firstLi.find('ul');
    if (firstUl.length > 0) {
        firstUl.slideDown();
    }

    var url = window.location;
    var clickedElem = link.filter(function () {
        return this.href === url.href;
    });

    if (clickedElem.length > 0) {
        if (firstUl.length > 0) {
            clickedElem.parents('ul').first().addClass('active');
        } else {
            clickedElem.parents('li').first().addClass('active');
        }
    }
}

function confirmActions() {
    $('#btnYesConfirmDelete').click(function () {
        var closeWindow = confirmDelete.runConfirmFunction();
        if (closeWindow) {
            $('#confirmDelete').modal('hide');
        }
    });
    $('#btnNoConfirmDelete').click(function () {
        $('#confirmDelete').modal('hide');
    });
}

/**
 * Method used before running any Ajax request.
 * Sets up the header to add the CRSF token, which avoids malicious attacks.
 */
function setupCsrfToken() {
    // Configurar cabecera para insertar CSRF
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var csrfToken = $("meta[name='_csrf']").attr("content");

    $(document).ajaxSend(function(e, xhr, options) {
        if(csrfHeader && csrfToken) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        }
    });
}

/**
 * Adds a trash icon to a table column.
 * @returns {String}
 */
function deleteFormatter() {
    return '<a href="#"><i class="fa fa-trash-o fa-lg"></i></a>';
}


/**
 * Shows a message before the table passed as parameter.
 *
 * @param table Table before which the message is shown.
 * @param message Message to show.
 * @param isError true if error message, false otherwise.
 */
function showMessage(table, message, isError) {
    if (isError) {
        table.before("<p class='alert alert-danger'><i class='fa fa-times' aria-hidden='true'></i> " + message + "</p>");
    } else {
        table.before("<p class='alert alert-success'><i class='fa fa-check' aria-hidden='true'></i> " + message + "</p>");
    }

    fadeMessage();
}

function fadeMessage() {
    $("p[class^='alert']").fadeIn().delay(5000).fadeOut();
}

function hideMessage() {
    $("p[class^='alert']").remove();
}

function goBack() {
    $('a.back').click(function () {
        parent.history.back();
        return false;
    });
}