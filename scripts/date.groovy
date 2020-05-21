// date.groovy - date script for jServer
// note to me - this version is in the src tree
//

message = new Date().toString();
scriptResource.write(message + "\r\n");

