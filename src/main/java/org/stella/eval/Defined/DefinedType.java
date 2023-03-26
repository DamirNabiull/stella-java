package org.stella.eval.Defined;

import org.stella.eval.ExceptionsUtils;

public class DefinedType {
    public final TypesEnum type;

    public DefinedType arg;

    public DefinedType result;

    public DefinedType(TypesEnum t){
        type = t;
        arg = null;
        result = null;
    }

    public DefinedType(TypesEnum t, DefinedType a, DefinedType r){
        type = t;
        arg = a;
        result = r;
    }

    public boolean equals(DefinedType o, String context)
    {
        return equals(this, o, context);
    }

    public boolean CheckNatRecFunParam(DefinedType o){
        boolean isFunc = o.type == TypesEnum.Fun;
        boolean isCorrectArg = o.arg.type == TypesEnum.Nat;
        boolean isCorrectReturn =
                o.result.type == TypesEnum.Fun
                && this.equals(o.result.arg, "NatRec")
                && this.equals(o.result.result, "NatRec");

        if (!isFunc)
            ExceptionsUtils.throwTypeException("TYPE_ERROR: NatRec [T3]\nExpected type: Fun\nBut got: " + o.type.name());

        if (!isCorrectArg)
            ExceptionsUtils.throwTypeException("TYPE_ERROR: NatRec [T3 Arg]\nExpected type: Nat\nBut got: " + o.arg.type.name());

        if (!isCorrectReturn)
            ExceptionsUtils.throwTypeException("TYPE_ERROR: NatRec [T3 Return]\nExpected type: Fun\nBut got: " + o.result.type.name());

        return true;
    }

    @Override
    public String toString() {
        return GetArgsAndReturns(this, 0);
    }

    private boolean equals(DefinedType a, DefinedType b, String context) {
        if (a == b)
            return true;

        if (a.type == b.type)
        {
            return equals(a.arg, b.arg, context)
                    && equals(a.result, b.result, context);
        }

        String aType = a.type == null ? "NULL" : a.toString();
        String bType = b.type == null ? "NULL" : b.toString();

        ExceptionsUtils.throwTypeException(
                "TYPE_ERROR: "
                + context
                + "\nExpected type:\n"
                +  aType
                + "\nBut got:\n"
                + bType
        );

        return false;
    }

    private String GetArgsAndReturns(DefinedType t, int k){
        String ans = "";

        if (t.type == TypesEnum.Fun) {
            ans += "\t".repeat(k) + "FUN\n";
            ans += "\t".repeat(k + 1) + "{ARGS}\n" + GetArgsAndReturns(t.arg, k+1);
            ans += "\t".repeat(k + 1) + "{RETURN}\n" + GetArgsAndReturns(t.result, k+1);
        } else {
//            ans += "\t".repeat(k) + "VAR:\n";
            ans += "\t".repeat(k) + t.type.name() + "\n\n";
        }

        return ans;
    }
}