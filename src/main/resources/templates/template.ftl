<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Differ service</title>
    <link rel="stylesheet" type="text/css" href="/css/main.css">
    <link href='https://fonts.googleapis.com/css?family=Bitter' rel='stylesheet'>
</head>
<body>
<div class="topbar">
    <a> <span>Differ</span> </a>
</div>

<div class="differ-ui">
    <div class="wrapper">
        <#list full?values[0].tags as tag>
            <span class="block-tag-section">
                <@buildTagSection tag "left"/>
                <@buildTagSection tag "right"/>
            </span>
        </#list>
    </div>
</div>
</body>
</html>

<#macro buildTagSection tag position>
    <div class="tag-section ${position}">
        <@buildBlockTag tag/>
    </div>
</#macro>

<#macro buildBlockTag tag>
    <h4 class="block-tag">
        <a class="nostyle" href="#/${tag.name}"><span>${tag.name}</span></a>
        <small>${tag.description}</small>
        <button class="expand-operation" title="Expand operation">
            <svg class="arrow" width="20" height="20">
                <use href="#large-arrow" xlink:href="#large-arrow"></use>
            </svg>
        </button>
    </h4>
</#macro>