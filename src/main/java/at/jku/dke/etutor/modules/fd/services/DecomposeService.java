package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Relation;
import at.jku.dke.etutor.modules.fd.repositories.RelationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static at.jku.dke.etutor.modules.fd.solve.CalculateDecompose.decomposeFolien;

@Service
public class DecomposeService {
    RelationRepository relationRepository;
    DecomposeService(RelationRepository relationRepository) {this.relationRepository = relationRepository;}

    public List<Relation> getDecompose(Long id) {
        Optional<Relation> optionalExercise = relationRepository.findById(id);
        Relation relation;
        if (optionalExercise.isPresent()) {
            relation = optionalExercise.get();
            return decomposeFolien(relation);
        }
        return null;
    }
}
