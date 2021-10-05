package at.jku.dke.etutor.modules.xquery.analysis;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.xquery.BaseXApi;
import at.jku.dke.etutor.modules.xquery.ParameterException;
import at.jku.dke.etutor.modules.xquery.UrlContentException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.UUID;


/**
 *
 * @author Georg Nitsche, Florian Reisinger
 * @version 2016.7.11
 * @since 1.0
 */
public class XQProcessor {

    private final String QUESTION_FOLDER_BASE_NAME;

    public XQProcessor(ApplicationProperties properties){
        this.QUESTION_FOLDER_BASE_NAME = properties.getXquery().getQuestionFolderBaseName();
    }
    /**
     * Executes and evaluates an XQuery query.
     *
     * @param query
     *            The query to evaluate.
     * @param urlSet
     *            A set of all Urls in the query, which need to be changed
     * @return A String representing the result as it is returned by the
     *         underlying XQuery processor.
     * @throws IOException
     *             if the query has syntax errors.
     */
    public String executeQuery(String query, Set urlSet) throws IOException {
        UrlContentMap ucm = new UrlContentMap();
        String tempTableBaseName = "eTutor"
                + UUID.randomUUID().toString().replace("-", "");
        int tempTableUsed = 0;
        BaseXApi api = null;
        String result = "";
        try {
            api = new BaseXApi();
            if (urlSet != null) {
                for (Object o : urlSet) {
                    // This is the Url in the query --> Only Strings
                    String curUrl = (String) o;
                    curUrl = curUrl.trim();
                    String filename = curUrl
                            .substring("http://etutor.dke.uni-linz.ac.at/etutor/XML?id="
                                    .length());

                    File file = new File(QUESTION_FOLDER_BASE_NAME, filename
                            + ".xml");
                    BufferedReader reader = null;
                    String line = null;
                    String fileContentString = "";
                    try {
                        reader = new BufferedReader(new InputStreamReader(
                                new FileInputStream(file), "UTF-8"));
                        while ((line = reader.readLine()) != null) {
                            fileContentString += line;
                        }
                        // Create Temp table
                        tempTableUsed++;
                        String currentTempTableName = tempTableBaseName
                                + tempTableUsed;
                        api.createDatabase(currentTempTableName,
                                fileContentString);
                        ucm.addUrlAlias(currentTempTableName, curUrl);
                    } catch (IOException | ParameterException e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            reader.close();
                        }
                    }
                }
                query = ucm.getEncodedQuery(query);
            }

            result = api.executeQuery(query);
        } catch (UrlContentException e) {
            throw new IOException(e);
        } finally {
            try {
                if (api != null) {
                    while (tempTableUsed > 0) {
                        api.dropDatabase(tempTableBaseName + tempTableUsed);
                        tempTableUsed--;
                    }
                    api.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
