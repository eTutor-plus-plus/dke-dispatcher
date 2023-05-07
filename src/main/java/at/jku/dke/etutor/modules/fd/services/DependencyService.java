package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.FunctionalDependency;
import at.jku.dke.etutor.modules.fd.repositories.DependencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DependencyService {
    @Autowired
    DependencyRepository repository;

    public List<FunctionalDependency> getAll() {
        List<FunctionalDependency> dependencies = new ArrayList<>();
        repository.findAll().forEach(dependency -> dependencies.add(dependency));
        return dependencies;
    }
    public boolean newDependency(FunctionalDependency functionalDependency) {
        if (functionalDependency.getId() == null || !repository.existsById(functionalDependency.getId())) {
            try {
                repository.save(functionalDependency);
            }
            catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }


}
