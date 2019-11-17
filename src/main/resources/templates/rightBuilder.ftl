<#import "common.ftl" as common>

<#macro buildRightBlockTag tag index>
    <#assign RenderUtils=statics['com.differ.differcore.utils.RightRenderUtils'].Companion>

    <#assign tagName=RenderUtils.attributeValue(tag, "name")>
    <#assign tagDescription=RenderUtils.attributeValue(tag, "description")>

    <h4 class="block-tag">
        <a class="nostyle" href="#/${tagName}"><span>${tagName}</span></a>
        <small>${tagDescription}</small>
        <@common.buildExpandButton/>
    </h4>

    <div class="block-content">
        <#list full?values[0].paths as path, methods>
            <#if methods?has_content>
                <#list methods as method, content>
                    <#if RenderUtils.containsMethod(left, path, method)>
                        <#if right?values[0].paths[path][method]??>
                            <#assign leftContent=right?values[0].paths[path][method]/>
                            <#if RenderUtils.shouldRenderMethod(tagName, leftContent)>
                                <@common.renderMethod method path leftContent.summary "" ""/>
                            </#if>
                        </#if>
                    <#else>
                        <#if RenderUtils.shouldRenderMethod(tagName, content)>
                            <#assign contentSummary=RenderUtils.attributeValue(content, "summary")>
                            <@common.renderMethod method path contentSummary ""/>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </div>
</#macro>