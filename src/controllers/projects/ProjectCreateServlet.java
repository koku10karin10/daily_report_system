package controllers.projects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Progress;
import models.Project;
import models.validators.ProjectValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ProjectCreateServlet
 */
@WebServlet("/projects/create")
public class ProjectCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            //check box の値を取得
           String[] eCodelist = request.getParameterValues("cBox");
           List<String> errors = new ArrayList<String>();
           if(eCodelist == null){
               String error = "プロジェクトに参加させる従業員を選択してください。";
               errors.add(error);
               System.out.println("ERORrrr:"+ error);
           }

           System.out.println("NAGASA:"+eCodelist);

           Project p = new Project();

           p.setTitle(request.getParameter("project_name"));



           System.out.println("errorsSize1;"+errors.size());
           String error2 = ProjectValidator.validate(p);
           if(error2 != null){
               errors.add(error2);
           }
           System.out.println("errorsSize2;"+errors.size());
           if(errors.size() > 0){
               em.close();
               System.out.println(errors);


               request.getSession().setAttribute("errors", errors);
               response.sendRedirect(request.getContextPath() + "/projects/new");


           }else{
               if(eCodelist != null){
                 //update login_employee.project
                   Employee newlogin_employee =(Employee)request.getSession().getAttribute("login_employee");
                   System.out.println("ProjectName::::"+p.getTitle());
                   System.out.println("LoginEmployeeName::::"+newlogin_employee.getName());
                   newlogin_employee.setProject(p);
                   request.getSession().removeAttribute("login_employee");
                   request.getSession().setAttribute("login_employee", newlogin_employee);

                   em.getTransaction().begin();
                   em.persist(p);
                   //check box の値から従業員を検索し、参加しているプロジェクトを登録
                       for(int i = 0 ; i < eCodelist.length ; i++){
                           Employee je = (Employee)em.createNamedQuery("searchEmployeeByCode", Employee.class)
                                                       .setParameter("code",eCodelist[i])
                                                       .getSingleResult();


                           je.setProject(p);
                           je.setProjectFinish(false);

                           List<Progress> prl = em.createNamedQuery("getRegisteredProgresses", Progress.class)
                                                   .setParameter("employee", je)
                                                   .getResultList();
                           System.out.println("prCount:::::"+prl);
                           if(prl.size() > 0){
                               for(int j = 0; j < prl.size() ; j++){
                                   em.remove(prl.get(j));
                                   em.flush();
                               }
                           }
                       }
                   }








              em.getTransaction().commit();
              em.close();


               request.getSession().setAttribute("flush", "登録が完了しました。");
               response.sendRedirect(request.getContextPath() + "/");

        }
    }
    }
}
