package nextstep.helloworld.mvc.mapping;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/param-header")
public class ParamHeaderController {

    @GetMapping("/message")
    public ResponseEntity<String> message() {
        return ResponseEntity.ok().body("message");
    }

    @GetMapping(path = "/message", params = "name")
    public ResponseEntity<String> messageForParam(@RequestParam("name") String name) {
        return ResponseEntity.ok().body(name);
    }

    @GetMapping(path = "/message", headers = "HEADER")
    public ResponseEntity<String> messageForHeader(HttpServletRequest request) {
        final String header = request.getHeader("HEADER");
        return ResponseEntity.ok().body(header);
    }
}