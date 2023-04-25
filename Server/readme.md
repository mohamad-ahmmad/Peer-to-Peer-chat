
--In coming requests must have the following rules:

#First line must be the type of the request.(Sign In/up)
--sign-in,username,password,port (Responses: ok, failed).
--sign-up,username,password,port (Responses: ok, failed).

--retrieve-list, (Return all users (online)).

->Format of retrieve-list
name,password,ip,port\n
..
...

--So you have to read using a loop => while(scan.hasNext())
Note: after each line there's new-line.


Example :

Formatter wr = new Formatter(connection.getOutputStream());
wr.format("name,password,port");
wr.flush

Scanner in = new Scanner(connection.getInputStream());
String res = in.next();
