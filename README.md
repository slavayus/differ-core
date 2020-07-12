# differ-core 
### библиотека для автоматического версионирования API

## Описание
Данная система состоит из 2 частей:
* Differ Core – содержит множество компонент, необходимых для генерации файла с описание API и набор ресурсов HTML, Javascript и CSS для отображения изменений. 
* Differ Commit – содержит Gradle плагин с настроенной задачей для выполнения коммита, а также набор стандартных классов для генерации названия версий.

_Пример готового приложения с данной библиотекой можно найти [тут](//github.com/viusiumbeli/testing-lib)_

## Настройка приложения
```groovy
apply plugin: 'com.differ.differcommit.plugins.DifferCommitPlugin' (1) 

dependencies {
  compile 'com.differ:differ-core:1.0.0' (2)
  compile 'com.differ:differ-commit:1.0.0' (3) 
}

differcommit.shouldRunAfter testIntegration (4)
```

1. Применить DifferCommitPlugin плагин
1. Добавить зависимость differ-core в compile конфигурации 
1. Добавить зависимость differ-commit в compile конфигурации
1. Сделать задачу differcommit зависимой от задачи интеграционных тестов, чтобы коммит в репозиторий выполнился только после интеграционных тестов, иначе нечего будет коммитить.

Данных действий будет достаточно для начала работы. По умолчанию задача `differcommit` будет генерировать названия версий классом [`IncrementVersionGenerator`](//github.com/viusiumbeli/differ-core/blob/master/buildSrc/src/main/kotlin/com/differ/differcommit/naming/generator/IncrementVersionGenerator.kt). Если есть потребность в конфигурации метода генерации названия версий, то необходимо добавить следующий код в build.gradle:

```groovy
task customDifferCommit (type: DifferCommit) { (1) 
  versionGenerator = new TimestampVersionGenerator () (2)
}

class TimestampVersionGenerator implements VersionGenerator { (3)
  @Override
  String generateName () {
    return System.currentTimeMillis().toString() 
  }
}
```

1. Создаем новую задачу. Задача должна быть с типом [`DifferCommit`](//github.com/viusiumbeli/differ-core/blob/master/buildSrc/src/main/kotlin/com/differ/differcommit/tasks/DifferCommit.kt).
1. Инициализировать переменную `versionGenerator` согласно выбранному способу генерации.
1. Создать сам класс для генерации названий. В данном случае достаточно реализовать только [`VersionGenerator`](//github.com/viusiumbeli/differ-core/blob/master/buildSrc/src/main/kotlin/com/differ/differcommit/naming/generator/VersionGenerator.kt),  но при необходимости можно реализовать и интерфейс [`LastFileProvider`](//github.com/viusiumbeli/differ-core/blob/master/buildSrc/src/main/kotlin/com/differ/differcommit/naming/provider/LastFileProvider.kt).

На этом этапе конфигурация системы сборки закончена. Далее необходимо включить в проект поддержку библиотеки Differ. Делается это путем добавления аннотации [`@EnableDiffer`](//github.com/viusiumbeli/differ-core/blob/master/src/main/kotlin/com/differ/differcore/annotations/EnableDiffer.kt) в один из конфигурационных классов. Тут стоит отметить, что данная аннотация заработает только, если запускаемое приложение – это веб-приложение. После того, как библиотека подключена к проекту и сконфигурирована, она начнет слушать события типа [`ApplicationStartedEvent`](//docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/context/event/ApplicationStartedEvent.html) от Spring Boot, и при получении события инициирует сохранение описания API во временный файл. Собственно, для этого и необходимо было настроить задачу коммита после интеграционных тестов.


## Настройка gralde plugin
Данная библиотека поддерживает два способа взаимодействия с удаленным репозиторием, для этого имеются определенные параметры, которые необходимо указать в файле `build.gradle` или `gradle.properties`.

Если отправка на удаленный репозиторий не требуется, то для этого используется параметр `differcommit.versions.git.push-required` со значением `false`. 

### ssh
Необходимо указать два параметра: 
1. `differcommit.versions.git.ssh.rsa.location` – тип данных строка, должен содержать путь в файловой системе до файла с приватным ключом.
1. `differcommit.versions.git.ssh.rsa.password` – тип данных строка, должен содержать пароль от вышеуказанного ключа.

### http
Необходимо указать два параметра: 
1. ```differcommit.versions.git.http.username``` – тип данных строка, должен содержать логин аккаунта, от имени которого будут отправляться коммиты на удаленный репозиторий.
1. ```differcommit.versions.git.http.password``` – тип данных строка, должен содержать пароль от вышеуказанного аккаунта.

При отсутствии какого-либо атрибута система не сможет отправить выполненный коммит на удаленный репозиторий и остановит выполнение с сообщение об ошибке, где будет указан недостающий атрибут.


## Пример
После запуска приложения станут доступны следующие адреса:
1. `/v1/differ` – выдает полную страницу с изменениями в формате HTML
2. `/v1/differ/version` – выдает только блок с изменения в формате HTML. Удобно использовать в ajax запросах, чтобы при выборе другой версии не обновлять полностью страницу, а только часть
3. `/v1/differ` с `MediaType.APPLICATION_JSON_VALUE` – отдает изменения между версиями в формате json

![asdf](https://github.com/viusiumbeli/differ-core/blob/master/example.png)
