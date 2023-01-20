package ucl.ac.uk.ibmpsmwithwatson.util;

import ucl.ac.uk.ibmpsmwithwatson.entity.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.entity.Record;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

import java.util.List;

public class SearchingUtil {
    public static void searchingUserByName(List<User> list, String searchInput) {
        if(!searchInput.equals("")) {
            if(searchInput.matches("[a-zA-Z]+")) {
                searchInput = searchInput.toLowerCase();
                for(int i = list.size() - 1; i >= 0; i--) {
                    User user = list.get(i);
                    String givenName = user.getGiven_name().toLowerCase();
                    String familyName = user.getFamily_name().toLowerCase();
                    if(!givenName.contains(searchInput) && !familyName.contains(searchInput)) {
                        list.remove(user);
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
                Template template = list.get(i);
                if(!template.getId().equals(searchInput) &&
                        !template.getTitle().toLowerCase().contains(searchInput.trim().toLowerCase())) {
                    list.remove(template);
                }
            }
        }
    }

    public static void searchingQuestionnaireByIdOrTitle(List<Questionnaire> list, String searchInput) {
        if(!searchInput.equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                Questionnaire questionnaire = list.get(i);
                if(!questionnaire.getId().equals(searchInput) &&
                        !questionnaire.getTitle().toLowerCase().contains(searchInput.trim().toLowerCase())) {
                    list.remove(questionnaire);
                }
            }
        }
    }

    public static void searchingRecordByIdAndFilters(List<Record> list, String searchInput, String resultFilter, String needMeetingFilter) {
        if(!searchInput.equals("")) {
            if(searchInput.matches("[0-9]+")) {
                System.out.println(searchInput);
                for(int i = list.size() - 1; i >= 0; i--) {
                    Record record = list.get(i);
                    if(!record.getId().equals(searchInput)) {
                        list.remove(record);
                    }
                }
            } else {
                list.clear();
                return;
            }
        }
        if(!resultFilter.equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                Record record = list.get(i);
                if(!record.getQuestionnaire().getResult().equals(resultFilter)) {
                    list.remove(record);
                }
            }
        }
        if(!needMeetingFilter.equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                Record record = list.get(i);
                if(!record.getQuestionnaire().getNeedMeeting().equals(needMeetingFilter)) {
                    list.remove(record);
                }
            }
        }
    }
}
