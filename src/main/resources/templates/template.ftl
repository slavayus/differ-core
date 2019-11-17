<!DOCTYPE html>
<#import "head.ftl" as head />
<#import "rightBuilder.ftl" as rightBuilder />
<#import "leftBuilder.ftl" as leftBuilder />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Differ service</title>
    <link rel="stylesheet" type="text/css" href="/css/main.css">
    <link rel="stylesheet" type="text/css" href="/css/main.css">
    <script type="text/javascript" src="/js/jquery/jquery-3.4.1.js"></script>
    <script type="text/javascript" src="/js/main.js"></script>

    <link href='https://fonts.googleapis.com/css?family=Bitter' rel='stylesheet'>

    <@head.buildSvgIcons/>
</head>
<body>
<div class="topbar">
    <a> <span>Differ</span> </a>
</div>

<div class="differ-ui">
    <div class="wrapper">
        <#list full?values[0].tags as tag>
            <span class="block-tag-section">
                <@buildBlockTagSection tag tag?index/>
            </span>
        </#list>
    </div>
</div>
</body>
</html>

<#macro buildBlockTagSection tag index>
    <div class="tag-section left">
        <@leftBuilder.buildLeftBlockTag tag index/>
    </div>

    <div class="tag-section right">
        <@rightBuilder.buildRightBlockTag tag index/>
    </div>
</#macro>

