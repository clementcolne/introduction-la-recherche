package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;
import solver.AbstractSolver;
import solver.Lpsolve;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LpsolveTest {

    private AbstractSolver solver1;
    private AbstractSolver solver2;
    private Lpsolve solver3;
    private Lpsolve solver4;
    private AbstractSolver solver5;
    private AbstractSolver solver6;
    private AbstractSolver solver7;
    private AbstractSolver solver8;
    private AbstractSolver solver9;
    private AbstractSolver solver10;
    private AbstractSolver solver11;
    private AbstractSolver solver12;
    private AbstractSolver solver13;
    private AbstractSolver solver14;

    @BeforeEach
    void setUp() {
        solver1 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "wrongFunctionRightMRU.txt", "-S7");
        solver2 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+"wrongExtension.mp3", "-S7");
        solver3 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+"testWrong.txt", "-S7");
        solver4 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+"wrongExtension.mp3", "-S7");
        solver5 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+"wrong_path.txt", "-S7");
        solver6 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "wrongFunctionRightMRU.txt", "");
        solver7 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "wrongFunctionRightMRU.txt", "-S1");
        solver8 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "wrongFunctionRightMRU.txt", "-S2");
        solver9 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "wrongFunctionRightMRU.txt", "-S3");
        solver10 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "wrongFunctionRightMRU.txt", "-S4");
        solver11 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "wrongFunctionRightMRU.txt", "-S5");
        solver12 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "wrongFunctionRightMRU.txt", "-S6");
        solver13 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "wrongMRU.txt", "");
        solver14 = new Lpsolve("."+File.separatorChar+"src"+File.separatorChar+"tests"+File.separatorChar+ "rightFunction.txt", "");
    }

    @Test
    void testCreateSolverFileRight() {
        solver1.createSolverFile();
        solver2.createSolverFile();
        solver3.createSolverFile();
        solver4.createSolverFile();

        File file = new File(solver1.getSolverFile());
        File file2 = new File(solver2.getSolverFile());
        File file3 = new File(solver3.getSolverFile());

        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file2.exists());
        Assertions.assertTrue(file3.exists());
    }

    /*@Test(expected = FileNotFoundException.class)
    void testcreateSolverFileWrong() {
        Assertions.assertThrows(FileNotFoundException.class, () ->
            solver5.createSolverFile(),
        "Expected createSolverFile() to throw, but it didn't"
        );
    }*/

    @Test
    void testParseOutputWrongExtension() throws IOException {
        solver2.createSolverFile();
        solver2.run();
        solver2.display();
        solver2.parseOutput();
    }

    @Test
    void testParseOutputEmptyTxt() throws IOException {
        solver3.createSolverFile();
        solver3.run();
        solver3.display();
        solver3.parseOutput();
    }

    @Test
    void retryLpFileWrongExtension() {
        solver4.createSolverFile();
        solver4.retryLpFile();
    }

    @Test
    void retryLpFileEmptyTxt() {
        solver3.createSolverFile();
        solver3.retryLpFile();
    }

    @Test
    void WrongFunctionNoOption() throws IOException {
        solver6.createSolverFile();
        solver6.run();
        solver6.display();
        solver6.parseOutput();

        String res = "\n" +
                "Value of objective function: 2.00100000\n" +
                "\n" +
                "Actual values of the variables:\n" +
                "z1                          2.001\n" +
                "z2                              0\n" +
                "y1                          2.999\n" +
                "y2                              2\n";

        Assertions.assertEquals(res, solver6.getOutput());
    }

    @Test
    void WrongFunctionS1Option() throws IOException {
        solver7.createSolverFile();
        solver7.run();
        solver7.display();
        solver7.parseOutput();

        String res = "\n" +
                "Value of objective function: 2.00100000\n";

        Assertions.assertEquals(res, solver7.getOutput());
    }

    @Test
    void WrongFunctionS2Option() throws IOException {
        solver8.createSolverFile();
        solver8.run();
        solver8.display();
        solver8.parseOutput();

        String res = "\n" +
                "Value of objective function: 2.00100000\n" +
                "\n" +
                "Actual values of the variables:\n" +
                "z1                          2.001\n" +
                "z2                              0\n" +
                "y1                          2.999\n" +
                "y2                              2\n";

        Assertions.assertEquals(res, solver8.getOutput());
    }

    @Test
    void WrongFunctionS3Option() throws IOException {
        solver9.createSolverFile();
        solver9.run();
        solver9.display();
        solver9.parseOutput();

        String res = "\n" +
                "Value of objective function: 2.00100000\n" +
                "\n" +
                "Actual values of the variables:\n" +
                "z1                          2.001\n" +
                "z2                              0\n" +
                "y1                          2.999\n" +
                "y2                              2\n" +
                "\n" +
                "Actual values of the constraints:\n" +
                "c1                         -0.998\n" +
                "c2                              5\n" +
                "c3                             -2\n" +
                "c4                              2\n" +
                "c5                          2.999\n" +
                "c6                          2.999\n" +
                "c7                              2\n" +
                "c8                              2\n";

        Assertions.assertEquals(res, solver9.getOutput());
    }

    @Test
    void WrongFunctionS4Option() throws IOException {
        solver10.createSolverFile();
        solver10.run();
        solver10.display();
        solver10.parseOutput();

        String res = "\n" +
                "Value of objective function: 2.00100000\n" +
                "\n" +
                "Actual values of the variables:\n" +
                "z1                          2.001\n" +
                "z2                              0\n" +
                "y1                          2.999\n" +
                "y2                              2\n" +
                "\n" +
                "Actual values of the constraints:\n" +
                "c1                         -0.998\n" +
                "c2                              5\n" +
                "c3                             -2\n" +
                "c4                              2\n" +
                "c5                          2.999\n" +
                "c6                          2.999\n" +
                "c7                              2\n" +
                "c8                              2\n" +
                "\n" +
                "Objective function limits:\n" +
                "                                 From            Till       FromValue\n" +
                "z1                                  0          1e+030         -1e+030\n" +
                "z2                                  0          1e+030               0\n" +
                "y1                            -1e+030               1         -1e+030\n" +
                "y2                                  0               1         -1e+030\n" +
                "\n" +
                "Dual values with from - till limits:\n" +
                "                           Dual value            From            Till\n" +
                "c1                                  0         -1e+030          1e+030\n" +
                "c2                                  1           2.999          1e+030\n" +
                "c3                                  0         -1e+030          1e+030\n" +
                "c4                                  0           1.001               2\n" +
                "c5                                  0         -1e+030          1e+030\n" +
                "c6                                 -1           1.001               5\n" +
                "c7                                  0         -1e+030          1e+030\n" +
                "c8                                  0         -1e+030          1e+030\n" +
                "z1                                  0         -1e+030          1e+030\n" +
                "z2                                  1               0           0.999\n" +
                "y1                                  0         -1e+030          1e+030\n" +
                "y2                                  0         -1e+030          1e+030\n";

        Assertions.assertEquals(res, solver10.getOutput());
    }

    @Test
    void WrongFunctionS5Option() throws IOException {
        solver11.createSolverFile();
        solver11.run();
        solver11.display();
        solver11.parseOutput();

        String res = "Model name: \n" +
                "                z1       z2       y1       y2 \n" +
                "Minimize         1        1        0        0 \n" +
                "c1               1        0       -1        0 >=       -5\n" +
                "c2               1        0        1        0 >=        5\n" +
                "c3               0        1        0       -1 >=       -2\n" +
                "c4               0        1        0        1 >=        2\n" +
                "c5               0        0        1        0 >=    1.001\n" +
                "c6               0        0        1        0 <=    2.999\n" +
                "c7               0        0        0        1 >=    1.001\n" +
                "c8               0        0        0        1 <=    2.999\n" +
                "Type          Real     Real     Real     Real \n" +
                "upbo           Inf      Inf      Inf      Inf \n" +
                "lowbo            0        0        0        0 \n" +
                "\n" +
                "Value of objective function: 2.00100000\n" +
                "\n" +
                "Actual values of the variables:\n" +
                "z1                          2.001\n" +
                "z2                              0\n" +
                "y1                          2.999\n" +
                "y2                              2\n" +
                "\n" +
                "Actual values of the constraints:\n" +
                "c1                         -0.998\n" +
                "c2                              5\n" +
                "c3                             -2\n" +
                "c4                              2\n" +
                "c5                          2.999\n" +
                "c6                          2.999\n" +
                "c7                              2\n" +
                "c8                              2\n" +
                "\n" +
                "Objective function limits:\n" +
                "                                 From            Till       FromValue\n" +
                "z1                                  0          1e+030         -1e+030\n" +
                "z2                                  0          1e+030               0\n" +
                "y1                            -1e+030               1         -1e+030\n" +
                "y2                                  0               1         -1e+030\n" +
                "\n" +
                "Dual values with from - till limits:\n" +
                "                           Dual value            From            Till\n" +
                "c1                                  0         -1e+030          1e+030\n" +
                "c2                                  1           2.999          1e+030\n" +
                "c3                                  0         -1e+030          1e+030\n" +
                "c4                                  0           1.001               2\n" +
                "c5                                  0         -1e+030          1e+030\n" +
                "c6                                 -1           1.001               5\n" +
                "c7                                  0         -1e+030          1e+030\n" +
                "c8                                  0         -1e+030          1e+030\n" +
                "z1                                  0         -1e+030          1e+030\n" +
                "z2                                  1               0           0.999\n" +
                "y1                                  0         -1e+030          1e+030\n" +
                "y2                                  0         -1e+030          1e+030\n";

        Assertions.assertEquals(res, solver11.getOutput());
    }

    @Test
    void WrongFunctionS6Option() throws IOException {
        solver12.createSolverFile();
        solver12.run();
        solver12.display();
        solver12.parseOutput();

        String res = "Model name: \n" +
                "                z1       z2       y1       y2 \n" +
                "Minimize         1        1        0        0 \n" +
                "c1               1        0       -1        0 >=       -5\n" +
                "c2               1        0        1        0 >=        5\n" +
                "c3               0        1        0       -1 >=       -2\n" +
                "c4               0        1        0        1 >=        2\n" +
                "c5               0        0        1        0 >=    1.001\n" +
                "c6               0        0        1        0 <=    2.999\n" +
                "c7               0        0        0        1 >=    1.001\n" +
                "c8               0        0        0        1 <=    2.999\n" +
                "Type          Real     Real     Real     Real \n" +
                "upbo           Inf      Inf      Inf      Inf \n" +
                "lowbo            0        0        0        0 \n" +
                "\n" +
                "Value of objective function: 2.00100000\n" +
                "\n" +
                "Actual values of the variables:\n" +
                "z1                          2.001\n" +
                "z2                              0\n" +
                "y1                          2.999\n" +
                "y2                              2\n" +
                "\n" +
                "Actual values of the constraints:\n" +
                "c1                         -0.998\n" +
                "c2                              5\n" +
                "c3                             -2\n" +
                "c4                              2\n" +
                "c5                          2.999\n" +
                "c6                          2.999\n" +
                "c7                              2\n" +
                "c8                              2\n" +
                "\n" +
                "Objective function limits:\n" +
                "                                 From            Till       FromValue\n" +
                "z1                                  0          1e+030         -1e+030\n" +
                "z2                                  0          1e+030               0\n" +
                "y1                            -1e+030               1         -1e+030\n" +
                "y2                                  0               1         -1e+030\n" +
                "\n" +
                "Dual values with from - till limits:\n" +
                "                           Dual value            From            Till\n" +
                "c1                                  0         -1e+030          1e+030\n" +
                "c2                                  1           2.999          1e+030\n" +
                "c3                                  0         -1e+030          1e+030\n" +
                "c4                                  0           1.001               2\n" +
                "c5                                  0         -1e+030          1e+030\n" +
                "c6                                 -1           1.001               5\n" +
                "c7                                  0         -1e+030          1e+030\n" +
                "c8                                  0         -1e+030          1e+030\n" +
                "z1                                  0         -1e+030          1e+030\n" +
                "z2                                  1               0           0.999\n" +
                "y1                                  0         -1e+030          1e+030\n" +
                "y2                                  0         -1e+030          1e+030\n";

        Assertions.assertEquals(res, solver12.getOutput());
    }

    @Test
    void WrongFunctionS7Option() throws IOException {
        solver1.createSolverFile();
        solver1.run();
        solver1.display();
        solver1.parseOutput();

        String res = "Model name: \n" +
                "                z1       z2       y1       y2 \n" +
                "Minimize         1        1        0        0 \n" +
                "c1               1        0       -1        0 >=       -5\n" +
                "c2               1        0        1        0 >=        5\n" +
                "c3               0        1        0       -1 >=       -2\n" +
                "c4               0        1        0        1 >=        2\n" +
                "c5               0        0        1        0 >=    1.001\n" +
                "c6               0        0        1        0 <=    2.999\n" +
                "c7               0        0        0        1 >=    1.001\n" +
                "c8               0        0        0        1 <=    2.999\n" +
                "Type          Real     Real     Real     Real \n" +
                "upbo           Inf      Inf      Inf      Inf \n" +
                "lowbo            0        0        0        0 \n" +
                "\n" +
                "Value of objective function: 2.00100000\n" +
                "\n" +
                "Actual values of the variables:\n" +
                "z1                          2.001\n" +
                "z2                              0\n" +
                "y1                          2.999\n" +
                "y2                              2\n" +
                "\n" +
                "Actual values of the constraints:\n" +
                "c1                         -0.998\n" +
                "c2                              5\n" +
                "c3                             -2\n" +
                "c4                              2\n" +
                "c5                          2.999\n" +
                "c6                          2.999\n" +
                "c7                              2\n" +
                "c8                              2\n" +
                "\n" +
                "Objective function limits:\n" +
                "                                 From            Till       FromValue\n" +
                "z1                                  0          1e+030         -1e+030\n" +
                "z2                                  0          1e+030               0\n" +
                "y1                            -1e+030               1         -1e+030\n" +
                "y2                                  0               1         -1e+030\n" +
                "\n" +
                "Dual values with from - till limits:\n" +
                "                           Dual value            From            Till\n" +
                "c1                                  0         -1e+030          1e+030\n" +
                "c2                                  1           2.999          1e+030\n" +
                "c3                                  0         -1e+030          1e+030\n" +
                "c4                                  0           1.001               2\n" +
                "c5                                  0         -1e+030          1e+030\n" +
                "c6                                 -1           1.001               5\n" +
                "c7                                  0         -1e+030          1e+030\n" +
                "c8                                  0         -1e+030          1e+030\n" +
                "z1                                  0         -1e+030          1e+030\n" +
                "z2                                  1               0           0.999\n" +
                "y1                                  0         -1e+030          1e+030\n" +
                "y2                                  0         -1e+030          1e+030\n" +
                "\n" +
                "Tableau at iter 3:\n" +
                "              6              8            -10              2\n" +
                "  5     -1.0000000      0.0000000     -2.0000000      0.0000000      4.0020000\n" +
                "  3      0.0000000      0.0000000      1.0000000      0.0000000      2.9990000\n" +
                "  7      0.0000000      1.0000000      0.0000000     -2.0000000      0.0000000\n" +
                "  4      0.0000000     -1.0000000      0.0000000      1.0000000      2.0000000\n" +
                "  9      0.0000000      0.0000000      1.0000000      0.0000000      1.9980000\n" +
                "  1     -1.0000000      0.0000000     -1.0000000      0.0000000      2.0010000\n" +
                " 11      0.0000000     -1.0000000      0.0000000      1.0000000      0.9990000\n" +
                "-12      0.0000000      1.0000000      0.0000000     -1.0000000      0.9990000\n" +
                "        -1.0000000      0.0000000     -1.0000000     -1.0000000      2.0010000\n";

        Assertions.assertEquals(res, solver1.getOutput());
    }


    @Test
    void testWrongMRU() throws IOException {
        solver13.createSolverFile();
        solver13.run();
        solver13.display();
        solver13.parseOutput();

        String res = "This problem is infeasible\n";

        Assertions.assertEquals(res, solver13.getOutput());
    }

    @Test
    void testRightFunction() throws IOException {
        solver14.createSolverFile();
        solver14.run();
        solver14.display();
        solver14.parseOutput();

        String res = "\nValue of objective function: 4.00000000\n" +
                "\n" +
                "Actual values of the variables:\n" +
                "x1                              2\n" +
                "x2                              2\n";

        Assertions.assertEquals(res, solver14.getOutput());
    }
}