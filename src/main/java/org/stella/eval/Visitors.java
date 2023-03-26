package org.stella.eval;

import org.stella.eval.Defined.DefinedType;
import org.stella.eval.Defined.TypesEnum;

public class Visitors {
    public class ProgramVisitor implements org.syntax.stella.Absyn.Program.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AProgram p, Context arg)
        { /* Code for AProgram goes here */
//            System.out.println("ProgramVisitor");
            p.languagedecl_.accept(new LanguageDeclVisitor(), arg);
            for (org.syntax.stella.Absyn.Extension x: p.listextension_) {
                x.accept(new ExtensionVisitor(), arg);
            }
            for (org.syntax.stella.Absyn.Decl x: p.listdecl_) {
                x.accept(new DeclVisitor(), arg);
            }
            return null;
        }
    }
    public class LanguageDeclVisitor implements org.syntax.stella.Absyn.LanguageDecl.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.LanguageCore p, Context arg)
        { /* Code for LanguageCore goes here */
//            System.out.println("LanguageDeclVisitor");
            return null;
        }
    }
    public class ExtensionVisitor implements org.syntax.stella.Absyn.Extension.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AnExtension p, Context arg)
        { /* Code for AnExtension goes here */
//            System.out.println("ExtensionVisitor");
            for (String x: p.listextensionname_) {
//                System.out.println("\t"+x);
                //x;
            }
            return null;
        }
    }
    public class DeclVisitor implements org.syntax.stella.Absyn.Decl.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.DeclFun p, Context arg)
        { /* Code for DeclFun goes here */
//            System.out.println("\nFun - DeclVisitor " + p.stellaident_);

            DefinedType funcType = new DefinedType(TypesEnum.Fun);
            Context context = new Context(arg);

            for (org.syntax.stella.Absyn.Annotation x: p.listannotation_) {
                x.accept(new AnnotationVisitor(), context);
            }
            //p.stellaident_;
            for (org.syntax.stella.Absyn.ParamDecl x: p.listparamdecl_) {
                funcType.arg = x.accept(new ParamDeclVisitor(), context);
            }
            funcType.result = p.returntype_.accept(new ReturnTypeVisitor(), context);
            p.throwtype_.accept(new ThrowTypeVisitor(), context);
            for (org.syntax.stella.Absyn.Decl x: p.listdecl_) {
                x.accept(new DeclVisitor(), context);
            }
            var body = p.expr_.accept(new ExprVisitor(), context);

//            System.out.println(context);

            // Clear context
            arg.LocalDefinitions.clear();

            funcType.result.equals(body, "FunDecl [" + p.stellaident_ + "]");

            //Add func to global definitions
            arg.GlobalDefinitions.put(p.stellaident_, funcType);

            return funcType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.DeclTypeAlias p, Context arg)
        { /* Code for DeclTypeAlias goes here */
            //p.stellaident_;
//            System.out.println("\nTypeAlias - DeclVisitor");
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
    public class LocalDeclVisitor implements org.syntax.stella.Absyn.LocalDecl.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ALocalDecl p, Context arg)
        { /* Code for ALocalDecl goes here */
//            System.out.println("LocalDeclVisitor");
            p.decl_.accept(new DeclVisitor(), arg);
            return null;
        }
    }
    public class AnnotationVisitor implements org.syntax.stella.Absyn.Annotation.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.InlineAnnotation p, Context arg)
        { /* Code for InlineAnnotation goes here */
//            System.out.println("AnnotationVisitor");
            return null;
        }
    }
    public class ParamDeclVisitor implements org.syntax.stella.Absyn.ParamDecl.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AParamDecl p, Context arg)
        { /* Code for AParamDecl goes here */
//            System.out.println("ParamDeclVisitor");
            //p.stellaident_;
            var param = p.type_.accept(new TypeVisitor(), arg);
            arg.LocalDefinitions.put(p.stellaident_, param);
            return param;
        }
    }
    public class ReturnTypeVisitor implements org.syntax.stella.Absyn.ReturnType.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoReturnType p, Context arg)
        { /* Code for NoReturnType goes here */
//            System.out.println("NoReturnTypeVisitor");
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomeReturnType p, Context arg)
        { /* Code for SomeReturnType goes here */
//            System.out.println("SomeReturnTypeVisitor");
            var returnType = p.type_.accept(new TypeVisitor(), arg);

            if (returnType == null)
                ExceptionsUtils.throwTypeException("TYPE_ERROR: SomeReturnType\nExpected type: [SomeType]\nBut got: NULL");

            return returnType;
        }
    }
    public class ThrowTypeVisitor implements org.syntax.stella.Absyn.ThrowType.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoThrowType p, Context arg)
        { /* Code for NoThrowType goes here */
//            System.out.println("NoThrowTypeVisitor");
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomeThrowType p, Context arg)
        { /* Code for SomeThrowType goes here */
//            System.out.println("SomeThrowTypeVisitor");
            for (org.syntax.stella.Absyn.Type x: p.listtype_) {
                x.accept(new TypeVisitor(), arg);
            }
            return null;
        }
    }
    public class ExprVisitor implements org.syntax.stella.Absyn.Expr.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.If p, Context arg)
        { /* Code for If goes here */
//            System.out.println("If");
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);
            var t3 = p.expr_3.accept(new ExprVisitor(), arg);

            if (t1.type == TypesEnum.Bool && t2.equals(t3, "IF"))
                return t2;

            ExceptionsUtils.throwTypeException("TYPE_ERROR: IF [T1]\nExpected type: Bool\nBut got: " + t1);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Let p, Context arg)
        { /* Code for Let goes here */
            //p.stellaident_;
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LessThan p, Context arg)
        { /* Code for LessThan goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LessThanOrEqual p, Context arg)
        { /* Code for LessThanOrEqual goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.GreaterThan p, Context arg)
        { /* Code for GreaterThan goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.GreaterThanOrEqual p, Context arg)
        { /* Code for GreaterThanOrEqual goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Equal p, Context arg)
        { /* Code for Equal goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.NotEqual p, Context arg)
        { /* Code for NotEqual goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeAsc p, Context arg)
        { /* Code for TypeAsc goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Abstraction p, Context arg)
        { /* Code for Abstraction goes here */
//            System.out.println("Abstraction");

            var abstractFunc = new DefinedType(TypesEnum.Fun);
            Context context = new Context(arg);

            for (org.syntax.stella.Absyn.ParamDecl x: p.listparamdecl_) {
                abstractFunc.arg = x.accept(new ParamDeclVisitor(), context);
            }
            abstractFunc.result = p.expr_.accept(new ExprVisitor(), context);

//            System.out.println(context);

            return abstractFunc;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Tuple p, Context arg)
        { /* Code for Tuple goes here */
            for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
                x.accept(new ExprVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Record p, Context arg)
        { /* Code for Record goes here */
            for (org.syntax.stella.Absyn.Binding x: p.listbinding_) {
                x.accept(new BindingVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Variant p, Context arg)
        { /* Code for Variant goes here */
            //p.stellaident_;
//            System.out.println("Variant");
            p.exprdata_.accept(new ExprDataVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Match p, Context arg)
        { /* Code for Match goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            for (org.syntax.stella.Absyn.MatchCase x: p.listmatchcase_) {
                x.accept(new MatchCaseVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.List p, Context arg)
        { /* Code for List goes here */
            for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
                x.accept(new ExprVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Add p, Context arg)
        { /* Code for Add goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LogicOr p, Context arg)
        { /* Code for LogicOr goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Multiply p, Context arg)
        { /* Code for Multiply goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LogicAnd p, Context arg)
        { /* Code for LogicAnd goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Application p, Context arg)
        { /* Code for Application goes here */
//            System.out.println("Application");
            DefinedType funType, argType = null;
            funType = p.expr_.accept(new ExprVisitor(), arg);
            for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
                argType = x.accept(new ExprVisitor(), arg);
            }

            if (funType.type == TypesEnum.Fun && funType.arg.equals(argType, "Application"))
                return funType.result;

            ExceptionsUtils.throwTypeException("TYPE_ERROR: Application\nExpected type: Fun\nBut got: " + funType.type.name());
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.ConsList p, Context arg)
        { /* Code for ConsList goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Head p, Context arg)
        { /* Code for Head goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.IsEmpty p, Context arg)
        { /* Code for IsEmpty goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Tail p, Context arg)
        { /* Code for Tail goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Succ p, Context arg)
        { /* Code for Succ goes here */
//            System.out.println("Succ");
            var type = p.expr_.accept(new ExprVisitor(), arg);

            if (type == null)
                ExceptionsUtils.throwTypeException("TYPE_ERROR: Succ\nExpected type: Nat\nBut got: NULL");

            if (type.type != TypesEnum.Nat)
                ExceptionsUtils.throwTypeException("TYPE_ERROR: Succ\nExpected type: Nat\nBut got: " + type.type.name());

            return type;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LogicNot p, Context arg)
        { /* Code for LogicNot goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Pred p, Context arg)
        { /* Code for Pred goes here */
//            System.out.println("Pred");
            var type = p.expr_.accept(new ExprVisitor(), arg);

            if (type == null)
                ExceptionsUtils.throwTypeException("TYPE_ERROR: Pred\nExpected type: Nat\nBut got: NULL");

            if (type.type != TypesEnum.Nat)
                ExceptionsUtils.throwTypeException("TYPE_ERROR: Pred\nExpected type: Nat\nBut got: " + type.type.name());

            return type;
        }
        public DefinedType visit(org.syntax.stella.Absyn.IsZero p, Context arg)
        { /* Code for IsZero goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Fix p, Context arg)
        { /* Code for Fix goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.NatRec p, Context arg)
        { /* Code for NatRec goes here */
//            System.out.println("NatRec");
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);
            var t3 = p.expr_3.accept(new ExprVisitor(), arg);

            if (t1.type == TypesEnum.Nat && t2.CheckNatRecFunParam(t3))
                return t2;

            ExceptionsUtils.throwTypeException("TYPE_ERROR: NatRec [T1]\nExpected type: Nat\nBut got: " + t1.type.name());
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Fold p, Context arg)
        { /* Code for Fold goes here */
            p.type_.accept(new TypeVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Unfold p, Context arg)
        { /* Code for Unfold goes here */
            p.type_.accept(new TypeVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.DotRecord p, Context arg)
        { /* Code for DotRecord goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            //p.stellaident_;
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.DotTuple p, Context arg)
        { /* Code for DotTuple goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            //p.integer_;
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.ConstTrue p, Context arg)
        { /* Code for ConstTrue goes here */
//            System.out.println("ConstTrue");
            return new DefinedType(TypesEnum.Bool);
        }
        public DefinedType visit(org.syntax.stella.Absyn.ConstFalse p, Context arg)
        { /* Code for ConstFalse goes here */
//            System.out.println("ConstTrue");
            return new DefinedType(TypesEnum.Bool);
        }
        public DefinedType visit(org.syntax.stella.Absyn.ConstInt p, Context arg)
        { /* Code for ConstInt goes here */
//            System.out.println("ConstInt");
            //p.integer_;
            return new DefinedType(TypesEnum.Nat);
        }
        public DefinedType visit(org.syntax.stella.Absyn.Var p, Context arg)
        { /* Code for Var goes here */
//            System.out.println("Var " + p.stellaident_);

            DefinedType type;
            type = arg.LocalDefinitions.get(p.stellaident_);

            if (type != null)
                return type;

            type = arg.GlobalDefinitions.get(p.stellaident_);

            if (type != null)
                return type;

            ExceptionsUtils.throwTypeException("ERROR: Var " + p.stellaident_ + " is undefined");
            return null;
        }
    }
    public class MatchCaseVisitor implements org.syntax.stella.Absyn.MatchCase.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AMatchCase p, Context arg)
        { /* Code for AMatchCase goes here */
            p.pattern_.accept(new PatternVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
    }
    public class OptionalTypingVisitor implements org.syntax.stella.Absyn.OptionalTyping.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoTyping p, Context arg)
        { /* Code for NoTyping goes here */
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomeTyping p, Context arg)
        { /* Code for SomeTyping goes here */
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
    public class PatternDataVisitor implements org.syntax.stella.Absyn.PatternData.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoPatternData p, Context arg)
        { /* Code for NoPatternData goes here */
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomePatternData p, Context arg)
        { /* Code for SomePatternData goes here */
            p.pattern_.accept(new PatternVisitor(), arg);
            return null;
        }
    }
    public class ExprDataVisitor implements org.syntax.stella.Absyn.ExprData.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoExprData p, Context arg)
        { /* Code for NoExprData goes here */
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomeExprData p, Context arg)
        { /* Code for SomeExprData goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
    }
    public class PatternVisitor implements org.syntax.stella.Absyn.Pattern.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.PatternVariant p, Context arg)
        { /* Code for PatternVariant goes here */
            //p.stellaident_;
            p.patterndata_.accept(new PatternDataVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternTuple p, Context arg)
        { /* Code for PatternTuple goes here */
            for (org.syntax.stella.Absyn.Pattern x: p.listpattern_) {
                x.accept(new PatternVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternRecord p, Context arg)
        { /* Code for PatternRecord goes here */
            for (org.syntax.stella.Absyn.LabelledPattern x: p.listlabelledpattern_) {
                x.accept(new LabelledPatternVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternList p, Context arg)
        { /* Code for PatternList goes here */
            for (org.syntax.stella.Absyn.Pattern x: p.listpattern_) {
                x.accept(new PatternVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternCons p, Context arg)
        { /* Code for PatternCons goes here */
            p.pattern_1.accept(new PatternVisitor(), arg);
            p.pattern_2.accept(new PatternVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternFalse p, Context arg)
        { /* Code for PatternFalse goes here */
//            System.out.println("PatternFalse");
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternTrue p, Context arg)
        { /* Code for PatternTrue goes here */
//            System.out.println("PatternTrue");
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternInt p, Context arg)
        { /* Code for PatternInt goes here */
//            System.out.println("PatternInt");
            //p.integer_;
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternSucc p, Context arg)
        { /* Code for PatternSucc goes here */
//            System.out.println("PatternSucc");
            p.pattern_.accept(new PatternVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternVar p, Context arg)
        { /* Code for PatternVar goes here */
//            System.out.println("PatternVar");
            //p.stellaident_;
            return null;
        }
    }
    public class LabelledPatternVisitor implements org.syntax.stella.Absyn.LabelledPattern.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ALabelledPattern p, Context arg)
        { /* Code for ALabelledPattern goes here */
            //p.stellaident_;
            p.pattern_.accept(new PatternVisitor(), arg);
            return null;
        }
    }
    public class BindingVisitor implements org.syntax.stella.Absyn.Binding.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ABinding p, Context arg)
        { /* Code for ABinding goes here */
            //p.stellaident_;
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
    }
    public class TypeVisitor implements org.syntax.stella.Absyn.Type.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.TypeFun p, Context arg)
        { /* Code for TypeFun goes here */
//            System.out.println("Type - TypeFun");

            DefinedType argsType = null, returnType;

            for (org.syntax.stella.Absyn.Type x: p.listtype_) {
                argsType = x.accept(new TypeVisitor(), arg);
            }
            returnType = p.type_.accept(new TypeVisitor(), arg);

            return new DefinedType(TypesEnum.Fun, argsType, returnType);
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeRec p, Context arg)
        { /* Code for TypeRec goes here */
            //p.stellaident_;
//            System.out.println("Type - TypeRec");
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeSum p, Context arg)
        { /* Code for TypeSum goes here */
//            System.out.println("Type - TypeSum");
            p.type_1.accept(new TypeVisitor(), arg);
            p.type_2.accept(new TypeVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeTuple p, Context arg)
        { /* Code for TypeTuple goes here */
//            System.out.println("Type - TypeTuple");
            for (org.syntax.stella.Absyn.Type x: p.listtype_) {
                x.accept(new TypeVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeRecord p, Context arg)
        { /* Code for TypeRecord goes here */
//            System.out.println("Type - TypeRecord");
            for (org.syntax.stella.Absyn.RecordFieldType x: p.listrecordfieldtype_) {
                x.accept(new RecordFieldTypeVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeVariant p, Context arg)
        { /* Code for TypeVariant goes here */
//            System.out.println("Type - TypeVariant");
            for (org.syntax.stella.Absyn.VariantFieldType x: p.listvariantfieldtype_) {
                x.accept(new VariantFieldTypeVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeList p, Context arg)
        { /* Code for TypeList goes here */
//            System.out.println("Type - TypeList");
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeBool p, Context arg)
        { /* Code for TypeBool goes here */
//            System.out.println("Type - TypeBool");
            return new DefinedType(TypesEnum.Bool);
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeNat p, Context arg)
        { /* Code for TypeNat goes here */
//            System.out.println("Type - TypeNat");
            return new DefinedType(TypesEnum.Nat);
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeUnit p, Context arg)
        { /* Code for TypeUnit goes here */
//            System.out.println("Type - TypeUnit");
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeVar p, Context arg)
        { /* Code for TypeVar goes here */
//            System.out.println("Type - TypeVar");
            //p.stellaident_;
            return null;
        }
    }
    public class VariantFieldTypeVisitor implements org.syntax.stella.Absyn.VariantFieldType.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AVariantFieldType p, Context arg)
        { /* Code for AVariantFieldType goes here */
            //p.stellaident_;
            p.optionaltyping_.accept(new OptionalTypingVisitor(), arg);
            return null;
        }
    }
    public class RecordFieldTypeVisitor implements org.syntax.stella.Absyn.RecordFieldType.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ARecordFieldType p, Context arg)
        { /* Code for ARecordFieldType goes here */
            //p.stellaident_;
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
    public class TypingVisitor implements org.syntax.stella.Absyn.Typing.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ATyping p, Context arg)
        { /* Code for ATyping goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
}
