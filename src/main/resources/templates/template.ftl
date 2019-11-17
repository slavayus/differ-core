<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Differ service</title>
    <link rel="stylesheet" type="text/css" href="/css/main.css">
    <link rel="stylesheet" type="text/css" href="/css/main.css">
    <script type="text/javascript" src="/js/jquery/jquery-3.4.1.js"></script>
    <script type="text/javascript" src="/js/main.js"></script>

    <link href='https://fonts.googleapis.com/css?family=Bitter' rel='stylesheet'>

    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
         style="position:absolute;width:0;height:0">
        <defs>

            <symbol viewBox="0 0 20 20" id="large-arrow">
                <path d="M13.25 10L6.109 2.58c-.268-.27-.268-.707 0-.979.268-.27.701-.27.969 0l7.83 7.908c.268.271.268.709 0 .979l-7.83 7.908c-.268.271-.701.27-.969 0-.268-.269-.268-.707 0-.979L13.25 10z"/>
            </symbol>

            <symbol viewBox="0 0 20 20" id="large-arrow-down">
                <path d="M17.418 6.109c.272-.268.709-.268.979 0s.271.701 0 .969l-7.908 7.83c-.27.268-.707.268-.979 0l-7.908-7.83c-.27-.268-.27-.701 0-.969.271-.268.709-.268.979 0L10 13.25l7.418-7.141z"/>
            </symbol>

        </defs>
    </svg>
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
    <#assign RenderUtils=statics['com.differ.differcore.utils.LeftRenderUtils'].Companion>
    <h4 class="block-tag">

        <#if tag.name?is_hash><#assign tagName=tag.name.leftValue()><#else><#assign tagName=tag.name!""></#if>

        <#if tag.name?is_hash>
            <a class="nostyle removed" href="#/${tagName}"><span>${tagName}</span></a>
        <#else>
            <a class="nostyle" href="#/${tagName}"><span>${tagName}</span></a>
        </#if>

        <#if tag.description?is_hash>
            <small class="removed">${tag.description.leftValue()}</small>
        <#else>
            <small>${tag.description}</small>
        </#if>

        <@buildExpandButton/>

    </h4>

    <div class="block-content">
        <#list full?values[0].paths as path, methods>
            <#if methods?has_content>
                <#list methods as method, content>
                    <#if RenderUtils.containsMethod(right, path, method)>
                        <#if left?values[0].paths[path][method]??>
                            <#assign leftContent=left?values[0].paths[path][method]/>
                            <#if RenderUtils.shouldRenderMethod(tagName, leftContent)>
                                <@renderMethod method path leftContent.summary "removed" "removed"/>
                            </#if>
                        </#if>
                    <#else >
                        <#if RenderUtils.shouldRenderMethod(tagName, content)>
                            <#if content.summary?is_hash>
                                <@renderMethod method path content.summary.leftValue() "" "removed"/>
                            <#else>
                                <@renderMethod method path content.summary ""/>
                            </#if>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </div>

</#macro>

<#macro buildRightBlockTag tag index>
    <#assign RenderUtils=statics['com.differ.differcore.utils.RightRenderUtils'].Companion>

    <#assign tagName=RenderUtils.attributeValue(tag, "name")>
    <#assign tagDescription=RenderUtils.attributeValue(tag, "description")>

    <h4 class="block-tag">
        <a class="nostyle" href="#/${tagName}"><span>${tagName}</span></a>
        <small>${tagDescription}</small>
        <@buildExpandButton/>
    </h4>

    <div class="block-content">
        <#list full?values[0].paths as path, methods>
            <#if methods?has_content>
                <#list methods as method, content>
                    <#if RenderUtils.containsMethod(left, path, method)>
                        <#if right?values[0].paths[path][method]??>
                            <#assign leftContent=right?values[0].paths[path][method]/>
                            <#if RenderUtils.shouldRenderMethod(tagName, leftContent)>
                                <@renderMethod method path leftContent.summary "" ""/>
                            </#if>
                        </#if>
                    <#else>
                        <#if RenderUtils.shouldRenderMethod(tagName, content)>
                            <#assign contentSummary=RenderUtils.attributeValue(content, "summary")>
                            <@renderMethod method path contentSummary ""/>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </div>
</#macro>

<#macro renderMethod method path content removedMethod removedDescription="">
    <span>
        <div class="opblock opblock-${method}">
            <div class="opblock-summary">
                <span class="opblock-summary-method ${removedMethod}">${method}</span>
                <span class="opblock-summary-path ${removedMethod}">${path}</span>
                <div class="opblock-summary-description ${removedDescription}">${content}</div>
            </div>
        </div>
    </span>
</#macro>

<#macro if if then else=""><#if if>${then}<#else>${else}</#if></#macro>

<#macro buildExpandButton>
    <button class="expand-operation" title="Expand operation">
        <svg class="arrow" width="20" height="20">
            <use href="#large-arrow" xlink:href="#large-arrow"></use>
        </svg>
    </button>
</#macro>