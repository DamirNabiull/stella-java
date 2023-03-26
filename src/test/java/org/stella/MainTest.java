package org.stella;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

class MainTest {


    @ParameterizedTest(name = "{index} Typechecking well-typed program {0}")
    @ValueSource(strings = {
            "tests/well-typed/factorial.stella",
            "tests/well-typed/squares.stella",
            "tests/well-typed/higher-order-1.stella",
            "tests/well-typed/increment_twice.stella",
            "tests/well-typed/logical-operators.stella"})
    public void testWellTyped(String filepath) throws IOException, Exception {
        String[] args = new String[0];
        final InputStream original = System.in;
        final FileInputStream fips = new FileInputStream(new File(filepath));
        System.setIn(fips);
        Main.main(args);
        System.setIn(original);
    }

    @ParameterizedTest(name = "{index} Typechecking ill-typed program {0}")
    @ValueSource(strings = {
            "tests/ill-typed/applying-non-function-1.stella",
            "tests/ill-typed/applying-non-function-2.stella",
            "tests/ill-typed/applying-non-function-3.stella",
            "tests/ill-typed/argument-type-mismatch-1.stella",
            "tests/ill-typed/argument-type-mismatch-2.stella",
            "tests/ill-typed/argument-type-mismatch-3.stella",
            "tests/ill-typed/bad-if-1.stella",
            "tests/ill-typed/bad-if-2.stella",
            "tests/ill-typed/bad-succ-1.stella",
            "tests/ill-typed/bad-succ-2.stella",
            "tests/ill-typed/bad-succ-3.stella",
            "tests/ill-typed/shadowed-variable-1.stella",
            "tests/ill-typed/undefined-variable-1.stella",
            "tests/ill-typed/undefined-variable-2.stella",
            "tests/ill-typed/bad-squares-1.stella",
            "tests/ill-typed/bad-squares-2.stella"})
    public void testIllTyped(String filepath) throws IOException, Exception {
        String[] args = new String[0];
        final InputStream original = System.in;
        final FileInputStream fips = new FileInputStream(new File(filepath));
        System.setIn(fips);

        boolean typecheckerFailed = false;
        try {
            Main.main(args); // TODO: check that if it fail then there is a type error actually, and not a problem with implementation
        } catch (Exception e) {
            System.out.println("Type Error: " + e.getMessage());
            typecheckerFailed = true;
        }
        if (!typecheckerFailed) {
            throw new Exception("expected the typechecker to fail!");
        }
        // System.setIn(original); // dead code
    }
}
