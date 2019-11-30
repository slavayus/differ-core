<#import "common.ftl" as common>

<#macro buildRightBlockTag tag index>
    <#assign tagName=rightRenderer.attributeValue(tag, "name")>
    <#assign tagDescription=rightRenderer.attributeValue(tag, "description")>

    <h4 class="block-tag">
        <a class="nostyle" href="#/${tagName}"><span>${tagName}</span></a>
        <small>${tagDescription}</small>
        <@common.buildExpandButton/>
    </h4>

    <div class="block-content">
        <#list full?values[0].paths as path, methods>
            <#if methods?has_content>
                <#list methods as method, content>
                    <#if rightRenderer.containsMethod(left, path, method)>
                        <#if right?values[0].paths[path][method]??>
                            <#assign leftContent=right?values[0].paths[path][method]/>
                            <#if rightRenderer.shouldRenderMethod(tagName, leftContent)>
                                <@common.renderMethod method path leftContent.summary "" ""/>
                            </#if>
                        </#if>
                    <#else>
                        <#if rightRenderer.shouldRenderMethod(tagName, content)>
                            <#assign contentSummary=rightRenderer.attributeValue(content, "summary")>
                            <@common.renderMethod method path contentSummary ""/>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </div>
</#macro>