package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.repositories.DependencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DependencyService {
    @Autowired
    DependencyRepository repository;

    public List<Dependency> getAll() {
        List<Dependency> dependencies = new ArrayList<>();
        repository.findAll().forEach(dependency -> dependencies.add(dependency));
        return dependencies;
    }
    public boolean newDependency(Dependency dependency) {
        if (dependency.getId() == null || !repository.existsById(dependency.getId())) {
            try {
                repository.save(dependency);
            }
            catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }


}
