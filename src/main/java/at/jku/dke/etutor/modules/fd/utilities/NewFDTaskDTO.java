package at.jku.dke.etutor.modules.fd.utilities;

import java.util.Arrays;

public class NewFDTaskDTO {
    String taskGroupId;
    String fDSubtype;
    String [] fDClosureIds;

    public NewFDTaskDTO() {
        //empty constructor
    }
    @Override
    public String toString() {
        return "NewFDTaskDTO{" +
                "taskGroupId='" + taskGroupId + '\'' +
                ", fdSubtype='" + fDSubtype + '\'' +
                ", fDClosureIds=" + Arrays.toString(fDClosureIds) +
                '}';
    }
}