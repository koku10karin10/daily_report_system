package controllers.progresses;

import java.io.IOException;
import java.sql.Date;
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
import utils.DBUtil;

/**
 * Servlet implementation class ProgressesUpdateServlet
 */
@WebServlet("/progresses/update")
public class ProgressesUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProgressesUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())){
            EntityManager em = DBUtil.createEntityManager();


            long registered_count = (long)em.createNamedQuery("checkRegisteredProgresses", Long.class)
                    .setParameter("employee", request.getSession().getAttribute("login_employee"))
                    .getSingleResult();

            if(registered_count > 0){
                //update
                em.getTransaction().begin();
                for(int i = 1 ; i <= registered_count ; i++){
                    Progress p = em.find(Progress.class, Integer.parseInt(request.getParameter("pId"+i)));
                    p.setStep(request.getParameter("step"+i));
                  //limitの値がnullかどうかで条件分岐
                    String ld = request.getParameter("limit"+i);
                    if(ld != null && !ld.equals("")){
                        Date limitDay = Date.valueOf(ld);
                        p.setLimit(limitDay);
                    }else{
                        p.setLimit(null);
                    }

                }
                em.getTransaction().commit();
                em.close();

                request.getSession().setAttribute("flush","更新が完了しました。" );
                response.sendRedirect(request.getContextPath() + "/progresses/index");

            }else{
                //create
                List<Progress> pl = new ArrayList<Progress>();



                int progressCount = 5;


                for(int i = 0 ; i <= progressCount ; i++){
                    Progress p = new Progress();
                    p.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
                    p.setPhase(i);
                    p.setStep(request.getParameter("step"+i));
                    p.setFinish(false);
                    System.out.println("limit:::"+request.getParameter("limit"+i));
                    //limitの値がnullかどうかで条件分岐
                    String ld = request.getParameter("limit"+i);
                    System.out.println(ld);
                    if(ld != null && !ld.equals("")){
                        Date limitDay = Date.valueOf(ld);
                        p.setLimit(limitDay);
                    }
                    p.setFinish(false);
                    pl.add(p);
                }

                em.getTransaction().begin();
                System.out.println("pl1::::"+pl.get(1));
                for(int i = 1 ; i < pl.size() ; i++){
                    em.persist(pl.get(i));
                }


                em.getTransaction().commit();
                em.close();

                System.out.println("(CREATE)getPARAmeter:1::"+request.getParameter("step1"));
                System.out.println("(CREATE)getPARAmeter:3::"+request.getParameter("step3"));
                System.out.println("(CREATE)getPARAmeter:5::"+request.getParameter("step5"));


                request.getSession().setAttribute("flush","更新が完了しました。" );
                response.sendRedirect(request.getContextPath() + "/progresses/index");
            }

        }
    }

}
