package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.XMLDefinitionDTO;
import at.jku.dke.etutor.modules.xquery.exercise.XQExerciseManagerImpl;
import at.jku.dke.etutor.modules.xquery.util.XMLUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class XQueryResourceService {
    private ApplicationProperties properties;
    private XQExerciseManagerImpl xqExerciseManager;

    public XQueryResourceService(ApplicationProperties properties, XQExerciseManagerImpl xqExerciseManager){
        this.properties = properties;
        this.xqExerciseManager = xqExerciseManager;
    }

    public boolean taskGroupExists(String taskGroupUUID){
        return false;
    }

    public void createXMLFiles(XMLDefinitionDTO dto, int diagnoseId, int submissionId) throws IOException {
        File diagnoseFile;
        File submissionFile;

        diagnoseFile = new File(properties.getXquery().getQuestionFolderBaseName()+"/"+diagnoseId+".xml");
        XMLUtil.printFile(dto.getDiagnoseXML(), diagnoseFile);

        submissionFile = new File(properties.getXquery().getQuestionFolderBaseName()+"/"+submissionId+".xml");
        XMLUtil.printFile(dto.getSubmissionXML(), submissionFile);
    }
}
