@echo off
echo Maven Wrapper - 簡化版本
echo 正在檢查 Java 環境...

java -version
if %ERRORLEVEL% NEQ 0 (
    echo 錯誤：找不到 Java
    exit /b 1
)

echo Java 環境正常
echo 請手動安裝 Maven 或使用 IDE 執行測試
pause 