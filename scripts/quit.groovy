name = scriptResource.getName();
desc = scriptResource.getDescription();
i = scriptResource.getIs();
o = scriptResource.getOs();
params = scriptResource.getParams();

String pass;
if (params.length > 0) pass = params[0];

if (pass == "naughty") {
    scriptResource.write("Attempting to quit...\r\n");
    Thread.sleep(1000);
    System.exit(0);
}
else {
    scriptResource.write("Naughty naughty!\r\n");
    Thread.sleep(1000);
}
