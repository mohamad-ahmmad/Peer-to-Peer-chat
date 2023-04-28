
<details>
    <summary>Request formats</summary>
    

### Sign-in
```
-->sign-in,username,password,port (Responses: ok, failed).
```

### Sign-up
```
-->sign-up,username,password,port (Responses: ok, failed).
```
### Retrieve online users
```    
-->retrieve-list, (Return all users (online)), response:{
    name,password,ip,port\n
    ..
    ...
    
--So you have to read using a loop => while(scan.hasNext())

--Note: after each line there's new-line.

}
```

### log-out 
```
-->log-out,username,password,port (Responses: ok, failed).
```
    
### Note :'failed' happens when the credentials are wrong.

</details>




Example :

```Java
Formatter wr = new Formatter(connection.getOutputStream());
wr.format("log-out,moha,password");
wr.flush();

Scanner in = new Scanner(connection.getInputStream());
String res = in.next(); //failed or ok

connection.close();
..
...
```

