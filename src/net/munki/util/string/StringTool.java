package net.munki.util.string;

import java.util.*;

public class StringTool {

    private StringTool() {
    }

    public static String cat(String[] strings) {
        StringBuffer sb = new StringBuffer();
        for (String s : strings) {
            if (s != null) sb.append(s);
        }
        return sb.toString();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String[] uncat(String string) {
        List list = new ArrayList();
        StringTokenizer st = new StringTokenizer(string);
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        Object[] objects = list.toArray();
        String[] arguments = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            arguments[i] = (String)objects[i];
        }
        return arguments;
    }

}
