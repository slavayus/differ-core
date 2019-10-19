<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Differ service</title>
    <link rel="stylesheet" type="text/css" href="/css/main.css">
    <link href='https://fonts.googleapis.com/css?family=Bitter' rel='stylesheet'>
</head>
<body>
<div class="differ-ui">
    <div class="topbar">
        <a>
            <span>Differ</span>
        </a>
    </div>
</div>
<label>
    <input type="text" placeholder="${name}">
</label>

<table>
    <#list persons as row>
        <tr>
            <#list row as field>
                <td>${field}</td>
            </#list>
        </tr>
    </#list>
</table>

</body>
</html>