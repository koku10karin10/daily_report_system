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
import utils.DBUtil;

/**
 * Servlet implementation class ProgressesEditServlet
 */
@WebServlet("/progresses/edit")
public class ProgressesEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProgressesEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getSession().getId();

        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        long registered_count = (long)em.createNamedQuery("checkRegisteredProgresses", Long.class)
                .setParameter("employee", login_employee)
                .getSingleResult();
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
            }

        em.close();
        request.setAttribute("_token", _token);
        request.setAttribute("pl", pl);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/progresses/edit.jsp");
        rd.forward(request, response);


    }

}
