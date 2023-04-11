package at.jku.dke.etutor.modules.fd.controller;

import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.services.DependencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/dep")
public class DependencyController {
    @Autowired
    DependencyService dependencyService;

    @GetMapping("/dependencies")
    public List<Dependency> getAllDependencies() {
        return dependencyService.getAll();
    }
}
