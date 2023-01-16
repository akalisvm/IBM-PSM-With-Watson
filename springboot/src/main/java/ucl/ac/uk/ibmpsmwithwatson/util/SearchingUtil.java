package ucl.ac.uk.ibmpsmwithwatson.util;

import ucl.ac.uk.ibmpsmwithwatson.entity.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

import java.util.List;

public class SearchingUtil {
    public static void searchingUserByName(List<User> list, String searchInput) {
        if(!searchInput.equals("")) {
            if(searchInput.matches("[a-zA-Z]+")) {
                searchInput = searchInput.toLowerCase();
                for(int i = list.size() - 1; i >= 0; i--) {
                    User temp = list.get(i);
                    String givenName = temp.getGiven_name().toLowerCase();
                    String familyName = temp.getFamily_name().toLowerCase();
                    if(!givenName.contains(searchInput) && !familyName.contains(searchInput)) {
                        list.remove(temp);
                    }
                }
            } else {
                list.clear();
            }
        }
    }

    public static void searchingTemplateByIdOrTitle(List<Template> list, String searchInput) {
        if(!searchInput.equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                Template temp = list.get(i);
                if(!temp.getId().equals(searchInput) &&
                        !temp.getTitle().toLowerCase().contains(searchInput.trim().toLowerCase())) {
                    list.remove(temp);
                }
            }
        }
    }

    public static void searchingQuestionnaireByIdOrTitle(List<Questionnaire> list, String searchInput) {
        if(!searchInput.equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                Questionnaire temp = list.get(i);
                if(!temp.getId().equals(searchInput) &&
                        !temp.getTitle().toLowerCase().contains(searchInput.trim().toLowerCase())) {
                    list.remove(temp);
                }
            }
        }
    }
}
