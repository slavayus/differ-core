<#import "rightBuilder.ftl" as rightBuilder />
<#import "leftBuilder.ftl" as leftBuilder />

<@buildApiDiff/>

<#macro buildApiDiff>
    <div class="api-wrapper">
        <#if errorMessage??>
            <@buildApiVersionError/>
        <#else>
            <@buildApiList/>
        </#if>
    </div>
</#macro>

<#macro buildApiList>
    <#list full?values[0].tags as tag>
        <span class="block-tag-section">
                <@buildBlockTagSection tag tag?index/>
            </span>
    </#list>
</#macro>

<#macro buildBlockTagSection tag index>
    <div class="tag-section left">
        <@leftBuilder.buildLeftBlockTag tag index/>
    </div>

    <div class="tag-section right">
        <@rightBuilder.buildRightBlockTag tag index/>
    </div>
</#macro>

<#macro buildApiVersionError>
    <div class="api-wrapper">
        <div class="error-message">
            <label>An error occurred while getting version file: ${errorMessage}</label>
        </div>
    </div>
</#macro>

