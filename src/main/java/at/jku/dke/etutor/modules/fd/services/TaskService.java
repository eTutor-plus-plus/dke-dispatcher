package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.*;
import at.jku.dke.etutor.modules.fd.repositories.*;
import at.jku.dke.etutor.modules.fd.utilities.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {
    private final RelationRepository relationRepository;
    private final TaskRepository taskRepository;
    private final ClosureRepository closureRepository;
    private final FunctionalDependencyRepository functionalDependencyRepository;


    public TaskService(RelationRepository relationRepository, TaskRepository taskRepository,
                ClosureRepository closureRepository,
                       FunctionalDependencyRepository functionalDependencyRepository) {
        this.relationRepository = relationRepository;
        this.taskRepository = taskRepository;
        this.closureRepository = closureRepository;
        this.functionalDependencyRepository = functionalDependencyRepository;
    }

    public ResponseEntity<Long> createClosureTasks(Long relationId, String[] fDClosureIds) {
        Long groupId = taskRepository.getLatestGroupId();

        if (groupId == null) {
            groupId = 1L;
        } else {
            groupId = groupId +1;
        }
        Optional<Relation> optionalRelation = relationRepository.findById(relationId);
        if (optionalRelation.isPresent()) {
            Relation relation = optionalRelation.get();
            Set<Task> tasks = relation.getTasks();
            for (String fDClosureId : fDClosureIds) {
                Long closureId;
                try {
                    closureId = Long.parseLong(fDClosureId);
                } catch (NumberFormatException e) {
                    return ResponseEntity.status(400).body(null);
                }
                Optional<Closure> optionalClosure = closureRepository.findById(closureId);
                if (optionalClosure.isPresent()) {
                    tasks.add(new Task(relation, "Closure", groupId, optionalClosure.get()));
                } else {
                    return ResponseEntity.status(404).body(null);
                }
            }
            relationRepository.save(relation);
            return ResponseEntity.status(200).body(groupId);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
    public ResponseEntity<Long> updateClosureTask(String inputId, Long relationId, String fDSubtype, String[] fDClosureIds) {
        Long groupId;
        try {
            groupId = Long.parseLong(inputId.replace("Closure-", ""));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }


        if (!fDSubtype.equals("Closure")) {
            taskRepository.deleteByClosureGroupId(groupId);
            return createTask(relationId, fDSubtype);
        }
        /** Das löschen ist notwenig da ein späteres löschen einzelnen nicht möglich ist. Spring Data JPA */
        Collection<Long> closureIds = new ArrayList<>();
        for (String fDClosureId: fDClosureIds) {
            Long closureId;
            try {
                closureId = Long.parseLong(fDClosureId);
                closureIds.add(closureId);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400).body(null);
            }
        }
        taskRepository.deleteByClosureGroupIdAndClosureNotIn(groupId, closureIds);

        Optional<Relation> optionalRelation = relationRepository.findById(relationId);
        Relation relation;
        if (optionalRelation.isPresent()) {
            relation = optionalRelation.get();
        } else {
            return ResponseEntity.status(404).body(null);
        }


        for (Long closureId: closureIds) {
            if (!taskRepository.checkClosureExists(groupId, closureId)) {
                Optional<Closure> optionalClosure = closureRepository.findById(closureId);
                if (optionalClosure.isPresent()) {
                    Closure closure = optionalClosure.get();
                    taskRepository.save(new Task(relation, "Closure", groupId, closure));
                } else {
                    return ResponseEntity.status(404).body(null);
                }
            }
        }
        return ResponseEntity.status(200).body(groupId);
    }

    public ResponseEntity<Long> createTask(Long relationId, String fDSubtype) {
        if (taskRepository.existsTask(relationId, fDSubtype)) {
            return ResponseEntity.status(409).body(null);
        } else {
            Optional<Relation> optionalRelation = relationRepository.findById(relationId);
            if (optionalRelation.isPresent()) {
                Relation relation = optionalRelation.get();
                Task newTask = new Task(relation, fDSubtype);
                taskRepository.save(newTask);
                return ResponseEntity.status(200).body(newTask.getId());
            } else {
                return ResponseEntity.status(404).body(null);
            }
        }
    }

    public ResponseEntity<Long> updateTask(String inputId, Long relationId, String fDSubtype, String[] fDClosureIds) {
        Long id;
        try {
            id = Long.parseLong(inputId);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }

        if (fDSubtype.equals("Closure")) {
            taskRepository.deleteTaskById(id);
            return createClosureTasks(relationId, fDClosureIds);
        }
        Relation relation;
        Optional<Relation> optionalRelation = relationRepository.findById(relationId);
        if (optionalRelation.isPresent()) {
            relation = optionalRelation.get();
        } else {
            return ResponseEntity.status(404).body(null);
        }

        taskRepository.updateRelationAndTypeById(relation, fDSubtype, id);
        return ResponseEntity.status(200).body(id);
    }

    public ResponseEntity<Long> deleteTask(Long id) {
        taskRepository.deleteTaskById(id);
        return ResponseEntity.status(200).body(id);
    }

    public ResponseEntity<Long> deleteClosureTask(Long id) {
        taskRepository.deleteByClosureGroupId(id);
        return ResponseEntity.status(200).body(id);
    }

    public ResponseEntity<Map<Long, String[]>> getLeftSidesClosure(Long groupId) {
        Map<Long, String[]> leftSideClosures = new TreeMap<>();
        for (Task task: taskRepository.getTasks(groupId)) {
            leftSideClosures.put(task.getId(), task.getClosure().getLeftSide());
        }
        return ResponseEntity.status(200).body(leftSideClosures);
    }

    public FDTaskSolveResponse fdTaskSolve(FDTaskSolve fdTaskSolve) {
        FDTaskSolveResponse response = new FDTaskSolveResponse(fdTaskSolve.getId());
        List<FDHint> hints = new ArrayList<>();
        response.setHints(hints);
        double percentage = 0;
        if (fdTaskSolve.getType().equals("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#Closure")) {
            percentage = solveClosures(fdTaskSolve, response, hints);
        } else if (fdTaskSolve.getType().equals("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#Normalform")) {
            percentage = solveNormalform(fdTaskSolve, response, hints);
        } else if (fdTaskSolve.getType().equals("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#Key")) {
            percentage = solveKey(fdTaskSolve, response, hints);
        } else if (fdTaskSolve.getType().equals("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#MinimalCover")) {
            percentage = solveMinimalCover(fdTaskSolve, response, hints);
        }
        response.setPoints(fdTaskSolve.getMaxPoints()*percentage);
        return response;
    }
    public FDTaskSolveResponse fdTaskGrade(FDTaskSolve fdTaskSolve) {
        FDTaskSolveResponse response = new FDTaskSolveResponse(fdTaskSolve.getId());
        List<FDHint> hints = new ArrayList<>();
        response.setHints(hints);
        double percentage = 0;
        if (fdTaskSolve.getType().equals("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#Closure")) {
            percentage = solveClosure(fdTaskSolve, response, hints);
        } else if (fdTaskSolve.getType().equals("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#Normalform")) {
            percentage = solveNormalform(fdTaskSolve, response, hints);
        } else if (fdTaskSolve.getType().equals("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#Key")) {
            percentage = solveKey(fdTaskSolve, response, hints);
        } else if (fdTaskSolve.getType().equals("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#MinimalCover")) {
            percentage = solveMinimalCover(fdTaskSolve, response, hints);
        }
        response.setPoints(fdTaskSolve.getMaxPoints()*percentage);
        return response;
    }
    private String[] parseSide(String side) {
        return side.replace(" ","").trim().split(",");
    }
    private double solveClosures(FDTaskSolve fdTaskSolve, FDTaskSolveResponse response, List<FDHint> hints){
        Long groupId = Long.parseLong(fdTaskSolve.getId().replace("Closure-",""));
        List<String> legalAttributes = Arrays.asList(taskRepository.findRelationByGroupId(groupId).getAttributes());
        int numberOfTasks = taskRepository.countByClosureGroupId(groupId);
        for (FDSolve fdSolve: fdTaskSolve.getClosureSolutions()) {
            if (fdSolve.getSolution().isEmpty()) {
                hints.add(new FDHint(fdSolve.getId(), "fDAssignment.hint.closure.empty"));
                response.setSolved(false);
                continue;
            }
            String[] parsedInput = parseSide(fdSolve.getSolution());
            List<String> duplicateCheck = new ArrayList<>();
            for (String item: parsedInput) {
                if (!legalAttributes.contains(item)) {
                    hints.add(new FDHint(fdSolve.getId(), "fDAssignment.hint.closure.illegalArgument"));
                    response.setSolved(false);
                    break;
                }
                if (duplicateCheck.contains(item)) {
                    hints.add((new FDHint(fdSolve.getId(), "fDAssignment.hint.closure.duplicate")));
                    response.setSolved(false);
                    break;
                } else {
                    duplicateCheck.add(item);
                }
            }
            List<String> answer = Arrays.asList(taskRepository.findClosureByTaskId(fdSolve.getId()).getRightSide());
            if (parsedInput.length>answer.size()) {
                hints.add((new FDHint(fdSolve.getId(), "fDAssignment.hint.closure.more")));
                response.setSolved(false);
                continue;
            } else if (parsedInput.length<answer.size()) {
                hints.add((new FDHint(fdSolve.getId(), "fDAssignment.hint.closure.less")));
                response.setSolved(false);
                continue;
            }
            if (response.isSolved()) {
                for (String item: parsedInput) {
                    if(!answer.contains(item)) {
                        hints.add((new FDHint(fdSolve.getId(), "fDAssignment.hint.closure.wrong")));
                        response.setSolved(false);
                        break;
                    }
                }
            }
        }
        Set destinctErrors = new TreeSet();
        for (FDHint hint: hints) {
            destinctErrors.add(hint.getSubId());
        }
        return ((numberOfTasks-destinctErrors.size())/Double.valueOf(numberOfTasks));
    }
    private double solveClosure(FDTaskSolve fdTaskSolve, FDTaskSolveResponse response, List<FDHint> hints) {
        FDSolve closure = fdTaskSolve.getClosureSolutions().get(0);
        List<String> legalAttributes = Arrays.asList(taskRepository.findRelationByTaskId(closure.getId()).getAttributes());
        if (closure.getSolution().isEmpty()) {
            hints.add(new FDHint(closure.getId(), "fDAssignment.hint.closure.empty"));
            response.setSolved(false);
            return 0;
        }
        String[] parsedInput = parseSide(closure.getSolution());
        List<String> duplicateCheck = new ArrayList<>();
        for (String item: parsedInput) {
            if (!legalAttributes.contains(item)) {
                hints.add(new FDHint(closure.getId(), "fDAssignment.hint.closure.illegalArgument"));
                response.setSolved(false);
                break;
            }
            if (duplicateCheck.contains(item)) {
                hints.add((new FDHint(closure.getId(), "fDAssignment.hint.closure.duplicate")));
                response.setSolved(false);
                break;
            } else {
                duplicateCheck.add(item);
            }
        }
        List<String> answer = Arrays.asList(taskRepository.findById(closure.getId()).get().getClosure().getRightSide());
        if (parsedInput.length>answer.size()) {
            hints.add((new FDHint(closure.getId(), "fDAssignment.hint.closure.more")));
            response.setSolved(false);
            return 0;
        } else if (parsedInput.length<answer.size()) {
            hints.add((new FDHint(closure.getId(), "fDAssignment.hint.closure.less")));
            response.setSolved(false);
            return 0;
        }
        if (response.isSolved()) {
            for (String item: parsedInput) {
                if(!answer.contains(item)) {
                    hints.add((new FDHint(closure.getId(), "fDAssignment.hint.closure.wrong")));
                    response.setSolved(false);
                    return 0;
                }
            }
            return 1;
        }
        return 0;
    }
    private double solveNormalform(FDTaskSolve fdTaskSolve, FDTaskSolveResponse response, List<FDHint> hints) {
        if (fdTaskSolve.getSolution() == null) {
            hints.add(new FDHint(null, "fDAssignment.hint.normalform.notSelected"));
            response.setSolved(false);
        } else {
            NF nFRelation = NF.valueOf(fdTaskSolve.getSolution().replace("http://www.dke.uni-linz.ac.at/etutorpp/Normalform#",""));
            for (FDSolve fdSolve: fdTaskSolve.getNormalFormSolutions()) {
                NF nFDependency;
                if (!fdSolve.getSolution().isEmpty()) {
                    nFDependency = NF.valueOf(fdSolve.getSolution().replace("http://www.dke.uni-linz.ac.at/etutorpp/Normalform#", ""));
                    if (nFRelation.ordinal()<=nFDependency.ordinal()) {
                        hints.add(new FDHint(fdSolve.getId(), "fDAssignment.hint.normalform.violates"));
                        response.setSolved(false);
                        continue;
                    }
                } else {
                    nFDependency = null;
                }
                Optional<FunctionalDependency> optionalFunctionalDependency = functionalDependencyRepository.findById(fdSolve.getId());
                if (optionalFunctionalDependency.isEmpty()) {
                    if(nFDependency != null) {
                        hints.add(new FDHint(fdSolve.getId(), "fDAssignment.hint.normalform.wrong"));
                        response.setSolved(false);
                    }
                } else {
                    if (nFDependency == null) {
                        hints.add(new FDHint(fdSolve.getId(), "fDAssignment.hint.normalform.wrong"));
                        response.setSolved(false);
                    } else if (!nFDependency.equals((optionalFunctionalDependency.get().getViolates()))) {
                        hints.add(new FDHint(fdSolve.getId(), "fDAssignment.hint.normalform.wrong"));
                        response.setSolved(false);
                    }
                }
            }
            if (response.isSolved()) {
                Optional<Task> optionalTask = taskRepository.findById((Long.valueOf(fdTaskSolve.getId())));
                NF answer = optionalTask.get().getRelation().getNormalForm();
                if (!answer.equals(nFRelation)) {
                    hints.add(new FDHint(null, "fDAssignment.hint.normalform.wrong"));
                    response.setSolved(false);
                }
            }
        }
        if (response.isSolved()) {
            return 1d;
        } else {
            return 0d;
        }
    }
    private double solveKey(FDTaskSolve fdTaskSolve, FDTaskSolveResponse response, List<FDHint> hints) {
        if(fdTaskSolve.getSolution() == null) {
            response.setSolved(false);
            hints.add(new FDHint(null, "fDAssignment.hint.key.empty"));
            return 0d;
        } else {
            double numberOfKeys;
            Set<Key> solvedKeys = new HashSet<>();
            List<String> lines = fdTaskSolve.getSolution().lines().toList();
            List<String> legalAttributes = Arrays.asList(taskRepository.findRelationByTaskId(Long.parseLong(fdTaskSolve.getId())).getAttributes());
            Long lineNumber = 0L;
            List<Set<String>> splitted = new ArrayList();

            for (String line: lines) {
                String[] solution = line.replace(" ","").split(",");
                lineNumber++;
                if (line.length()==0) {
                    hints.add(new FDHint(lineNumber, "fDAssignment.hint.key.empty"));
                    response.setSolved(false);
                    continue;
                }
                for (String item: solution) {
                    if (!legalAttributes.contains(item)) {
                        hints.add(new FDHint(lineNumber, "fDAssignment.hint.key.illegalArgument"));
                        response.setSolved(false);
                        break;
                    }
                }
                List<String> duplicateCheck = new ArrayList<>();
                for (String item: solution) {
                    if (duplicateCheck.contains(item)) {
                        hints.add((new FDHint(lineNumber, "fDAssignment.hint.key.duplicate")));
                        response.setSolved(false);
                        break;
                    } else {
                        duplicateCheck.add(item);
                    }
                }
                splitted.add(new TreeSet<>(List.of(solution)));
            }
            Set<Key> keys = taskRepository.findRelationByTaskId(Long.parseLong(fdTaskSolve.getId())).getKeys();
            numberOfKeys = keys.size();
            if (lines.size() > keys.size()) {
                hints.add((new FDHint(lineNumber, "fDAssignment.hint.key.more")));
                response.setSolved(false);
            }
            if (lines.size() < keys.size()) {
                hints.add((new FDHint(lineNumber, "fDAssignment.hint.key.less")));
                response.setSolved(false);
            }

            for (Set<String> item: splitted) {
                for (Key key : keys) {
                    String[] keyAnswer = key.getLeftSide();
                    if (item.size() == key.getLeftSide().length &&
                            item.containsAll(Arrays.stream(keyAnswer).toList())) {
                        solvedKeys.add(key);
                    }
                }
            }
            if (solvedKeys.size() != keys.size() && response.isSolved()) {
                hints.add((new FDHint(lineNumber, "fDAssignment.hint.key.wrong")));
                response.setSolved(false);
            }
            return ((solvedKeys.size())/numberOfKeys);
        }
    }
    private double solveMinimalCover(FDTaskSolve fdTaskSolve, FDTaskSolveResponse response, List<FDHint> hints) {
        if(fdTaskSolve.getSolution() == null) {
            response.setSolved(false);
            hints.add(new FDHint(null, "fDAssignment.hint.minimalCover.empty"));
            return 0d;
        } else {
            List<String> lines = fdTaskSolve.getSolution().lines().toList();
            List<String> legalAttributes = Arrays.asList(taskRepository.findRelationByTaskId(Long.parseLong(fdTaskSolve.getId())).getAttributes());
            Long lineNumber = 0L;
            List<FDSolveDependency> checkedInput = new ArrayList<>();
            for (String line: lines) {
                lineNumber++;
                String[] sides = line.split("(->|-|>)");
                Set<String> leftSide;
                Set<String> ríghtSide;
                if (sides.length < 2 ) {
                    hints.add(new FDHint(lineNumber,"fDAssignment.hint.minimalCover.sideMissing"));
                    response.setSolved(false);
                    continue;
                } else if ( sides.length > 2){
                    hints.add(new FDHint(lineNumber,"fDAssignment.hint.minimalCover.format"));
                    response.setSolved(false);
                    continue;
                }
                String [] leftSideRaw = sides[0].replace(" ","").split(",");
                leftSide = new TreeSet<>(List.of(leftSideRaw));
                if (!legalAttributes.containsAll(leftSide)) {
                    hints.add((new FDHint(lineNumber, "fDAssignment.hint.minimalCover.illegalArgumentLeft")));
                    response.setSolved(false);
                }
                if (leftSideRaw.length > leftSide.size()) {
                    hints.add((new FDHint(lineNumber, "fDAssignment.hint.minimalCover.duplicateLeft")));
                    response.setSolved(false);
                }
                String [] ríghtSideRaw = sides[1].replace(" ","").split(",");
                ríghtSide = new TreeSet<>(List.of(ríghtSideRaw));
                if (!legalAttributes.containsAll(ríghtSide)) {
                    hints.add((new FDHint(lineNumber, "fDAssignment.hint.minimalCover.illegalArgumentRight")));
                    response.setSolved(false);
                }
                if (ríghtSideRaw.length > ríghtSide.size()) {
                    hints.add((new FDHint(lineNumber, "fDAssignment.hint.minimalCover.duplicateRight")));
                    response.setSolved(false);
                }
                if (leftSide.containsAll(ríghtSide)) {
                    hints.add((new FDHint(lineNumber, "fDAssignment.hint.minimalCover.trivial")));
                    response.setSolved(false);
                }
                if (ríghtSide.size()>1) {
                    hints.add((new FDHint(lineNumber, "fDAssignment.hint.minimalCover.canonical")));
                    response.setSolved(false);
                }
                checkedInput.add(new FDSolveDependency(leftSide,ríghtSide));
            }
            List<Dependency> answers = new ArrayList<>();
            Relation relation = taskRepository.findRelationByTaskId(Long.parseLong(fdTaskSolve.getId()));
            for (FunctionalDependency dependency: relation.getFunctionalDependencies()) {
                if (dependency.getMinimalCover().isEmpty()) {
                    answers.add(dependency);
                } else {
                    for (MinimalCover minimalCover: dependency.getMinimalCover()) {
                        answers.add(minimalCover);
                    }
                }
            }
            if (checkedInput.size() < answers.size() && response.isSolved()) {
                hints.add((new FDHint(null, "fDAssignment.hint.minimalCover.less")));
                response.setSolved(false);
            }
            if (checkedInput.size() > answers.size() && response.isSolved()) {
                hints.add((new FDHint(null, "fDAssignment.hint.minimalCover.more")));
                response.setSolved(false);
            }
            if (response.isSolved()) {
                List<Dependency> solved = new ArrayList<>();
                lineNumber = 0L;
                for (FDSolveDependency item: checkedInput) {
                    lineNumber++;
                    for (Dependency answer: answers) {
                        if (item.getLeftSide().size() == answer.getLeftSide().length &&
                                item.getRightSide().size() == answer.getRightSide().length &&
                                item.getLeftSide().containsAll(Arrays.asList(answer.getLeftSide())) &&
                                item.getRightSide().containsAll(Arrays.asList(answer.getRightSide()))) {
                            solved.add(answer);
                        } else if (item.getLeftSide().size() > answer.getLeftSide().length &&
                                item.getRightSide().size() == answer.getRightSide().length &&
                                item.getLeftSide().containsAll(Arrays.asList(answer.getLeftSide())) &&
                                item.getRightSide().containsAll(Arrays.asList(answer.getRightSide()))) {
                            hints.add((new FDHint(lineNumber, "fDAssignment.hint.minimalCover.redundantAttribute")));
                            response.setSolved(false);
                        }

                    }
                }
                if (solved.size() != answers.size()) {
                    hints.add((new FDHint(null, "fDAssignment.hint.minimalCover.wrong")));
                    response.setSolved(false);
                }
            }
            if (response.isSolved()) {
                return 1d;
            } else {
                return 0d;
            }
        }
    }

}
