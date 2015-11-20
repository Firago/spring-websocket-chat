$(window).load(function () {
    $(".swipe-area").swipe({
        swipeStatus: function (event, phase, direction, distance, duration, fingers) {
            if (phase == "move" && direction == "right") {
                $("#swipe-sidebar").addClass("open");
                return false;
            }
            if (phase == "move" && direction == "left") {
                $("#swipe-sidebar").removeClass("open");
                return false;
            }
        }
    });
});