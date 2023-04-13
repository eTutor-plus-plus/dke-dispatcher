package at.jku.dke.etutor.modules.fd.controller;

import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.services.DependencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/fd")
public class DependencyController {
    DependencyService dependencyService;

    DependencyController(DependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }

    @GetMapping("/dependencies")
    public List<Dependency> getAllDependencies() {
        return dependencyService.getAll();
    }

    @PostMapping("/new_dependency")
    public boolean newDependency(@RequestBody Dependency dependency) {
        return dependencyService.newDependency(dependency);
    }
}
