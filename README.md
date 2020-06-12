## Getting Started

Run all commands from the root directory. The reletive paths in the code are all set up to work when running from the root directory.
Do not run from withing the src directory.

## How to Compile Everything

javac -sourcepath src/ChatApp*.java -cp "lib/*;src"

## How to Run

It is recommened that you use 2 terminals side by side. One for the server and the other for the client.

1) `Run the Server`
    java -cp "src;lib/*" ChatAppServer

2) `Run the Client` (You will be asked to enter your name)
    java -cp "src;lib/*" ChatAppClient

3) `Send message`
    Once all the certificates are verified you can send a message



