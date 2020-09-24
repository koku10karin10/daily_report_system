package controllers.progresses;

import java.io.IOException;
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
import utils.DBUtil;

/**
 * Servlet implementation class ProgressesFinishServlet
 */
@WebServlet("/progresses/finish")
public class ProgressesFinishServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProgressesFinishServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        //progress がまだ未作成だとエラーとなる
        try{
            Progress p = em.find(Progress.class, Integer.parseInt(request.getParameter("finish")));
            System.out.println("progressFinish::::"+p.getFinish());
            if(p.getFinish() == false){
                p.setFinish(true);
            }else{
                p.setFinish(false);
            }
        }catch(Exception e){
            System.out.println("Progressテーブルが未作成です。");
            request.setAttribute("errors", "「進捗管理内容を編集する」よりサブゴールを設定してください。");
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/progresses/index.jsp");
            rd.forward(request, response);
        }



        List<Progress> pl = em.createNamedQuery("getRegisteredProgresses", Progress.class)
                .setParameter("employee", login_employee)
                .getResultList();

        //全てのプログレスが完了の場合、自分の分のプロジェクト進捗も完了にする
        Employee e = em.find(Employee.class, login_employee.getId());
        if(pl.size() > 0){
            e.setProjectFinish(true);
            for(int i = 0 ; i < pl.size() ; i++){
                if(pl.get(i).getFinish() == false){
                    e.setProjectFinish(false);
                    break;
                }
            }
        }

        System.out.println("PFinish:::"+login_employee.getProjectFinish());

        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();




        response.sendRedirect(request.getContextPath() + "/progresses/index");
    }

}
