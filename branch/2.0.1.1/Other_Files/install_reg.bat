@echo off
::Get user documents path first
FOR /F "tokens=3 delims= " %%G IN ('REG QUERY "HKCU\Software\Microsoft\Windows\CurrentVersion\Explorer\Shell Folders" /v "Personal"') DO (SET docDir=%%G)

REG ADD HKCU\Software\Normitec /v FreEMerge_InstallLoc /t REG_SZ /d %docDir%\FreEMerge\