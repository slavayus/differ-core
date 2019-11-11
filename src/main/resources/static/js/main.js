$(document).ready(function () {
    $('.block-tag').click(function () {
        $(this).parents(".block-tag-section").find(".block-content").toggle();
    });
});