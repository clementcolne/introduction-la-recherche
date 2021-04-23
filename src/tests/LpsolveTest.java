package tests;

import org.junit.jupiter.api.Assertions;
import solver.AbstractSolver;
import solver.Lpsolve;

import java.io.File;
import java.io.IOException;

class LpsolveTest {

    private AbstractSolver solver1;
    private AbstractSolver solver2;
    private Lpsolve solver3;
    private AbstractSolver solver5;
    private Lpsolve solver4;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        solver1 = new Lpsolve("test.txt", "-S7", "lp_solve");
        solver2 = new Lpsolve("./wrongExtension.mp3", "-S7", "lp_solve");
        solver3 = new Lpsolve("./testWrong.txt", "-S7", "lp_solve");
        solver4 = new Lpsolve("./wrongExtension.mp3", "-S7", "lp_solve");
        solver5 = new Lpsolve("./wrong_path.txt", "-S7", "lp_solve");
    }

    @org.junit.jupiter.api.Test
    void testCreateLpFileRight() {
        solver1.createLpFile();
        solver2.createLpFile();
        solver3.createLpFile();
        solver4.createLpFile();
        solver5.createLpFile();

        File file = new File(solver1.getLpFile());
        File file2 = new File(solver2.getLpFile());
        File file3 = new File(solver3.getLpFile());

        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file2.exists());
        Assertions.assertTrue(file3.exists());
    }

    @org.junit.jupiter.api.Test
    void testCreateLpFileWrong() {
        solver5.createLpFile();

        File file = new File(solver5.getLpFile());

        Assertions.assertTrue(file.exists());
    }

    @org.junit.jupiter.api.Test
    void testParseOutputWrongExtension() throws IOException {
        solver2.createLpFile();
        solver2.run();
        solver2.display();
        solver2.parseOutput();
    }

    @org.junit.jupiter.api.Test
    void testParseOutputEmptyTxt() throws IOException {
        solver3.createLpFile();
        solver3.run();
        solver3.display();
        solver3.parseOutput();
    }

    @org.junit.jupiter.api.Test
    void retryLpFileWrongExtension() {
        solver4.createLpFile();
        solver4.retryLpFile();
    }

    @org.junit.jupiter.api.Test
    void retryLpFileEmptyTxt() {
        solver3.createLpFile();
        solver3.retryLpFile();
    }
}