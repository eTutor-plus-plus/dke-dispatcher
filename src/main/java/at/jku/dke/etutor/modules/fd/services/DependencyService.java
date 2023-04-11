package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.repositories.DependencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class DependencyService {
    @Autowired
    DependencyRepository repository;

    public List<Dependency> getAll() {
        List<Dependency> dependencies = new ArrayList<>();
        repository.findAll().forEach(dependency -> dependencies.add(dependency));
        for (Dependency dependency: dependencies) {
            System.out.println(dependency.getId());
        }
        return dependencies;
    }
}
