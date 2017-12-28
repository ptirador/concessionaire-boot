/**
 * Event to delete cars.
 */
window.deleteEventCar = {
    'click .fa-trash-o': function (e, value, row) {
        e.preventDefault();

        // Hide previous messages.
        $("span[class^='text-']").hide();

        $('#confirmDelete')
            .data('id', row.id)
            .modal('show');
        $("#lblTitleConfirmDelete").html(confirmDeleteCarTitle);
        $("#lblMsgConfirmDelete").html(confirmDeleteCarMsg);

        confirmDelete = objConfirmCar;
    }
};

var objConfirmCar = {
    runConfirmFunction: function () {
        var $table = $('#carsListTb');

        // Hide previous messages.
        hideMessage();

        var id = $('#confirmDelete').data('id');
        var url = 'cars/delete/' + id;

        $.ajax({
            url: url,
            type: 'DELETE'
        })
            .done(function () {
                // Delete row by id.
                $table.bootstrapTable('removeByUniqueId', id);
                showMessage($table, deleteCarOK, false);
            })
            .fail(function () {
                showMessage($table, deleteCarKO, true);
            });

        return true;
    }
};