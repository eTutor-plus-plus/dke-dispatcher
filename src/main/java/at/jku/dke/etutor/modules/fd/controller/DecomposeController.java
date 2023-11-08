package at.jku.dke.etutor.modules.fd.controller;

import at.jku.dke.etutor.modules.fd.entities.Relation;
import at.jku.dke.etutor.modules.fd.services.DecomposeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/fd")
public class DecomposeController {
    DecomposeService decomposeService;
    DecomposeController(DecomposeService decomposeService) {this.decomposeService = decomposeService;}

    @GetMapping("/decompose")
    public List<Relation> isBCNF(@RequestParam Long id) {
        return decomposeService.getDecompose(id);
    }
}
