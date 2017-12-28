$(function() {
    $.fn.bootstrapSwitch.defaults.onColor = 'success';
    $.fn.bootstrapSwitch.defaults.offColor = 'danger';
    $("[type='checkbox']").bootstrapSwitch();
});