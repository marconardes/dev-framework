

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import devframework.op.uploadfile.PersistFileTest;
import devframework.services.ClassValidationServiceTest;
import devframework.utils.ClassLoaderUtilsTest;
import devframework.webservice.InvokerTest;


@RunWith(Suite.class)
@SuiteClasses({ ClassValidationServiceTest.class, PersistFileTest.class, ClassLoaderUtilsTest.class, InvokerTest.class})
public class AllTests {

}