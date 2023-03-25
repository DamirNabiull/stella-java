package org.stella.eval;

import org.syntax.stella.Absyn.*;
import org.syntax.stella.PrettyPrinter;

public class TypeCheck
{
    public static void typecheckProgram(Program program) throws Exception
    {
        var visitors = new Visitors();
        var visitor = visitors.new ProgramVisitor();
        program.accept(visitor, new Context());
//        System.out.println(PrettyPrinter.print(program));
//        System.out.println("typechecker is not implemented!");
        return;
    }
}
