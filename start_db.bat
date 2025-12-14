@echo off
echo Запуск PostgreSQL для тестов...
echo.

pg_ctl start -D "C:\Program Files\PostgreSQL\18\data"

if %errorlevel% equ 0 (
    echo ✓ Сервер PostgreSQL запущен успешно
    echo   База данных: testdb
    echo   Пользователь: postgres
    echo   Порт: 5432
) else (
    echo ✗ Не удалось запустить сервер PostgreSQL
    echo.
    echo Возможные причины:
    echo 1. Сервер уже запущен
    echo 2. Неправильный путь к данным
    echo 3. Проблемы с правами доступа
)

echo.
echo Для остановки сервера запустите stop_db.bat
echo.
pause