<#import "rightBuilder.ftl" as rightBuilder />
<#import "leftBuilder.ftl" as leftBuilder />

<@buildApiDiff/>

<#macro buildApiDiff>
    <div class="api-wrapper">
        <#list full?values[0].tags as tag>
            <span class="block-tag-section">
                <@buildBlockTagSection tag tag?index/>
            </span>
        </#list>
    </div>
</#macro>

<#macro buildBlockTagSection tag index>
    <div class="tag-section left">
        <@leftBuilder.buildLeftBlockTag tag index/>
    </div>

    <div class="tag-section right">
        <@rightBuilder.buildRightBlockTag tag index/>
    </div>
</#macro>

