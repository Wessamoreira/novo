package junit;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest 
public class TesteRedisHelperIT {

    @Autowired
    private TesteRedisHelper helper;

    @Test
    public void prepararCidade9999NoRedis() {
        helper.gravarCidade9999();
        
    }
}
