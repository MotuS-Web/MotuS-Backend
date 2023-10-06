package website.motus;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @RequestMapping("/ci-cd")
    public String cicdTest(){
        return "CI / CD Test Success";
    }
}
