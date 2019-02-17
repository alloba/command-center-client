# command-center-client
client piece of application that allows defined scripts to be triggered from a server

This command center project is meant to be run in a local network. 
The idea is that a client can register with a running server, and provide the server with a list of commands the client supports.
Then the server will provide a webpage that will display all clients and their registered commands, and allow a user to trigger them remotely.

Currently this is limited to powershell scripts, and is a proof of concept more than anything that is meant to be used in practice. 

Once the initial work was done, i feel comfortable saying that while communicating through rest endpoints to execute arbitrary scripts on a remote client is possible, 
it may not be the best idea :)

Discontinued.
