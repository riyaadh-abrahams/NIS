## Getting Started

Run all java applications from the root directory. The relative paths in the code are all set up to work when running from the root directory.
Do not run from within the src directory. You can compile from the src directory

## How to Compile Everything (Windows)

javac -sourcepath src/ChatApp*.java -cp "lib/*;src"

if that does not work then:

    cd src
    del *.class
    javac *.java -cp ../lib/*

## How to Compile Everything (Linux)

    cd src
    rm *.class
    javac *.java -cp ../lib/*

## How to Run (Windows)

It is recommened that you use 2 terminals side by side. One for the server and the other for the client.
Run all these commands from the root directory

1) `Run the Server`
    java -cp "src;lib/*" ChatAppServer

2) `Run the Client` (You will be asked to enter your name)
    java -cp "src;lib/*" ChatAppClient

3) `Send message`
    Once all the certificates are verified you can send a message

## How to Run (Linux)

It is recommened that you use 2 terminals side by side. One for the server and the other for the client.
Run all these commands from the root directory

1) `Run the Server`
    java -cp "src:lib/*" ChatAppServer

2) `Run the Client` (You will be asked to enter your name)
    java -cp "src:lib/*" ChatAppClient

3) `Send message`
    Once all the certificates are verified you can send a message



