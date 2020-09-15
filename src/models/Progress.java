package models;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "progresses")
@NamedQueries({
    @NamedQuery(
            name = "checkRegisteredProgresses",
            query = "SELECT COUNT(p) FROM Progress AS p WHERE p.employee = :employee"
            )
})
@Entity
public class Progress {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "phase" , nullable = false)
    private Integer phase;

    @Column(name = "step" , nullable = false)
    private String step;

    @Column(name = "limit" , nullable = false)
    private Date limit;

    @Column(name = "finish" , nullable = false)
    private Boolean finish;


    //setter and getter
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getPhase(){
        return phase;
    }
    public void setPhase(Integer phase){
        this.phase = phase;
    }

    public String getStep(){
        return step;
    }
    public void setStep(String step){
        this.step = step;
    }


    public Date getLimit(){
        return limit;
    }
    public void setLimit(Date limit){
        this.limit = limit;
    }

    public Boolean getFinish(){
        return finish;
    }
    public void setFinish(Boolean finish){
        this.finish = finish;
    }
}
