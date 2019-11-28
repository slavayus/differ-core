const BASE_URL = "/v1/differ/";

$(document).ready(function () {
    $('.block-tag').click(function () {
        $(this).parents(".block-tag-section").find(".block-content").toggle();
        setupExpandOperationSvgIcon(this);
    });
});

function setupExpandOperationSvgIcon(element) {
    let useTag = $(element).parents(".block-tag-section").find("use");
    let currentSvg = useTag.attr('href');
    useTag.attr('href', currentSvg === "#large-arrow-down" ? '#large-arrow' : '#large-arrow-down');
}

function versionChanged() {
    let selectVersion = $('.version-select option:selected');
    let leftVersion = $(selectVersion[0]).text();
    let rightVersion = $(selectVersion[1]).text();
    $.get(BASE_URL + "version", {left: leftVersion, right: rightVersion}).done(function (data) {
        $(".api-wrapper").html(data);
    })
}