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

<#macro buildExpandButton>
    <button class="expand-operation" title="Expand operation">
        <svg class="arrow" width="20" height="20">
            <use href="#large-arrow" xlink:href="#large-arrow"></use>
        </svg>
    </button>
</#macro>