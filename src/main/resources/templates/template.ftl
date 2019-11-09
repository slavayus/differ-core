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
                <@buildBlockTagSection tag tag?index/>
            </span>
        </#list>
    </div>
</div>
</body>
</html>

<#macro buildBlockTagSection tag index>
    <div class="tag-section left">
        <@buildLeftBlockTag tag index/>
    </div>

    <div class="tag-section right">
        <@buildRightBlockTag tag index/>
    </div>
</#macro>

<#macro buildLeftBlockTag tag index>
    <h4 class="block-tag">

        <#if tag.name?is_hash>
            <a class="nostyle removed" href="#/${tag.name.leftValue()}"><span>${tag.name.leftValue()}</span></a>
        <#else>
            <a class="nostyle" href="#/${tag.name}"><span>${tag.name}</span></a>
        </#if>

        <#if tag.description?is_hash>
            <small class="removed">${tag.description.leftValue()}</small>
        <#else>
            <small>${tag.description}</small>
        </#if>

        <button class="expand-operation" title="Expand operation">
            <svg class="arrow" width="20" height="20">
                <use href="#large-arrow" xlink:href="#large-arrow"></use>
            </svg>
        </button>
    </h4>
</#macro>

<#macro buildRightBlockTag tag index>
    <#if tag.name?is_hash><#assign tagName=tag.name.rightValue()><#else><#assign tagName=tag.name></#if>
    <#if tag.description?is_hash><#assign tagDescription=tag.description.rightValue()><#else><#assign tagDescription=tag.description></#if>

    <h4 class="block-tag">
        <a class="nostyle" href="#/${tagName}"><span>${tagName}</span></a>
        <small>${tagDescription}</small>
        <button class="expand-operation" title="Expand operation">
            <svg class="arrow" width="20" height="20">
                <use href="#large-arrow" xlink:href="#large-arrow"></use>
            </svg>
        </button>
    </h4>
</#macro>

<#macro if if then else=""><#if if>${then}<#else>${else}</#if></#macro>