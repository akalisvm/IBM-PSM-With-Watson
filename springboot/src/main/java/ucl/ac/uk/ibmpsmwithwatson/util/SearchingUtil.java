package ucl.ac.uk.ibmpsmwithwatson.util;

import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.RecordQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Record;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Template;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.RecordVO;

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

    public static void searchingRecordByIdAndFilters(List<RecordVO> list, RecordQueryDTO dto) {
        if(!dto.getSearchInput().equals("")) {
            if(dto.getSearchInput().matches("[0-9]+")) {
                for(int i = list.size() - 1; i >= 0; i--) {
                    RecordVO record = list.get(i);
                    if(!record.getId().equals(dto.getSearchInput())) {
                        list.remove(record);
                    }
                }
            } else {
                list.clear();
                return;
            }
        }
        if(!dto.getPatientFilter().equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                RecordVO recordVO = list.get(i);
                if(!recordVO.getCreatorId().equals(dto.getPatientFilter())) {
                    list.remove(recordVO);
                }
            }
        }
        if(!dto.getResultFilter().equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                RecordVO recordVO = list.get(i);
                if(!recordVO.getQuestionnaire().getResult().equals(dto.getResultFilter())) {
                    list.remove(recordVO);
                }
            }
        }
        if(!dto.getNeedMeetingFilter().equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                RecordVO recordVO = list.get(i);
                if(!recordVO.getQuestionnaire().getNeedMeeting().equals(dto.getNeedMeetingFilter())) {
                    list.remove(recordVO);
                }
            }
        }
    }
}
