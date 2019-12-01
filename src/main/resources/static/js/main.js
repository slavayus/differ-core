const BASE_URL = "/v1/differ/";

$(function () {
    $('.differ-ui').on("click", ".block-tag", function () {
        $(this).parents(".block-tag-section").find(".block-content").toggle();
        setupExpandOperationSvgIcon(this);
    });

    $(".differ-ui").on("click", ".opblock-summary", function () {
        const classes = $(this).attr('class').split(" ").map(value => "." + value).join('');
        $(this).parents(".block-tag-section").find(classes).next().toggle();
    })
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
        $(".api-wrapper").replaceWith(data);
        history.pushState({}, null, "?left=" + leftVersion + "&right=" + rightVersion);
    })
}