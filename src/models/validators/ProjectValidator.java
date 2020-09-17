package models.validators;

import models.Project;

public class ProjectValidator {
    public static String validate(Project p){
        String title_error = _validateTitle(p.getTitle());
        if(!title_error.equals("")){
            return title_error;
        }

        return null;
    }


    private static String _validateTitle(String title){
        if(title == null || title.equals("")){
            return "プロジェクト名を入力してください。";
        }
        return "";
    }

}
