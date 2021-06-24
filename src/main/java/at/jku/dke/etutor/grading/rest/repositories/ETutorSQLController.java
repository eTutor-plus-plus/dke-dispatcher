package at.jku.dke.etutor.grading.rest.repositories;

import at.jku.dke.etutor.grading.rest.dto.SchemaDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.logging.Logger;

@RestController
@RequestMapping("/sql")
public class ETutorSQLController {
    private Logger logger;

    public ETutorSQLController(){
        this.logger= Logger.getLogger("at.jku.dke.etutor.sqlcontroller");
    }

    @CrossOrigin(origins="*")
    @PostMapping("")
    public String initializeSchema(){
        String schemaName = "\"";
        // createSchemaName

        int leftLimit = 33; // numeral '0'
        int rightLimit = 126; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> i != 34)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        schemaName+=generatedString+"\"";

        //initialize schema
        
        // return schema

        return schemaName;
    }

}
