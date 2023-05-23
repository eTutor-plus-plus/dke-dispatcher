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

//    public List<Map<String,String>> getIsBCNF() {
//        List<Relation> exercises = relationRepository.findAll();
//        List<Map<String,String>> returnList = new ArrayList<>();
//        for (Relation exercise: exercises) {
//            Map<String, String> map = new HashMap<>();
//            map.put("id", exercise.getId().toString());
//            map.put("BCNF", Boolean.toString(isBCNF(exercise)));
//            map.put("3NF", Boolean.toString(is3NF(exercise)));
//            map.put("2NF", Boolean.toString(is2NF(exercise)));
//            returnList.add(map);
//        }
//        return returnList;
//    }
//
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
