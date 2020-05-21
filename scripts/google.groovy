// google.groovy - google script for jServer
// note to me - this version is in the src tree
//

params = scriptResource.getParams();

Socket sock;
BufferedReader input;
PrintWriter out;
String buffer = null;

StringBuffer query = new StringBuffer();

for (String param:params) {
    query.append(param);
    query.append("+");
}
query.deleteCharAt(query.length()-1);

try {
    sock = new Socket("www.google.com", 80);
    input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    out = new PrintWriter(sock.getOutputStream());
    out.println("GET /search?q=" + query.toString() + "&btnI=I'm+feeling+lucky HTTP/1.0\n\n");
    out.flush();

    buffer = input.readLine();
    try {
        while(buffer != null) {
            buffer = input.readLine();
            if ((buffer != null) && (buffer.startsWith("Location:"))) {
                buffer = buffer.substring(10, buffer.length());
                if (buffer.startsWith("http://")) {
                    scriptResource.write("Google result : " + buffer + "\r\n");
                } else {
                    scriptResource.write("Google result : no result" + "\r\n");
                }
                break;
            }
        }
    }
    catch (NullPointerException e) {
        e.printStackTrace();
    }
}
catch (UnknownHostException e) {
    e.printStackTrace();
}
catch (IOException e) {
    e.printStackTrace();
}
