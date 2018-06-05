package suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//先执行其他模块的case
@RunWith(Suite.class)
@Suite.SuiteClasses({
        junitdemo.Test1.class
})

public class DemoSuite  {

}
