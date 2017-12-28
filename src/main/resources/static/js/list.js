$(function () {
    tableActions();
});

function tableActions() {
    var $table = $("#carsListTb");

    $table.on('dbl-click-row.bs.table', function (e, row) {
        window.location.href = 'detail/' + row.id;
    });
}