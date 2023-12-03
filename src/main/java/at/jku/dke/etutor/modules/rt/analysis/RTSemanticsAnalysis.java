package at.jku.dke.etutor.modules.rt.analysis;

import java.util.*;

public class RTSemanticsAnalysis {
    List<String> studentSolution;
    List<String> solution;
    List<String> relationsStudent = new ArrayList();
    List<String> relations = new ArrayList<>();
    Map<String,String> pkStudent = new HashMap<String,String>();
    Map<String,String> pk = new HashMap<String,String>();
    Map<String,String> attributes = new HashMap<String,String>();
    Map<String,String>attributesStudent = new HashMap<String,String>();
    Map<String,String> dependencies = new HashMap<String,String>();
    Map<String,String> dependenciesStudent = new HashMap<String,String>();
    String errorLogSemantik = "";
    String errorLogSyntax = "";
    int [] weighting;
    int pointsPK = 0;
    int pointsAtt = 0;
    int pointsDep = 0;
    int countRelations = 0;


    public RTSemanticsAnalysis(List<String> studentSolution, List<String> solution, int [] weighting) {
        this.studentSolution = studentSolution;
        this.solution = solution;
        this.weighting = weighting;
        this.calcRelations();
        this.countRelations();
        this.clacPrimaryKey();
        this.clacAttributes();
        this.clacDependendcies();
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
                this.errorLogSyntax = this.errorLogSyntax.concat("<br>Falsche Relation! (Tipp: Rechtschreibung prüfen!)");
                return false;
            }
        }
        return true;
    }

    public boolean checkPrimaryKey(){
        boolean check = true;
        for (Map.Entry<String,String> entry : this.pk.entrySet()){
            String solution = entry.getValue();
            String student = this.pkStudent.get(entry.getKey());
            if (!solution.equals(student)) {
                this.errorLogSemantik = this.errorLogSemantik.concat("<br>Fehler in der Relation " + entry.getKey() + ": Falscher Primärschlüssel!");
                check = false;
            }
            else {
                pointsPK = pointsPK + (weighting[0] / countRelations);
            }
        }
        return check;
    }

    public boolean checkAttributes(){
        boolean check = true;
        for (Map.Entry<String,String> entry : this.attributes.entrySet()){
            String solution = entry.getValue();
            String student = this.attributesStudent.get(entry.getKey());
            if (!solution.equals(student)) {
                this.errorLogSemantik = this.errorLogSemantik.concat("<br>Fehler in der Relation " + entry.getKey() + ": Falsche/s, redundante/s oder fehlende/s Attributt/e!");
                check = false;
            }
            else {
                pointsAtt = pointsAtt + (weighting[1] / countRelations);
            }
        }
        return check;
    }

    public boolean checkDependendcies(){
        boolean check = true;
        for (Map.Entry<String,String> entry : this.dependencies.entrySet()){
            String solution = entry.getValue();
            String student = this.dependenciesStudent.get(entry.getKey());
            if (!solution.equals(student)) {
                this.errorLogSemantik = this.errorLogSemantik.concat("<br>Fehler in der Relation " + entry.getKey() + ": Falsche, redundante oder fehlende Inklusions-Abhängigkeit/en!");
                check = false;
            }
            else{
                pointsDep = pointsDep + (weighting[2] / countRelations);
            }
        }
        return check;
    }

    public void clacPrimaryKey(){
        for(String str : this.solution) {
            int indexOfFirstPipe = str.indexOf('|');
            int indexOfLastPipe = str.lastIndexOf('|');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                String key = this.getRelation(str);
                this.pk.put(key,extractedText);
            }
        }

        for(String str : this.studentSolution) {
            int indexOfFirstPipe = str.indexOf('|');
            int indexOfLastPipe = str.lastIndexOf('|');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                String key = this.getRelation(str);
                this.pkStudent.put(key,extractedText);
            }
        }
    }

    public void clacAttributes(){
        for(String str : this.solution) {
            int indexOfFirstPipe = str.lastIndexOf('|');
            int indexOfLastPipe = str.indexOf(')');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                String key = this.getRelation(str);
                this.attributes.put(key,extractedText);
            }
        }

        for(String str : this.studentSolution) {
            int indexOfFirstPipe = str.lastIndexOf('|');
            int indexOfLastPipe = str.indexOf(')');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                String key = this.getRelation(str);
                this.attributesStudent.put(key,extractedText);
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

    public Map<String,String> getPk() {
        return pk;
    }

    public Map<String, String> getPkStudent() {
        return pkStudent;
    }

    public Map<String,String> getAttributes() {
        return attributes;
    }

    public Map<String,String> getAttributesStudent() {
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

    @Override
    public String toString() {
        return "RTSemanticsAnalysis{" +
                "studentSolution=" + studentSolution +
                ", solution=" + solution +
                ", relationsStudent=" + relationsStudent +
                ", relations=" + relations +
                '}';
    }
}
