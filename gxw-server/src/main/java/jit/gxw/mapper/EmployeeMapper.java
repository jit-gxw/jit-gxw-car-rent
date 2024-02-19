package jit.gxw.mapper;


import com.github.pagehelper.Page;
import jit.gxw.dto.EmployeePageQueryDTO;
import jit.gxw.entity.Employee;
import jit.gxw.entity.EmployeeRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {


    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 根据用员工id查询角色id
     * @param id
     * @return
     */
    @Select("select employee_role.role_id from employee left join employee_role on employee.id=employee_role.employee_id where employee.id=#{id}")
    Long getRoleIdByUserId(Long id);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void insert(Employee employee);

    @Insert("insert into employee_role (employee_id, role_id) " +
            "values (#{employeeId},#{roleId})")
    void insertEmpIdAndRoleId(EmployeeRole employeeRole);

    Employee getById(Long id);

    void update(Employee employee);


    void updateEmpRole(EmployeeRole build);

    @Update("update employee set is_del=1 where id=#{id}")
    void setIsDel(Long id);
}
