package fm.ask.kplavins;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HomeworkApplication.class)
@ActiveProfiles("test")
public class HomeworkApplicationTests {

	@Test
	public void integrationSmokeTest() {
	}

}
