@echo off
setlocal enabledelayedexpansion

set "API=http://api.github.com/repos/matita008/SimulatoreCPU/releases/latest"
set "JAVAURL=https://download.oracle.com/java/24/latest/jdk-24_windows-x64_bin.zip"
set "DATADIR=%APPDATA%\matita008\CPUSim"
set "TMPDIR=%DATADIR%\tmp"
set "JAVAZIP=%TMPDIR%\java.zip"
set "RELEASEINFO=%TMPDIR%\releaseInfo.json"
set "JARFILE=%DATADIR%\CPUSim.jar"
set "ICON=icon.ico"
set "ICOFILE=%DATADIR%\assets\%icon%"
set "DESC=Launch CPU Emulator"
set "VBSTMPSCRIPT=%temp%\mkshortcut.vbs"
set "Launcher=%DATADIR%\run.bat"
set "VISIBILITYFILE=%DATADIR%\showconsole"
set "CONFIGFILE=%DATADIR%\params.list"
set "RunHiddenVBS=%DATADIR%\runhidden.vbs"
set "ProgramName=CPUSim"
set "StartMenu=%AppData%\Microsoft\Windows\Start Menu\Programs"
set "Desktop=%USERPROFILE%\Desktop"
set "StartMenuLnk=%StartMenu%\%ProgramName%.lnk"
set "DesktopLnk=%Desktop%\%ProgramName%.lnk"

if not exist "%TMPDIR%" mkdir "%TMPDIR%"

echo Pulling java release from oracle...
curl --ssl-no-revoke -o %JAVAZIP% %JAVAURL%

echo Pulling file hash from oracle...
FOR /F "usebackq" %%i IN (`curl --ssl-no-revoke -s %JAVAURL%.sha256`) DO set "originHash=%%~i"
REM curl --ssl-no-revoke -o %TMPDIR%\hash.sha256 https://download.oracle.com/java/24/latest/jdk-24_windows-x64_bin.zip.sha256
REM set /P "originHash= " <%TMPDIR%\hash.sha256
(powershell -c "\"%originHash%\".toUpper()") >%TMPDIR%\hash.sha256
set /P "originHash= " <%TMPDIR%\hash.sha256

echo Calculating file hash
FOR /F "usebackq" %%i IN (`powershell -c "(Get-FileHash -Algorithm SHA256 %JAVAZIP%).hash"`) DO set "fileHash=%%~i"

echo Comparing hash...
if %originHash% == %fileHash% goto hash
color 0C
echo The file hash is different from the oracle one, the download may be broken
echo To prevent any problem caused from a broken dowload the program will terminate now
echo.
echo Hash:
echo originHash = %originHash%
echo fileHash = %fileHash%
echo.
echo The script will terminate in 5 minutes unless a key is pressed
timeout /T 300
color
goto :EOF

:hash
echo Hash check completed

echo Exctracting JVM/JRE
if not exist "%DATADIR%\java" mkdir "%DATADIR%\java"
cd %DATADIR%
tar -xf %JAVAZIP% -C java

for /f "usebackq tokens=* delims=" %%A in (`where /R "%DATADIR%\java" java.exe `) DO set "JAVAEXE=%%A"

echo Pulling latest release info
curl --ssl-no-revoke -L -s %API% >%RELEASEINFO%

echo Searching for download link
set "JAR_URL="
for /f "usebackq tokens=* delims=" %%A in (`findstr /i "browser_download_url" "%RELEASEINFO%" ^| findstr /i ".jar"`) DO set "JAR_URL1=%%A"
set "JAR_URL1=!JAR_URL1:"browser_download_url": =!"
set "JAR_URL1=!JAR_URL1: =!"
set "JAR_URL1=!JAR_URL1:"=!"
set "JAR_URL=!JAR_URL1!"

echo Downloading latest release from %JAR_URL% into %JARFILE% ...
curl --ssl-no-revoke -L -o %JARFILE% %JAR_URL%

echo Downloading icon...
for /f "usebackq tokens=* delims=" %%A in (`findstr /i "browser_download_url" "%RELEASEINFO%" ^| findstr /i ".ico"`) DO set "ICO_URL1=%%A"
set "ICO_URL1=!ICO_URL1:"browser_download_url": =!"
set "ICO_URL1=!ICO_URL1: =!"
set "ICO_URL1=!ICO_URL1:"=!"
set "ICO_URL=!ICO_URL1!"

if not exist "%DATADIR%\assets" mkdir "%DATADIR%\assets"

curl --ssl-no-revoke -L -o %ICOFILE% %ICO_URL%
echo Downloaded icon from %ICO_URL% to %ICOFILE%

echo Creating config files
if not exist "%VISIBILITYFILE%" echo false>"%VISIBILITYFILE%"
if not exist "%CONFIGFILE%" echo. >"%CONFIGFILE%"

echo Creating execution script...

echo @echo off                                                                               >"%Launcher%" 
echo setlocal                                                                                >>"%Launcher%"
echo for /f "usebackq delims=" %%%%A in ("%VISIBILITYFILE%") do set "MODE=%%%%A"             >>"%Launcher%"
echo for /f "usebackq delims=" %%%%A in ("%CONFIGFILE%") do set "params=%%%%A"               >>"%Launcher%"
echo set "Target=%JAVAEXE% -jar %JARFILE% %%params%%"                                        >>"%Launcher%"
echo if /I "%%MODE%%"=="true" (                                                              >>"%Launcher%"
echo   start %JAVAEXE% -jar "%JARFILE%" ""%%params%%"" ^>"%DATADIR%\latest.log"              >>"%Launcher%"
echo ) else (                                                                                >>"%Launcher%"
echo   powershell -NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -Command "Start-Process cmd.exe -ArgumentList '/c', ('""%%Target%%""') -WindowStyle Hidden -RedirectStandardOutput ""%DATADIR%\latest.log"" -RedirectStandardError ""%DATADIR%\error.log"""  >>"%Launcher%"
echo )                                                                                       >>"%Launcher%"

echo Creating shortcut to desktop and start menu

for %%d in ("%StartMenuLnk%" "%DesktopLnk%") do (
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ws=New-Object -ComObject WScript.Shell; $s=$ws.CreateShortcut('%%~d'); $s.TargetPath='cmd.exe'; $s.Arguments='/C ""%Launcher%""'; if (Test-Path '%ICOFILE%') { $s.IconLocation='%ICOFILE%' }; $s.Save()"
REM ; $s.WorkingDirectory='%DATADIR%'
)

del /f /s /q %TMPDIR% >nul
rmdir /s /q %TMPDIR%

REM ComSpec
REM C:\WINDOWS\System32\WindowsPowerShell\v1.0\powershell.exe -NoProfile -ExecutionPolicy Bypass -File "C:\Users\matti\AppData\Roaming\matita008\CPUSim\run.bat