# Tetris Clone Project

### What is this project ?
	This is one of the clones of epic tetris game, developed from scratch with standard 
	java libraries by me. And if you interested you can check everything behind a project 
	that created by an amator video-game programmer.
### What is the goal of the project ? 
	Just practising for beginning to developing games. Rookies of the game developing 
	hobbyists may want to check this project, to see how to develop game without using 
	any tools that prepared for game development like presents in game engines.
	
### How can I test a released version of the project ?
	For now there is well-tested version but still it is pre-released state. Because, still there 
	are somethings that I want to add to the project but you can download pre-release version
	to test,play and enjoy with. Also if you want to build project on your local and to do some 
	contribution by following the steps at below, you are always welcome. Please contact with me 
	if you are interested that much.
	
* Release Page : https://github.com/taner1es/Tetris/releases
	
## How to build it?


### Prerequisites

You only need a Oracle JavaSE Development kit 9.0.4 or upper. Below versions may work but
I didn't try and to avoid any problem that is why I recommend.

## IF you already have JDK installed on your local system, you can skip to step 4

### STEP 1 - Download JavaSE SDK ZIP

```
https://www.oracle.com/technetwork/java/javase/downloads/index.html
```

### STEP 2 - Open PowerShell CLI and Create a new folder to work on

```
mkdir C:\gitClonedProject
```
### STEP 3 - Extract files to the folder that we created (don't forget to replace pre-fix of downloaded zip file path)

```
Expand-Archive [your downloads directory path will be here]\jdk-11.0.1_windows-x64_bin.zip C:\gitClonedProject\
```

### STEP 4 - get tetris project files to your local computer

```
git clone https://github.com/taner1es/Tetris.git C:\gitClonedProject\Tetris
```

### STEP 5 - now we're ready to build project 

Switch Current Directory : 
```
cd C:\gitClonedProject\Tetris\Tetris\src\tetris\code\
```
Compile the source code:
```
C:\gitClonedProject\jdk-11.0.1\bin\javac.exe -cp ../../ ./Tetris.java
```
Run the game to test your binaries works fine or not: 
```
C:\gitClonedProject\jdk-11.0.1\bin\java.exe -cp ../../ tetris.code.Tetris
```

## Built With

* [TODO]

## Contributing

* You're always welcome to submit a bug,advice or pull request
* You can contact me with taner.esmrglu@gmail.com

## Versioning

* [TODO]

## Author(s)

* **Taner Esmeroğlu** - *Starter* - [taner1es](https://github.com/taner1es)


## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE.md](LICENSE) file for details
