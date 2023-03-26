package org.stella.eval;

import com.ibm.icu.impl.Pair;
import org.stella.eval.Defined.DefinedType;

import java.util.HashMap;
import java.util.Stack;

public class Context {
    public HashMap<String, DefinedType> GlobalDefinitions;

    public HashMap<String, DefinedType> LocalDefinitions;

    public Stack<DefinedType> LocalContext;

    public Context()
    {
        GlobalDefinitions = new HashMap<String, DefinedType>();
        LocalDefinitions = new HashMap<String, DefinedType>();
        LocalContext = new Stack<DefinedType>();
    }

    public DefinedType LookUp(String name) throws Exception {
        if (GlobalDefinitions.containsKey(name))
            return GlobalDefinitions.get(name);

        if (LocalDefinitions.containsKey(name))
            return LocalDefinitions.get(name);

        throw new Exception(String.format("Not found declared %s", name));
    }

}