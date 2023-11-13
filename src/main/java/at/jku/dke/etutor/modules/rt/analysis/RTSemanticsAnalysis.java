package at.jku.dke.etutor.modules.rt.analysis;

import java.util.ArrayList;
import java.util.List;

public class RTSemanticsAnalysis {
    List<String> studentSolution;
    List<String> solution;
    List<String> relationsStudent = new ArrayList();
    List<String> relations = new ArrayList<>();
    List<String> pkStudent = new ArrayList<>();
    List<String> pk = new ArrayList<>();
    List<String> attributes = new ArrayList<>();
    List<String> attributesStudent = new ArrayList<>();
    List<String> dependencies = new ArrayList<>();
    List<String> dependenciesStudent = new ArrayList<>();


    public RTSemanticsAnalysis(List<String> studentSolution, List<String> solution) {
        this.studentSolution = studentSolution;
        this.solution = solution;
        this.calcRelations();
        this.clacPrimaryKey();
        this.clacAttributes();
        this.clacDependendcies();
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

    public boolean checkRelation(){
        for(String str : this.relationsStudent){
            if(!this.relations.contains(str)){
                return false;
            }
        }

        if(this.relations.size() != this.relationsStudent.size()){
            return false;
        }

        return true;
    }

    public boolean checkPrimaryKey(){
        for(String str : this.pkStudent){
            if(!this.pk.contains(str)){
                return false;
            }
        }
        return true;
    }

    public boolean checkAttributes(){
        for(String str : this.attributesStudent){
            if(!this.attributes.contains(str)){
                return false;
            }
        }
        return true;
    }

    public boolean checkDependendcies(){
        for(String str : this.dependenciesStudent){
            if(!this.dependencies.contains(str)){
                System.out.println("Error in Attributes: " + str);
                return false;
            }
        }
        return true;
    }

    public void clacPrimaryKey(){
        for(String str : this.solution) {
            int indexOfFirstPipe = str.indexOf('|');
            int indexOfLastPipe = str.lastIndexOf('|');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                this.pk.add(extractedText);
            }
        }

        for(String str : this.studentSolution) {
            int indexOfFirstPipe = str.indexOf('|');
            int indexOfLastPipe = str.lastIndexOf('|');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                this.pkStudent.add(extractedText);
            }
        }
    }

    public void clacAttributes(){
        for(String str : this.solution) {
            int indexOfFirstPipe = str.lastIndexOf('|');
            int indexOfLastPipe = str.indexOf(')');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                this.attributes.add(extractedText);
            }
        }

        for(String str : this.studentSolution) {
            int indexOfFirstPipe = str.lastIndexOf('|');
            int indexOfLastPipe = str.indexOf(')');
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                this.attributesStudent.add(extractedText);
            }
        }
    }

    public void clacDependendcies() {
        for (String str : this.solution) {
            int indexOfFirstPipe = str.indexOf(')');
            int indexOfLastPipe = str.length();
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                this.dependencies.add(extractedText);
            }
        }

        for (String str : this.studentSolution) {
            int indexOfFirstPipe = str.indexOf(')');
            int indexOfLastPipe = str.length();
            if (indexOfFirstPipe != -1 && indexOfLastPipe != -1 && indexOfFirstPipe < indexOfLastPipe) {
                String extractedText = str.substring(indexOfFirstPipe + 1, indexOfLastPipe);
                this.dependenciesStudent.add(extractedText);
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

    public List<String> getPk() {
        return pk;
    }

    public List<String> getPkStudent() {
        return pkStudent;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public List<String> getAttributesStudent() {
        return attributesStudent;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public List<String> getDependenciesStudent() {
        return dependenciesStudent;
    }

}
