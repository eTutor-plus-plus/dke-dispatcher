package at.jku.dke.etutor.grading.rest.dto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

/**
 * Used as a Wrapper and Generator for the submissionId
 *  in order to facilitate sending the id as part of an EntityModel<T> Object
 */
public class SubmissionId {
    private String value;
    public SubmissionId(String value){
        this.value=value;
    }

    public String getId() {
        return value;
    }

    public void setId(String value) {
        this.value = value;
    }

    public static SubmissionId createId(Submission submission) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(submission);
        oos.flush();
        byte[] data = bos.toByteArray();

        UUID uuid = UUID.nameUUIDFromBytes(data);
        return new SubmissionId(uuid.toString());
    }


    public String toString(){
        return value;
    }
}
