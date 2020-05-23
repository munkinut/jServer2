name = scriptResource.getName();
desc = scriptResource.getDescription();
i = scriptResource.getIs();
o = scriptResource.getOs();
params = scriptResource.getParams();

index = 0;
stringBuilder = new StringBuilder();
if ((params != null) && (params.length > 0)) {
    for(String param:params) {
        stringBuilder.append(param);
        stringBuilder.append(" ");
    }
}

scriptResource.write("Hello from " + name + "\r\n");
scriptResource.write("Desc " + desc + "\r\n");
scriptResource.write("Params " + stringBuilder.toString() + "\r\n");
