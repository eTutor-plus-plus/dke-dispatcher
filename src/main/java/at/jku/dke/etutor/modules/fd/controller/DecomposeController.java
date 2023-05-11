package at.jku.dke.etutor.modules.fd.controller;

import at.jku.dke.etutor.modules.fd.services.DecomposeService;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/fd")
public class DecomposeController {
    DecomposeService decomposeService;
    DecomposeController(DecomposeService decomposeService) {this.decomposeService = decomposeService;}

    @GetMapping("/are_BCNF")
    public List<Map<String, String>> areBCNF() {
        return decomposeService.getIsBCNF();
    }
    @GetMapping("/is_BCNF")
    public Map<String, String> isBCNF(@RequestParam Long id) {
        return decomposeService.getIsBCNF(id);
    }
}
