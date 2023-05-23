package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Relation;
import at.jku.dke.etutor.modules.fd.repositories.DependencyRepository;
import at.jku.dke.etutor.modules.fd.repositories.RelationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static at.jku.dke.etutor.modules.fd.entities.Relation.calculateRelation;
import static at.jku.dke.etutor.modules.fd.solve.CalculateClosure.calculateClosures;
import static at.jku.dke.etutor.modules.fd.solve.CalculateKeys.calculateKeys;
import static at.jku.dke.etutor.modules.fd.solve.CalculateMinimalCover.calculateMinimalCover;
import static at.jku.dke.etutor.modules.fd.solve.CalculateNormalForm.calculateNormalForm;

@Service
public class RelationService {
    RelationRepository relationRepository;
    DependencyRepository dependencyRepository;

    RelationService(RelationRepository relationRepository, DependencyRepository dependencyRepository) {
        this.relationRepository = relationRepository;
        this.dependencyRepository = dependencyRepository;
    }

    public boolean createExercise(Relation relation) {
        try {
            if (relation.getAttributes()==null || relation.getAttributes().length==0) {
                relation.setAttributes(calculateRelation(relation));
            }
            relation.setClosures(calculateClosures(relation));
            relation.setKeys(calculateKeys(relation));
            relation.setMinimalCovers(calculateMinimalCover(relation));
            relation.setNormalForm(calculateNormalForm(relation));
            relationRepository.save(relation);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Relation getExerciseById(long id) {
        Optional<Relation> optionalExercise = relationRepository.findById(id);
        Relation relation;
        if (optionalExercise.isPresent()) {
            relation = optionalExercise.get();
            return relation;
        }
        return null;
    }
    public List<Relation> getAll() {
        return relationRepository.findAll();
    }
    public void deleteExerciseById(Long id){
        relationRepository.deleteById(id);
    }
    public void deleteAll() {
        relationRepository.deleteAll();
    }


    public Long getNextId() {
        return relationRepository.getNextId();
    }
}
