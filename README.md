# MyClient_Version2
### 1) Make sure the server is started before you run the client 
### 2) Update config.properties file and provide the following parameters 
  1) port Number of the server 
  2) hostName or IP address of the server 
  3) timeOut value for client in seconds 
### 3) Ensure the Resources folder is in your class path
### 4) Now run the client. ThinSocketClient.java provides a main method. 
You would be prompted to provide comma separated list of client IDs. For eg: A,B,C will create 3 separate threads. 
You can even start multiple clients as separate processes. 


# Sample Output on Client Console 
  Enter comma separated list of client IDs: A  
  ClientA is trying to connect with server:9.65.165.251 at port:4321  
  Client A: Job 0 status: created  
  Client A: Job 0 status: complete  
  
  ### To-Do Tasks 
  1) If connection drops out with the server, client should re-connect. 
