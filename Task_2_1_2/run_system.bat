@echo off
REM Запускаем мастер-узел с тестовым массивом
start cmd /k java -cp target/classes ru.nsu.vyaznikova.Main master 6 8 7 13 5 9 4

REM Даем мастеру время на запуск
timeout /t 2

REM Запускаем три рабочих узла
start cmd /k java -cp target/classes ru.nsu.vyaznikova.Main worker worker1
start cmd /k java -cp target/classes ru.nsu.vyaznikova.Main worker worker2
start cmd /k java -cp target/classes ru.nsu.vyaznikova.Main worker worker3
