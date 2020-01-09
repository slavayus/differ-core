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
                        <#if (right?values[0].paths[path][method])??>
                            <#assign rightContent=right?values[0].paths[path][method]/>
                            <#if rightRenderer.shouldRenderMethod(tagName, rightContent)>
                                <div class="opblock opblock-${method}">
                                    <@common.renderMethodHeader method path rightContent.summary "" ""/>
                                    <@renderMethodBody rightContent/>
                                </div>
                            </#if>
                        </#if>
                    <#else>
                        <#if rightRenderer.shouldRenderMethod(tagName, content)>
                            <div class="opblock opblock-${method}">
                                <#assign contentSummary=rightRenderer.attributeValue(content, "summary")>
                                <@common.renderMethodHeader method path contentSummary ""/>
                                <@renderMethodBody content/>
                            </div>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </div>
</#macro>


<#macro renderMethodBody content>
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
                                <div class="parameter__name <#if rightRenderer.attributeValue(parameter, "required")!false>required</#if>">
                                    ${rightRenderer.attributeValue(parameter, "name")}
                                </div>
                                <div class="parameter__type">
                                    ${rightRenderer.attributeValue(parameter, "type")!""}
                                </div>
                                <div class="parameter__in">
                                    (${rightRenderer.attributeValue(parameter, "in")!""})
                                </div>
                            </td>

                            <td class="col parameters-col_description">
                                <div class="parameter__description">${rightRenderer.attributeValue(parameter, "description")!""}</div>
                                <#if (parameter.default)??>
                                    <div class="parameter__default">
                                        <p><i>Default value</i>
                                            : ${rightRenderer.attributeValue(parameter, "default")!""}</p>
                                    </div>
                                </#if>
                                <#if (parameter.x\-example)??>
                                    <div class="parameter__example">
                                        <p><i>Example value</i>
                                            : ${rightRenderer.attributeValue(parameter, "x-example")!""}</p>
                                    </div>
                                </#if>
                                <#if (parameter.enum)??>
                                    <div class="parameter__available">
                                        <p><i>Available values</i>
                                            : ${rightRenderer.attributeValue(parameter, "enum")!""}</p>
                                    </div>
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