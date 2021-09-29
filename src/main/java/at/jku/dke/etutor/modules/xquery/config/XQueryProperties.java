package at.jku.dke.etutor.modules.xquery.config;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "xquery")
public class XQueryProperties {
    private ApplicationProperties applicationProperties;
    private Table table;
    private String databasePath;
    private String databaseUser;
    private String databasePwd;

    public XQueryProperties(ApplicationProperties applicationProperties){

    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public static class Table{
        private Error error;
        private String urls;
        private String sortings;
        private String exercise;

        public Error getError() {
            return error;
        }

        public void setError(Error error) {
            this.error = error;
        }

        public String getUrls() {
            return urls;
        }

        public void setUrls(String urls) {
            this.urls = urls;
        }

        public String getSortings() {
            return sortings;
        }

        public void setSortings(String sortings) {
            this.sortings = sortings;
        }

        public String getExercise() {
            return exercise;
        }

        public void setExercise(String exercise) {
            this.exercise = exercise;
        }

        public static class Error{
            private String categories;
            private String grading;

            public String getCategories() {
                return categories;
            }

            public void setCategories(String categories) {
                this.categories = categories;
            }

            public String getGrading() {
                return grading;
            }

            public void setGrading(String grading) {
                this.grading = grading;
            }
        }
    }
}
