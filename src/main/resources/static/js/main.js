$(document).ready(function () {
    $('.block-tag').click(function () {
        $(this).parents(".block-tag-section").find(".block-content").toggle();
        setupExpandOperationSvgIcon(this);
    });
});

function setupExpandOperationSvgIcon(element) {
    let useTag = $(element).parents(".block-tag-section").find("use");
    let currentSvg = useTag.attr('href');
    if (currentSvg === "#large-arrow-down") {
        useTag.attr('href', '#large-arrow');
    } else {
        useTag.attr('href', '#large-arrow-down');
    }
}