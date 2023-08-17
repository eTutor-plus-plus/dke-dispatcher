package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Relation;
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

    RelationService(RelationRepository relationRepository) {
        this.relationRepository = relationRepository;
    }

    public boolean createGroup(Relation input) {
        Relation relation = getRelationById(input.getId());
        if (relation==null) {
            relation = new Relation();
            relation.setId(input.getId());
        }
        else {
            relationRepository.delete(relation);
        }
        try {
            relation.setFunctionalDependencies(input.getFunctionalDependencies());
            if (input.getAttributes()==null || input.getAttributes().length==0) {
                relation.setAttributes(calculateRelation(input));
            }
            else {
                relation.setAttributes(input.getAttributes());
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

    public Relation getRelationById(long id) {
        Optional<Relation> optionalRelation = relationRepository.findById(id);
        Relation relation;
        if (optionalRelation.isPresent()) {
            relation = optionalRelation.get();
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
