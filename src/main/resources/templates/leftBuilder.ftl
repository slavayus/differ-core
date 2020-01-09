<#import "common.ftl" as common>

<#macro buildLeftBlockTag tag index>
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
                    <#if leftRenderer.shouldRenderMethod(tagName, content)>
                        <#if leftRenderer.isFullyRemovedMethod(left, right, path, method)>
                            <@renderFullyRemovedMethod path method tagName/>
                        <#else >
                            <div class="opblock opblock-${method}">
                                <#if content.summary?is_hash>
                                    <@common.renderMethodHeader method path content.summary.leftValue() "" "removed"/>
                                <#else>
                                    <@common.renderMethodHeader method path content.summary ""/>
                                </#if>

                                <@renderMethodBody content path method/>
                            </div>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </div>

</#macro>


<#macro renderMethodBody content path method>
    <div class="opblock-content">
        <div class="opblock-section-header">
            <div class="tab-header">Parameters</div>
        </div>
        <div class="opblock-section-parameters">
            <#if (content.parameters)??>
                <table class="parameters">
                    <thead>
                    <tr>
                        <th class="col col_header parameters-col_name">Name</th>
                        <th class="col col_header parameters-col_description">Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list content.parameters as parameter>
                        <tr class="parameters">
                            <td class="col parameters-col_name">
                                <#if !leftRenderer.containsParameter(right, path, method, "name", parameter?index)>
                                    <div class="parameter__name <#if parameter.name?is_hash>removed</#if>">
                                        ${leftRenderer.attributeValue(parameter, "name")}
                                        <label class="parameter__name <#if parameter.required?is_hash>removed</#if> <#if leftRenderer.attributeValue(parameter,"required")!false>required</#if>"></label>
                                    </div>
                                </#if>
                                <#if !leftRenderer.containsParameter(right, path, method, "type", parameter?index)>
                                    <div class="parameter__type <#if (parameter.type)?? && parameter.type?is_hash>removed</#if>">
                                        ${leftRenderer.attributeValue(parameter, "type")!""}
                                    </div>
                                </#if>
                                <#if !leftRenderer.containsParameter(right, path, method, "type", parameter?index)>
                                    <div class="parameter__in <#if (parameter.in)?? && parameter.in?is_hash>removed</#if>">
                                        (${leftRenderer.attributeValue(parameter, "in")!""})
                                    </div>
                                </#if>
                            </td>


                            <td class="col parameters-col_description">
                                <#if !leftRenderer.containsParameter(right, path, method, "description", parameter?index)>
                                    <div class="parameter__description <#if parameter.description?is_hash>removed</#if>">
                                        ${leftRenderer.attributeValue(parameter, "description")}
                                    </div>
                                </#if>
                                <#if (parameter.default)?? && !leftRenderer.containsParameter(right, path, method, "default", parameter?index)>
                                    <p class="parameter__default">
                                        <i>Default value : </i>
                                        <label class="<#if parameter.default?is_hash>removed</#if>">${leftRenderer.attributeValue(parameter, "default")}</label>
                                    </p>
                                </#if>
                                <#if (parameter.x\-example)?? && !leftRenderer.containsParameter(right, path, method, "x-example", parameter?index)>
                                    <p class="parameter__example">
                                        <i>Example value : </i>
                                        <label class="<#if parameter.x\-example?is_hash>removed</#if>">${leftRenderer.attributeValue(parameter, "x-example")}</label>
                                    </p>
                                </#if>
                                <#if (parameter.enum)?? && !leftRenderer.containsParameter(right, path, method, "enum", parameter?index)>
                                    <p class="parameter__available">
                                        <i>Available values : </i>
                                        <label class="<#if parameter.enum?is_hash>removed</#if>">${leftRenderer.attributeValue(parameter, "enum")}</label>
                                    </p>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>


            <#else>
                <div class="opblock-description-wrapper"><p>No parameters</p></div>
            </#if>
        </div>
    </div>
</#macro>

<#macro renderFullyRemovedMethod path method tagName>
    <#assign leftContent=left?values[0].paths[path][method]/>
    <#if leftRenderer.shouldRenderMethod(tagName, leftContent)>
        <div class="opblock opblock-${method}">
            <@common.renderMethodHeader method path leftContent.summary "removed" "removed"/>
            <@renderMethodBody leftContent path method/>
        </div>
    </#if>
</#macro>