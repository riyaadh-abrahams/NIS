## Getting Started

Run all commands from the root directory. The reletive paths in the code are all set up to work when running from the root directory.
Do not run from withing the src directory.

## How to Compile Everything

javac -sourcepath src/*.java -cp "lib/*;src"

## How to Run

1) `Run the Server`
    java -cp "src;lib/*" Server

2) `Run the Client` (You will be asked to enter your name)
    java -cp "src;lib/*" Client

3) `Run another Client` (You will 2 clients to send messages to each other)
    java -cp "src;lib/*" Client

4) `Send private message`
    Use `@name <message>` to send messages to other clients
    eg `@Riyaadh Hello World`


