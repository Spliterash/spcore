# SPCORE

Библиотека, написанная для более быстрой разработки приложений на spring, включает в себя базовые классы сущностей и
реализацию mongo репозитория

Подключение:
```groovy
repositories {
    maven {
        url = "https://nexus.spliterash.ru/repository/group/"
    }
}
dependencies {
    implementation 'ru.spliterash:spcore-domain:1.0.0'
}
```