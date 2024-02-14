package at.jku.dke.etutor.modules.rt.analysis;

import java.util.*;

public class RTSemanticsAnalysis {
    List<String> studentSolution;
    List<String> solution;
    List<String> relationsStudent = new ArrayList();
    List<String> relations = new ArrayList<>();
    Map<String,List<String>> pkStudent = new HashMap<String,List<String>>();
    Map<String,List<String>> pk = new HashMap<String,List<String>>();
    Map<String,List<String>> attributes = new HashMap<String,List<String>>();
    Map<String,List<String>>attributesStudent = new HashMap<String,List<String>>();
    Map<String,String> dependencies = new HashMap<String,String>();
    Map<String,String> dependenciesStudent = new HashMap<String,String>();
    String errorLogSemantik = "";
    String errorLogSyntax = "";
    int [] weighting;
    int pointsPK = 0;
    int pointsAtt = 0;
    int pointsDep = 0;
    int countRelations = 0;
    List<String> allowedAttributes = new ArrayList<>();
    List<String> allowedAttributesStudent = new ArrayList<>();


    public RTSemanticsAnalysis(List<String> studentSolution, List<String> solution, int [] weighting) {
        this.studentSolution = studentSolution;
        solutionToUppercase(solution);
        this.weighting = weighting;
        this.calcRelations();
        this.countRelations();
        this.clacPrimaryKey();
        this.clacAttributes();
        this.clacDependendcies();
        this.checkAllowedAttributtes();
        this.checkPrimaryKey();
        this.checkAttributes();
        this.checkDependendcies();
        this.checkRelation();
    }

    public void countRelations(){
        this.countRelations = relations.size();
    }

    private void calcRelations() {
        for (String str : studentSolution) {
            int indexOfOpeningParenthesis = str.indexOf('(');
            if (indexOfOpeningParenthesis != -1) {
                String result = str.substring(0, indexOfOpeningParenthesis);
                this.relationsStudent.add(result);
            }
        }

        for (String str : solution) {
            int indexOfOpeningParenthesis = str.indexOf('(');
            if (indexOfOpeningParenthesis != -1) {
                String result = str.substring(0, indexOfOpeningParenthesis);
                this.relations.add(result);
            }
        }
    }

    public String getRelation(String input) {
        String relation = "";
        int indexOfOpeningParenthesis = input.indexOf('(');
        if (indexOfOpeningParenthesis != -1) {
            relation = input.substring(0, indexOfOpeningParenthesis);
        }
        return relation;
    }

    public boolean checkRelation(){
        for(String str : this.relations){
            if(!this.relationsStudent.contains(str)){
                this.errorLogSyntax = this.errorLogSyntax.concat("<br>Wrong or missing relation! (Tip: check spelling!)");
                return false;
            }
        }
        return true;
    }

    public boolean checkPrimaryKey(){
        boolean check = true;
        for (Map.Entry<String,List<String>> entry : this.pk.entrySet()) {
            List<String> solution = entry.getValue();
            List<String> student = this.pkStudent.get(entry.getKey());
            try {
                if (!student.containsAll(solution) || student.size() != solution.size() || !solution.containsAll(student)) {
                    this.errorLogSemantik = this.errorLogSemantik.concat("<br>Error in the relation " + entry.getKey() + ": Incorrect primary key!");
                    check = false;
                } else {
                    pointsPK = pointsPK + (weighting[0] / countRelations);
                }
            } catch (Exception e) {
            }
        }
        return check;
    }

    public boolean checkAttributes(){
        boolean check = true;
        for (Map.Entry<String,List<String>> entry : this.attributes.entrySet()) {
            List<String> solution = entry.getValue();
            List<String> student = this.attributesStudent.get(entry.getKey());
            try {
            if (solution == null && student == null){
                pointsAtt = pointsAtt + (weighting[1] / countRelations);
            }
            else if (!student.containsAll(solution) || solution.size() != student.size() || !solution.containsAll(student)) {
                this.errorLogSemantik = this.errorLogSemantik.concat("<br>Error in the relation " + entry.getKey() + ": Incorrect, redundant or missing attribute(s)!");
                check = false;
            } else {
                pointsAtt = pointsAtt + (weighting[1] / countRelations);
            }
        } catch (Exception e){
                this.errorLogSemantik = this.errorLogSemantik.concat("<br>Error in the relation " + entry.getKey() + ": Incorrect, redundant or missing attribute(s)!");
                check = false;
            }
        }
        return check;
    }

    public boolean checkDependendcies(){
        List<String> solutionList = null;
        List<String> studentList = null;
        boolean check = true;
        for (Map.Entry<String,String> entry : this.dependencies.entrySet()){
            String solution = entry.getValue();
            String student = this.dependenciesStudent.get(entry.getKey());
            if(solution != null && solution.length() > 0) {
                solution = solution.substring(1);
                solutionList = Arrays.asList(solution.split("\\s*,\\s*"));
            }
            if (student != null && student.length() > 0) {
                student = student.substring(1);
                studentList = Arrays.asList(student.split("\\s*,\\s*"));
            }
            if(studentList == null && solutionList == null) {
                pointsDep = pointsDep + (weighting[2] / countRelations);
            }
            else if (!solutionList.equals(studentList)) {
                if(studentList != null && solutionList != null) {
                    int pointsForRightAnswer = weighting[2] / countRelations / solutionList.size();
                    this.errorLogSemantik = this.errorLogSemantik.concat("<br>Error in the relation " + entry.getKey() + ": Incorrect, redundant or missing inclusion dependency/dependencies!");
                    for (String elem : studentList) {
                        if (solutionList.contains(elem)) {
                            pointsDep += pointsForRightAnswer;
                        }
                    }
                    int sizeDifference = studentList.size() - solutionList.size();
                    if (pointsDep > 0 && sizeDifference > 0) {
                        pointsDep -= pointsForRightAnswer;
                    }
                    check = false;
                }
            }
            else{
                pointsDep = pointsDep + (weighting[2] / countRelations);
            }
        }
        return check;
    }

    public void clacPrimaryKey(){
        for(String str : this.solution) {
            int indexOfFirstPipe = str.indexOf('#');
            int indexOfLastPipe = str.lastIndexOf('#');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String key = this.getRelation(str);
                try{
                    String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                    String[] words = extractedText.split(",");
                    this.pk.put(key, Arrays.stream(words).toList());
                }catch (Exception e){
                    this.pk.put(key,null);
                }
            }
        }

        for(String str : this.studentSolution) {
            int indexOfFirstPipe = str.indexOf('#');
            int indexOfLastPipe = str.lastIndexOf('#');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String key = this.getRelation(str);
                try{
                    String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                    String[] words = extractedText.split(",");
                    this.pkStudent.put(key, Arrays.stream(words).toList());
                }catch (Exception e){
                    this.pkStudent.put(key,null);
                }
            }
        }
    }

    public void clacAttributes(){
        for(String str : this.solution) {
            int indexOfFirstPipe = str.lastIndexOf('#');
            int indexOfLastPipe = str.indexOf(')');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String key = this.getRelation(str);
                try {
                    String extractedText = str.substring(indexOfFirstPipe + 2, indexOfLastPipe);
                    String[] words = extractedText.split(",");
                    this.attributes.put(key, Arrays.stream(words).toList());
                }
                catch (Exception e){
                    this.attributes.put(key, null);
                }
            }
        }

        for(String str : this.studentSolution) {
            int indexOfFirstPipe = str.lastIndexOf('#');
            int indexOfLastPipe = str.indexOf(')');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String key = this.getRelation(str);
                try {
                    String extractedText = str.substring(indexOfFirstPipe + 2, indexOfLastPipe);
                    String[] words = extractedText.split(",");
                    this.attributesStudent.put(key, Arrays.stream(words).toList());
                }
                catch (Exception e){
                    this.attributesStudent.put(key, null);
                }
            }
        }
    }

    public void clacDependendcies() {
        for (String str : this.solution) {
            int indexOfFirstPipe = str.indexOf(')');
            int indexOfLastPipe = str.length();
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                String key = this.getRelation(str);
                this.dependencies.put(key,extractedText);
            }
        }

        for (String str : this.studentSolution) {
            int indexOfFirstPipe = str.indexOf(')');
            int indexOfLastPipe = str.length();
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                String key = this.getRelation(str);
                this.dependenciesStudent.put(key,extractedText);
            }
        }
    }

    public void checkAllowedAttributtes(){
        for (Map.Entry<String,List<String>> entry : this.attributesStudent.entrySet()) {
            if(entry.getValue() != null) {
                allowedAttributesStudent.addAll(entry.getValue());
            }
        }
        for (Map.Entry<String,List<String>> entry : this.pkStudent.entrySet()) {
            allowedAttributesStudent.addAll(entry.getValue());
        }
        for (Map.Entry<String,List<String>> entry : this.attributes.entrySet()) {
            if(entry.getValue() != null) {
                allowedAttributes.addAll(entry.getValue());
            }
        }
        for (Map.Entry<String,List<String>> entry : this.pk.entrySet()) {
            allowedAttributes.addAll(entry.getValue());
        }
        allowedDependendcies();
    }

    public void allowedDependendcies() {
        for (String str : this.solution) {
            int indexOfFirstPipe = str.indexOf(')');
            int indexOfLastPipe = str.length();
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                String key = this.getRelation(str);
                extractedText = extractedText.replaceAll("\\s+", "");
                extractedText = extractedText.replaceAll(",", "");
                extractedText = extractedText.replaceAll("=", "");
                String[] parts = extractedText.split("[()]");
                for (String part : parts) {
                    if (!part.isEmpty()) {
                        this.allowedAttributes.add(part);
                    }
                }
            }
        }

        for (String str : this.studentSolution) {
            int indexOfFirstPipe = str.indexOf(')');
            int indexOfLastPipe = str.length();
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                String key = this.getRelation(str);
                List<String> wordsList = new ArrayList<>();
                extractedText = extractedText.replaceAll("\\s+", "");
                extractedText = extractedText.replaceAll(",", "");
                extractedText = extractedText.replaceAll("=", "");
                String[] parts = extractedText.split("[()]");
                for (String part : parts) {
                    if (!part.isEmpty()) {
                        this.allowedAttributesStudent.add(part);
                    }
                }

            }
        }
    }

    public List<String> getStudentSolution() {
        return studentSolution;
    }

    public List<String> getSolution() {
        return solution;
    }

    public List<String> getRelationsStudent() {
        return relationsStudent;
    }

    public List<String> getRelations() {
        return relations;
    }

    public Map<String,List<String>> getPk() {
        return pk;
    }

    public Map<String, List<String>> getPkStudent() {
        return pkStudent;
    }

    public Map<String,List<String>> getAttributes() {
        return attributes;
    }

    public Map<String,List<String>> getAttributesStudent() {
        return attributesStudent;
    }

    public Map<String,String> getDependencies() {
        return dependencies;
    }

    public Map<String,String> getDependenciesStudent() {
        return dependenciesStudent;
    }

    public String getErrorLogSemantik() {
        return errorLogSemantik;
    }

    public String getErrorLogSyntax() {
        return errorLogSyntax;
    }

    public int getPointsPK() {
        return pointsPK;
    }

    public int getPointsAtt() {
        return pointsAtt;
    }

    public int getPointsDep() {
        return pointsDep;
    }
    public int getTotalPoints(){
        return this.pointsAtt + this.pointsPK + this.pointsDep;
    }

    private void solutionToUppercase(List<String> solution){
        List<String> solutionUppercase = new ArrayList<String>();
        for(String str : solution){
            solutionUppercase.add(str.toUpperCase());
        }
        this.solution = solutionUppercase;
    }

    public List<String> getAllowedAttributes() {
        return allowedAttributes;
    }

    public List<String> getAllowedAttributesStudent() {
        return allowedAttributesStudent;
    }
}
