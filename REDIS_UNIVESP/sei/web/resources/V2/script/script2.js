(function($) {
    $(document).ready(function() {
        $(".mascaraCpf").mask("999.999.999-99");
        $(".mascaraDinheiro,.precisao2").maskMoney({
            symbol: "R$ ",
            decimal: ",",
            precision: 2,
            thousands: ".",
            allowZero: true
        });
    });
}(jQuery));