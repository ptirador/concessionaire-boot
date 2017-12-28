$(function () {
    tableActions();
});

function tableActions() {
    var $table = $("#usersListTb");

    $table.on('dbl-click-row.bs.table', function (e, row) {
        window.location.href = 'users/detail/' + row.id;
    });
}

function roleFormatter(value) {
    var text;
    switch (value) {
        // ADMIN
        case 1:
            text = msgAdministratorRole;
            break;
        // USER
        case 2:
            text = msgUserRole;
            break;
        default:
            text = '--';
            break;
    }

    return text;
}

function rowStyle(row, index) {
    if (!row.active) {
        return {
            classes: 'text-danger'
        };
    }
    return {};
}