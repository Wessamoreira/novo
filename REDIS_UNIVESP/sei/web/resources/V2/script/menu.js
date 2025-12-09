(function($) {
    $(document).ready(function() {
        $(".nav1 .li1").click(function(e) {
            e.preventDefault();
            $(this).find("img").attr("src", "./javax.faces.resource/nav-less.png.jsf?ln=imagens");
            $(this).siblings().slideDown(250);
            $(this).parent("li").siblings().find("ul.nav2").slideUp(250);
            $(this).parent("li").siblings().find("img").attr("src", "./javax.faces.resource/nav-more.png.jsf?ln=imagens");
        });
    });
}(jQuery));