package controllers.progresses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Progress;
import models.Project;
import utils.DBUtil;
/**
 * Servlet implementation class ProgressIndexServlet
 */
@WebServlet("/progresses/index")
public class ProgressIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProgressIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");


        if(login_employee.getProject() != null){
            long registered_count = (long)em.createNamedQuery("checkRegisteredProgresses", Long.class)
                    .setParameter("employee", login_employee)
                    .getSingleResult();
            Project progressing_project = em.find(Project.class, login_employee.getProject().getId());

            List<Employee> joinedEmployees = em.createNamedQuery("getJoinProjectEmployee", Employee.class)
                                                .setParameter("project_id", login_employee.getProject())
                                                .getResultList();


            String project_name = progressing_project.getTitle();
            //自分のプロジェクトに何人参加しているか
            int pEmployeeCount = joinedEmployees.size();
            //プロジェクト内で自分のタスクが完了している人を抽出する
            int finishEmployeeCount = 0;
            for(int i = 0 ; i < joinedEmployees.size() ; i++){
                Employee e = joinedEmployees.get(i);
                if(e.getProjectFinish() == true){
                    finishEmployeeCount ++;
                }
            }
            System.out.println("finishemployeeproject:::" + login_employee.getProjectFinish());
            System.out.println("finishemployeeCount:::" + finishEmployeeCount);

            int percent = 100 * finishEmployeeCount / pEmployeeCount;

            request.setAttribute("project_name", project_name);
            request.setAttribute("joinedEmployees", joinedEmployees);
            request.setAttribute("percent", percent);

            List<Progress> pl = new ArrayList<Progress>();
            if(registered_count == 0){
                int progressCount = 5;
                for(int i = 1; i <= progressCount; i++){
                    Progress p = new Progress();
                    p.setEmployee(login_employee);
                    p.setPhase(i);
                    p.setFinish(false);
                    pl.add(p);

                }

                }else{
                    pl = em.createNamedQuery("getRegisteredProgresses", Progress.class)
                                        .setParameter("employee", login_employee)
                                        .getResultList();
                    System.out.println("plに値があるとわかった。："+pl);
                }

            request.setAttribute("progresses", pl);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/progresses/index.jsp");
            rd.forward(request, response);

        }else{
            request.getSession().setAttribute("errors", "現在プロジェクトに参加していません。");
            response.sendRedirect(request.getContextPath() + "/");
        }





    }


}
