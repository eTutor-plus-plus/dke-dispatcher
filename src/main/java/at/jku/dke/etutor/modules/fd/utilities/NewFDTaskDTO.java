package at.jku.dke.etutor.modules.fd.utilities;

import java.util.Arrays;

public class NewFDTaskDTO {
    String taskGroupId;
    String fDSubtype;
    String [] fDClosureIds;

    public NewFDTaskDTO() {
    }

//    public NewFDTaskDTO(String taskGroupId, String fDSubtype, String [] fDClosureIds) {
//        this.taskGroupId = taskGroupId;
//        this.fDSubtype = fDSubtype;
//        this.fDClosureIds = fDClosureIds;
//    }
//    public NewFDTaskDTO(String taskGroupId, String fDSubtype) {
//        this.taskGroupId = taskGroupId;
//        this.fDSubtype = fDSubtype;
//        this.fDClosureIds = null;
//    }

    @Override
    public String toString() {
        return "NewFDTaskDTO{" +
                "taskGroupId='" + taskGroupId + '\'' +
                ", fdSubtype='" + fDSubtype + '\'' +
                ", fDClosureIds=" + Arrays.toString(fDClosureIds) +
                '}';
    }
}