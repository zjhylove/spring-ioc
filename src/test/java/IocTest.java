import com.zjhy.framework.ioc.core.XmlApplicationContext;
import testBean.Robot;

import java.io.File;
import java.net.URL;

/**
 * 测试IOC
 */
public class IocTest {

    public static void main(String[] args) throws Exception {

        URL resource = Thread.currentThread().getContextClassLoader().getResource("applicationContext.xml");

        XmlApplicationContext context = new XmlApplicationContext(new File(resource.getPath()));

        Robot robot = (Robot) context.getBean("robot");

        robot.show();
    }
}
