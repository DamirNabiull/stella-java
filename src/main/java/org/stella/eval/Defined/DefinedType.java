package org.stella.eval.Defined;

import org.syntax.stella.Absyn.Type;
import org.syntax.stella.Absyn.TypeFun;

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

    public boolean equals(DefinedType o)
    {
        return equals(this, o);
    }

    public boolean CheckNatRecFunParam(DefinedType o){
        boolean isFunc = o.type == TypesEnum.Func;
        boolean isCorrectArg = o.arg.type == TypesEnum.Nat;
        boolean isCorrectReturn =
                o.result.type == TypesEnum.Func
                && o.result.arg.type == this.type
                && o.result.result.type == this.type;

        return isFunc && isCorrectArg && isCorrectReturn;
    }

    @Override
    public String toString() {
        return GetArgsAndReturns(this, 0);
    }

    private boolean equals(DefinedType a, DefinedType b){
        if (a == b)
            return true;

        if (a.type == b.type)
        {
            return equals(a.arg, b.arg)
                    && equals(a.result, b.result);
        }

        return false;
    }

    private String GetArgsAndReturns(DefinedType t, int k){
        String ans = "";

        if (t.type == TypesEnum.Func) {
            ans += "\t".repeat(k) + "FUNC:\n";
            ans += "\t".repeat(k) + "ARGS:\n" + GetArgsAndReturns(t.arg, k+1);
            ans += "\t".repeat(k) + "RETURN:\n" + GetArgsAndReturns(t.result, k+1);
        } else {
            ans += "\t".repeat(k) + "VAR:\n";
            ans += "\t".repeat(k + 1) + t.type.toString() + '\n';
        }

        return ans;
    }
}
