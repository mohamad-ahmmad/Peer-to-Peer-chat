
--In coming requests must have the following rules:

#First line must be the type of the request.(Sign In/up, retrieve-list, log-out)

-->sign-in,username,password,port (Responses: ok, failed).
-->sign-up,username,password,port (Responses: ok, failed).
-->retrieve-list, (Return all users (online)), response:{
    ->Format of retrieve-list
    name,password,ip,port\n
    ..
    ...
--So you have to read using a loop => while(scan.hasNext())
--Note: after each line there's new-line.
}

-->log-out,username,password,port (Responses: ok, failed).

->'failed' happens when the credentials are wrong.






Example :

Formatter wr = new Formatter(connection.getOutputStream());
wr.format("log-out,moha,password");
wr.flush();

Scanner in = new Scanner(connection.getInputStream());
String res = in.next();

connection.close();
