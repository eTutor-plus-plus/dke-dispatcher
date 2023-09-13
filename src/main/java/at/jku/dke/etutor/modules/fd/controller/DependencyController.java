package at.jku.dke.etutor.modules.fd.controller;

import at.jku.dke.etutor.modules.fd.entities.FunctionalDependency;
import at.jku.dke.etutor.modules.fd.services.FunctionalDependencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/fd")
public class DependencyController {
    FunctionalDependencyService functionalDependencyService;

    DependencyController(FunctionalDependencyService functionalDependencyService) {
        this.functionalDependencyService = functionalDependencyService;
    }

    @GetMapping("/dependencies")
    public List<FunctionalDependency> getAllDependencies() {
        return functionalDependencyService.getAll();
    }

}
