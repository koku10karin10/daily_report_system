package controllers.projects;

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
import utils.DBUtil;

/**
 * Servlet implementation class ProjectNewServlet
 */
@WebServlet("/projects/new")
public class ProjectNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectNewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("_token", request.getSession().getId());
        EntityManager em = DBUtil.createEntityManager();

        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        }catch(NumberFormatException e){}
        List<Employee> employees = em.createNamedQuery("getAllCurrentEmployees", Employee.class)
                                    .setFirstResult(15*(page-1))
                                    .setMaxResults(15)
                                    .getResultList();

        long employees_count = (long)em.createNamedQuery("getCurrentEmployeesCount", Long.class)
                                        .getSingleResult();

        em.close();
        System.out.println("newが再読み込みされました。");
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);
        if(request.getSession().getAttribute("flush") != null){
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        System.out.println(request.getSession().getAttribute("errors"));
        if(request.getSession().getAttribute("errors") != null){

            System.out.println("errorrrrrrrr:"+ request.getSession().getAttribute("errors"));
            request.setAttribute("errors", request.getSession().getAttribute("errors"));
            request.getSession().removeAttribute("errors");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/projects/new.jsp");
        rd.forward(request, response);


    }

}
