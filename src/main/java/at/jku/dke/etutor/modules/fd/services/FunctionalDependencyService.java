package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.FunctionalDependency;
import at.jku.dke.etutor.modules.fd.repositories.FunctionalDependencyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunctionalDependencyService {
    FunctionalDependencyRepository repository;

    public FunctionalDependencyService(FunctionalDependencyRepository repository) {
        this.repository = repository;
    }

    public List<FunctionalDependency> getAll() {
        List<FunctionalDependency> dependencies = new ArrayList<>();
        repository.findAll().forEach(dependency -> dependencies.add(dependency));
        return dependencies;
    }
//    public boolean newDependency(FunctionalDependency functionalDependency) {
//        if (functionalDependency.getId() == null || !repository.existsById(functionalDependency.getId())) {
//            try {
//                repository.save(functionalDependency);
//            }
//            catch (Exception e) {
//                return false;
//            }
//            return true;
//        }
//        return false;
//    }


}
