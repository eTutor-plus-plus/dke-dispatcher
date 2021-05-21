package at.jku.dke.etutor.grading.rest.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

/**
 * Wrapper for and Generator of unique submissionId's
 *  (in order to facilitate sending the id as part of an EntityModel<T> Object(no Strings possible))
 */

public class SubmissionId {
    private String submissionId;

    public SubmissionId(){};
    public SubmissionId(String value){
        this.submissionId=value;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    /**
     * takes the submission and generates a unique uuid depending on the byte[] of the submission
     * (same submissions produce same uuid)
     * sets the submissionId for the Submission-Object and persists it using the corresponding JpaRepository
     * @param submission The submission from the student
     * @return a new SubmissionId Instance which wraps the generated ID
     * @throws IOException
     */
    public static SubmissionId createId(Submission submission) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(submission);
        oos.flush();
        byte[] data = bos.toByteArray();

        UUID uuid = UUID.nameUUIDFromBytes(data);
        submission.setSubmissionId(uuid.toString());
        return new SubmissionId(UUID.randomUUID().toString());
    }


    public String toString(){
        return submissionId;
    }
}
