package org.stella.eval;

import com.ibm.icu.impl.Pair;
import org.stella.eval.Defined.DefinedType;

import java.util.HashMap;
import java.util.Stack;

public class Context {
    public HashMap<String, DefinedType> GlobalDefinitions;

    public HashMap<String, DefinedType> LocalDefinitions;

    public Context()
    {
        GlobalDefinitions = new HashMap<>();
        LocalDefinitions = new HashMap<>();
    }

    public Context(Context context)
    {
        GlobalDefinitions = new HashMap<>(context.GlobalDefinitions);
        GlobalDefinitions.putAll(context.LocalDefinitions);

        LocalDefinitions = new HashMap<>();
    }

    public DefinedType LookUp(String name) throws Exception {
        if (GlobalDefinitions.containsKey(name))
            return GlobalDefinitions.get(name);

        if (LocalDefinitions.containsKey(name))
            return LocalDefinitions.get(name);

        throw new Exception(String.format("Not found declared %s", name));
    }

    @Override
    public String toString() {
        String ans = "Global:\n";

        for (String key : GlobalDefinitions.keySet()) {
            ans += key + " : " + GlobalDefinitions.get(key);
        }

        ans += "\nLocal:\n";

        for (String key : LocalDefinitions.keySet()) {
            ans += key + " : " + LocalDefinitions.get(key);
        }

        return ans;
    }
}