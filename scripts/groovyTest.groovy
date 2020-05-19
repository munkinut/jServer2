
name = scriptResource.getName();
desc = scriptResource.getDescription();
i = scriptResource.getIs();
o = scriptResource.getOs();

scriptResource.write("Hello from " + name + "\r\n");
scriptResource.write("Desc " + desc + "\r\n");
