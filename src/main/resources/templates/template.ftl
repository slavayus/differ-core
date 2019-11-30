<!DOCTYPE html>
<#import "head.ftl" as head />
<#import "apiBuilder.ftl" as apiBuilder />
<#import "spring.ftl" as spring />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Differ service</title>
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
    <div class="version-section">
        <span class="block-tag-section">
            <#if versions??>
                <span class="tag-section version left">
                    <label>Select version
                        <select class="version-select" onchange="versionChanged()">
                            <#list versions as version>
                                <#assign selectedVersion = leftRenderer.versionSelected(versions, RequestParameters.left)!""/>
                                <option <#if version == selectedVersion>selected</#if>>${version}</option>
                            </#list>
                        </select>
                    </label>
                </span>

                <span class="tag-section version right">
                    <label>Select version
                        <select class="version-select" onchange="versionChanged()">
                            <#list versions as version>
                                <#assign selectedVersion = rightRenderer.versionSelected(versions, RequestParameters.right)!""/>
                                <option <#if version == selectedVersion>selected</#if>>${version}</option>
                            </#list>
                        </select>
                    </label>
                </span>
            </#if>
        </span>
    </div>

    <@apiBuilder.buildApiDiff/>
</div>
</body>
</html>