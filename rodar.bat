@echo off
cd /d "%~dp0"
javac -encoding UTF-8 -cp "lib/*;src" src/view/BibliotecaView.java
java -cp "lib/*;src" view.BibliotecaView
pause