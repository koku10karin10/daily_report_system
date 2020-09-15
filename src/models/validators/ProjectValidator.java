package models.validators;

import models.Project;

public class ProjectValidator {
    public static String validate(Project p){
        String title_error = _validateTitle(p.getTitle());

        return title_error;


    }

    private static String _validateTitle(String title){
        if(title == null || title.equals("")){
            return "プロジェクト名を入力してください。";
        }
        return "";
    }

}
