<#import "common.ftl" as common>

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

        <@common.buildExpandButton/>

    </h4>

    <div class="block-content">
        <#list full?values[0].paths as path, methods>
            <#if methods?has_content>
                <#list methods as method, content>
                    <#if RenderUtils.containsMethod(right, path, method)>
                        <#if left?values[0].paths[path][method]??>
                            <#assign leftContent=left?values[0].paths[path][method]/>
                            <#if RenderUtils.shouldRenderMethod(tagName, leftContent)>
                                <@common.renderMethod method path leftContent.summary "removed" "removed"/>
                            </#if>
                        </#if>
                    <#else >
                        <#if RenderUtils.shouldRenderMethod(tagName, content)>
                            <#if content.summary?is_hash>
                                <@common.renderMethod method path content.summary.leftValue() "" "removed"/>
                            <#else>
                                <@common.renderMethod method path content.summary ""/>
                            </#if>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </div>

</#macro>