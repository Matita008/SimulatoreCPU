@echo off

set "API=http://api.github.com/repos/matita008/SimulatoreCPU/releases/latest"
set "JAVAURL=https://download.oracle.com/java/24/latest/jdk-24_windows-x64_bin.zip"
set "DATADIR=%APPDATA%\matita008\CPUSim"
set "TMPDIR=%DATADIR%\tmp"
set "JAVAZIP=%TMPDIR%\java.zip"
set "RELEASEINFO=%DATADIR%\releaseInfo.json"
set "JARFILE=%DATADIR%\CPUSim.jar"
set "EXE=start.exe"
set "ICON=icon.ico"
set "ICOFILE=%DATADIR%\assets\%icon%"
set "DESC=Launch CPU Emulator"
set "VBSTMPSCRIPT=%temp%\mkshortcut.vbs"
set "RUNFILE=%DATADIR%\run.bat"
set "VISIBILITYFILE=%DATADIR%\showconsole"
set "CONFIGFILE=%DATADIR%\params.list"

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
FOR /F "usebackq" %%i IN (`powershell -c "(Get-FileHash -Algorithm SHA256 %JAVAZIP%).hash"`) DO set "fileHash=%%~I"

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
tar -xf j%JAVAZIP% -C %DATADIR%\java
for /f "usebackq tokens=* delims=" %%A in (`where /R %DATADIR%\java java.exe `) DO set "JAVAEXE=%%A"

echo Pulling latest release info
curl --ssl-no-revoke -L -s %API% >%RELEASEINFO%

echo Searching for download link
set "JAR_URL="
for /f "usebackq tokens=* delims=" %%A in (`findstr /i "browser_download_url" %RELEASEINFO% ^| findstr /i ".jar"`) DO set "JAR_URL1=%%A"
set "JAR_URL1=!JAR_URL1:"browser_download_url": =!"
set "JAR_URL1=!JAR_URL1: =!"
set "JAR_URL1=!JAR_URL1:"=!"
set "JAR_URL=!JAR_URL1!"

echo Downloading latest release from %JAR_URL% into %JARFILE% ...
curl --ssl-no-revoke -L -o %JARFILE% %JAR_URL%

echo Creating execution script...

(
 echo @echo off
 echo java -jar %JARFILE% %%*
 echo Program exited with code %%ERRORLEVEL%%
 echo echo This window will close in 2 minutes unless a key is pressed
 echo timeout /T 120
) >run.bat

echo Downloading icon...
for /f "usebackq tokens=* delims=" %%A in (`findstr /i "browser_download_url" %RELEASEINFO% ^| findstr /i ".ico"`) DO set "ICO_URL1=%%A"
set "ICO_URL1=!ICO_URL1:"browser_download_url": =!"
set "ICO_URL1=!ICO_URL1: =!"
set "ICO_URL1=!ICO_URL1:"=!"
set "ICO_URL=!ICO_URL1!"

curl --ssl-no-revoke -L -o %ICOFILE% %ICO_URL%

echo Creating config files
if not exist "%VISIBILITYFILE%" echo false > "%VISIBILITYFILE%"
if not exist "%CONFIGFILE%" echo. >%CONFIGFILE%

set "RunHiddenVBS=%DATADIR%\runhidden.vbs"
(
echo Set WshShell = CreateObject("WScript.Shell")
echo WshShell.Run """" ^& WScript.Arguments(0) ^& """", 0, False
) > "%RunHiddenVBS%"

(
echo @echo off
echo setlocal
echo for /f "usebackq delims=" %%%%A in ("%VISIBILITYFILE%") do set "MODE=%%%%A"
echo for /f "usebackq delims=" %%%%A in ("%CONFIGFILE%") do set "params=%%%%A"
echo if /I "%%MODE%%"=="false" (
echo     cscript //nologo "%RunHiddenVBS%" "%JAVAEXE% %JARFILE% %%params%%"
echo ) else (
echo     call "%JAVAEXE% %JARFILE% %%params%%"
echo )
) > "%RUNFILE%"

echo Creating shortcut to desktop and start menu
(
echo Set oWS = CreateObject("WScript.Shell")
echo sLinkFile = WScript.Arguments(0)
echo sTarget = WScript.Arguments(1)
echo sIcon = WScript.Arguments(2)
echo Set oLink = oWS.CreateShortcut(sLinkFile)
echo oLink.TargetPath = sTarget
echo If sIcon ^<^> "" Then oLink.IconLocation = sIcon
echo oLink.Save
) > "%VBSTMPSCRIPT%"

cscript //nologo "%VBSTMPSCRIPT%" "%StartMenu%\%ProgramName%.lnk" %RUNFILE% %ICOFILE%
cscript //nologo "%VBSTMPSCRIPT%" "%Desktop%\%ProgramName%.lnk" %RUNFILE% %ICOFILE%