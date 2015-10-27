package chemistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormulaCheckerTest {

    @Test
    public void testNoParenthesesFormulas() {
        FormulaChecker checker = new FormulaChecker();
        String[] formulas = {"H2O", "HCL", "Hea7Ji3IJKl", "O999999999999999", "AgBrO3", "AgCl3Cu2", "AgMnO4", "CH3CH2CH2CH2OH", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "H"};

        for(String s: formulas) {
            assertTrue("Checking " + s, checker.checkFormula(s));
        }
    }

    @Test
    public void testParenthesesFormulas() {
        FormulaChecker checker = new FormulaChecker();
        String[] formulas = {"H(CL)2", "(Hea7Ji)3IJKl3", "(OjH)37", "(JfkC4)909(H2Ao)70", "(Hel2B3)3(Six6Fi4)22(Eye32Ps4)", "Al(NO3)3",
                "Au2(SeO4)3", "Ba(BrO3)2H20", "CH3COO(CH2)2CH(CH3)2", "(CH3)3COOC(CH3)3"};

        for(String s: formulas) {
            assertTrue("Checking " + s, checker.checkFormula(s));
        }
    }

    @Test
    public void testIncorrectFormulas() {
        FormulaChecker checker = new FormulaChecker();
        String[] formulas = {"H2 O", "eBBe3", "x", "5", "Wrong2", "0"};

        for(String s: formulas) {
            assertFalse("Checking " + s, checker.checkFormula(s));
        }
    }

    @Test
    public void testInvalidParentheses() {
        FormulaChecker checker = new FormulaChecker();
        String[] formulas = {"H(Io4)3", "H(Io3", "H((H2O)7)6", "()", "H2O)3", "(H20(I)4Nz2)2", "(H20)", "(H2O)B4"};

        for(String s: formulas) {
            assertFalse("Checking " + s, checker.checkFormula(s));
        }
    }

    @Test
    public void testIncorrectMultiplier() {
        FormulaChecker checker = new FormulaChecker();
        String[] formulas = {"B0", "Ze098", "X9Je011"};

        for(String s: formulas) {
            assertFalse("Checking " + s, checker.checkFormula(s));
        }
    }

    @Test
    public void testInvalidCharacters() {
        FormulaChecker checker = new FormulaChecker();
        String[] formulas = {"?3", "H20!", "K#9"};

        for(String s: formulas) {
            assertFalse("Checking " + s, checker.checkFormula(s));
        }
    }
}