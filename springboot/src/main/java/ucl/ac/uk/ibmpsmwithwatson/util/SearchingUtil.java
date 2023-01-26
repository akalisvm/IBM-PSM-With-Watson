package ucl.ac.uk.ibmpsmwithwatson.util;

import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.EventQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.RecordQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Template;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.EventVO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.RecordVO;

import java.util.List;

public class SearchingUtil {
    public static void searchingUser(List<User> list, String searchInput) {
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

    public static void searchingTemplate(List<Template> list, String searchInput) {
        if(!searchInput.equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                Template template = list.get(i);
                if(!template.getId().equals(searchInput) &&
                        !template.getTitle().toLowerCase().contains(searchInput)) {
                    list.remove(template);
                }
            }
        }
    }

    public static void searchingQuestionnaire(List<Questionnaire> list, String searchInput) {
        if(!searchInput.equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                Questionnaire questionnaire = list.get(i);
                if(!questionnaire.getId().equals(searchInput) &&
                        !questionnaire.getTitle().toLowerCase().contains(searchInput)) {
                    list.remove(questionnaire);
                }
            }
        }
    }

    public static void searchingRecord(List<RecordVO> list, RecordQueryDTO dto) {
        if(!dto.getSearchInput().equals("")) {
            if(dto.getSearchInput().matches("[0-9]+")) {
                for(int i = list.size() - 1; i >= 0; i--) {
                    RecordVO recordVO = list.get(i);
                    if(!recordVO.getId().equals(dto.getSearchInput())) {
                        list.remove(recordVO);
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

    public static void searchingEvent(List<EventVO> list, EventQueryDTO dto) {
        if(!dto.getSearchInput().equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                EventVO eventVO = list.get(i);
                if(!eventVO.getId().equals(dto.getSearchInput()) &&
                        !eventVO.getTitle().toLowerCase().contains(dto.getSearchInput())) {
                    list.remove(eventVO);
                }
            }
        }
        if(!dto.getPatientFilter().equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                EventVO eventVO = list.get(i);
                if(!eventVO.getParticipantId().equals(dto.getPatientFilter())) {
                    list.remove(eventVO);
                }
            }
        }
        if(!dto.getPlatformFilter().equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                EventVO eventVO = list.get(i);
                if(!eventVO.getPlatform().equals(dto.getPlatformFilter())) {
                    list.remove(eventVO);
                }
            }
        }
        if(!dto.getResultFilter().equals("")) {
            for(int i = list.size() - 1; i >= 0; i--) {
                EventVO eventVO = list.get(i);
                if(!eventVO.getResult().equals(dto.getResultFilter())) {
                    list.remove(eventVO);
                }
            }
        }
    }
}
