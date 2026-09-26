# VirtualOrientation

Готовый проект для сборки APK через GitHub Actions.

## Как получить APK

1. Создай новый репозиторий на GitHub.
2. Загрузи в него **все файлы и папки из этого проекта**.
3. Открой вкладку **Actions**.
4. Выбери workflow **Build APK**.
5. Нажми **Run workflow**.
6. После завершения открой запуск workflow.
7. Внизу страницы в разделе **Artifacts** скачай `VirtualOrientation-debug`.
8. В ZIP будет `app-debug.apk`.

Проект не требует AndroidIDE или Android Studio для сборки на GitHub.

Приложение читает акселерометр и вычисляет сглаженные Pitch/Roll. Оно НЕ регистрирует системный TYPE_GYROSCOPE.
