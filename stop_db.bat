@echo off
echo Остановка PostgreSQL...
echo.

pg_ctl stop -D "C:\Program Files\PostgreSQL\18\data"

if %errorlevel% equ 0 (
    echo ✓ Сервер PostgreSQL остановлен успешно
    echo   Память освобождена
) else (
    echo ℹ Не удалось остановить сервер PostgreSQL
    echo   Возможно, он уже не запущен
)

echo.
pause