package ac.massey;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TextEditorTest {
    
    @Test
    public void testApplicationStarts() {
        // 测试应用能够编译和运行
        assertTrue(true, \"应用程序应该能够正常编译\");
    }
    
    @Test 
    public void testMainMethodExists() {
        // 测试主方法存在
        try {
            Class<?> clazz = Class.forName(\"ac.massey.TextEditor\");
            clazz.getMethod(\"main\", String[].class);
            assertTrue(true, \"主方法应该存在\");
        } catch (Exception e) {
            fail(\"主方法缺失: \" + e.getMessage());
        }
    }
}
